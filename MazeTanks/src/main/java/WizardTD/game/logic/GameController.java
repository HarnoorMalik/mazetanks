package WizardTD.game.logic;

import WizardTD.App;
import WizardTD.Resources;
import WizardTD.data.Point;
import WizardTD.data.config.MonsterWaveInfo;
import WizardTD.game.GameMap;
import WizardTD.game.GameScreen;
import WizardTD.game.board.Board;
import WizardTD.game.board.object.Monster;
import WizardTD.game.board.object.Tower;
import WizardTD.game.ui.SideBar;
import WizardTD.ui.Component;
import WizardTD.ui.SquareButton;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class GameController extends Component implements Board.Callbacks, SquareButton.ClickListener {

    private final GameScreen gameScreen;
    private final Resources resources;

    private WaveController waveController;
    private ManaController manaController;
    private long lastTime = -1;

    private boolean isPaused = false;
    private boolean isFastForward = false;

    public GameController(GameScreen gameScreen, Resources resources) {
        super(null, null);
        this.gameScreen = gameScreen;
        this.resources = resources;

        startGame();
    }

    public void startGame() {
        waveController = new WaveController(resources.config.waves);
        manaController = new ManaController(resources.config);
        gameScreen.board.callbacks = this;
        gameScreen.sideBar.onButtonClickListener = this;
        gameScreen.sideBar.setTowerCost(resources.config.towerCost);
    }

    // Here I don't draw, but just update the game logic
    @Override
    public void draw() {
        if (gameScreen.board.gameStatus != Board.GameStatus.IN_PROGRESS) return;

        long currentTime = System.currentTimeMillis();
        if (lastTime == -1) {
            lastTime = currentTime;
        }
        long deltaMs = currentTime - lastTime;
        lastTime = currentTime;

        // for the fast-forward mode
        long deltaMsX2 = deltaMs;
        if (isFastForward) {
            deltaMsX2 *= 2;
        }

        gameScreen.sideBar.update(deltaMsX2);

        if (isPaused) {
            // even if the game is paused, I need to correctly update mana values
            // because a user can build towers and use spells
            showManaValues();

            // I need to update "hovered" tower indicators when game is paused, but not when the game is won/lost
            gameScreen.board.updateHovered();
            updateTowerUpgradeTooltip();

            return;
        }

        waveController.update(deltaMsX2);

        gameScreen.topBar.setTitle(waveController.getWaveTitle());
        MonsterWaveInfo monsterToSpawn = waveController.getMonsterToSpawn();
        if (monsterToSpawn != null) {
            gameScreen.board.spawnMonster(monsterToSpawn);
        }

        gameScreen.board.update(deltaMs);
        // for the board we need to call update 2 times for fast-forward mode
        if (isFastForward) {
            gameScreen.board.update(deltaMs);
        }
        updateTowerUpgradeTooltip();

        manaController.update(deltaMsX2);
        showManaValues();

        if (manaController.isLost) {
            isPaused = true;
            gameScreen.board.gameStatus = Board.GameStatus.LOST;
        } else if (checkWin()) {
            isPaused = true;
            gameScreen.board.gameStatus = Board.GameStatus.WON;
        }
    }

    private void showManaValues() {
        gameScreen.topBar.setMana(Math.round(manaController.mana), manaController.maximumMana);
        gameScreen.sideBar.setManaPoolUpgradeCost(manaController.upgradeCost);
    }

    private boolean checkWin() {
        return waveController.getCurrentWaveNumber() == WaveController.WAVE_NUMBER_AFTER_LAST_WAVE &&
                gameScreen.board.getMonsters().isEmpty();
    }

    public void keyPressed(KeyEvent e) {
        System.out.println("*Key pressed: " + e.getKey());
        if (e.getKey() == 'r') {
            restartGame();
        } else if (e.getKey() == 'f') {
            toggleFastForward();
        } else if (e.getKey() == 'p') {
            togglePause();
        } else if (e.getKey() == 't') {
            gameScreen.sideBar.toggleButtonSelected(SideBar.BUTTON_TOWER_TEXT);
        } else if (e.getKey() == '1') {
            gameScreen.sideBar.toggleButtonSelected(SideBar.BUTTON_UPGRADE_RANGE_TEXT);
        } else if (e.getKey() == '2') {
            gameScreen.sideBar.toggleButtonSelected(SideBar.BUTTON_UPGRADE_SPEED_TEXT);
        } else if (e.getKey() == '3') {
            gameScreen.sideBar.toggleButtonSelected(SideBar.BUTTON_UPGRADE_DAMAGE_TEXT);
        } else if (e.getKey() == 'm') {
            manaController.tryUpgradeManaPool();
        }
    }

    private void togglePause() {
        setPaused(!isPaused);
    }

    private void toggleFastForward() {
        setFastForward(!isFastForward);
    }

    private void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
        gameScreen.sideBar.setButtonSelected(SideBar.BUTTON_PAUSE_TEXT, isPaused);
    }

    private void setFastForward(boolean isFastForward) {
        this.isFastForward = isFastForward;
        gameScreen.sideBar.setButtonSelected(SideBar.BUTTON_FAST_FORWARD_TEXT, isFastForward);
    }


    private void restartGame() {
        setPaused(false);
        setFastForward(false);
        gameScreen.board.restart();
        gameScreen.sideBar.unselectAllButtons();
        startGame();
    }

    @Override
    public void onMonsterKilled(Monster monster) {
        manaController.onMonsterKilled(monster);
    }

    @Override
    public void onMonsterArrived(Monster monster) {
        manaController.onMonsterArrived(monster);
    }

    @Override
    public void onBoardClicked(Point boardPoint) {
        if (gameScreen.sideBar.isButtonSelected(SideBar.BUTTON_TOWER_TEXT)) {
            tryPlaceTower(boardPoint);
        }

        tryUpgradeTower(boardPoint);
    }

    private void tryPlaceTower(Point boardPoint) {
        boolean isGrass = gameScreen.board.gameMap.get(boardPoint) == GameMap.OBJECT_GRASS;
        if (!isGrass) return; // can place only on grass

        if (!manaController.tryConsumeMana(resources.config.towerCost)) {
            return; // not enough mana
        }
        gameScreen.board.gameMap.put(boardPoint, GameMap.OBJECT_TOWER);
        gameScreen.board.spawnTower(boardPoint);
    }

    private void tryUpgradeTower(Point boardPoint) {
        Tower tower = gameScreen.board.findTower(boardPoint);
        if (tower == null) return; // need to click on tower to upgrade it

        if (gameScreen.sideBar.isButtonSelected(SideBar.BUTTON_UPGRADE_RANGE_TEXT)) {
            if (manaController.tryConsumeMana(tower.getRangeUpgradeCost())) {
                tower.upgradeRange(tower.getRange() + App.CELL_SIZE);
            }
        }

        if (gameScreen.sideBar.isButtonSelected(SideBar.BUTTON_UPGRADE_SPEED_TEXT)) {
            if (manaController.tryConsumeMana(tower.getFireSpeedUpgradeCost())) {
                tower.upgradeFireSpeed(tower.getFireSpeed() + 0.5f);
            }
        }

        if (gameScreen.sideBar.isButtonSelected(SideBar.BUTTON_UPGRADE_DAMAGE_TEXT)) {
            if (manaController.tryConsumeMana(tower.getDamageUpgradeCost())) {
                int bonusDamage = Math.round(resources.config.initialTowerDamage * 0.5f);
                tower.upgradeDamage(tower.getDamage() + bonusDamage);
            }
        }
    }

    private void updateTowerUpgradeTooltip() {
        Tower tower = gameScreen.board.getHoveredTower();
        if (tower == null) {
            gameScreen.sideBar.setTowerUpgradeTooltip(null);
            return;
        }

        List<String> lines = new ArrayList<>();
        lines.add("Upgrade cost");
        int totalCost = 0;

        if (gameScreen.sideBar.isButtonSelected(SideBar.BUTTON_UPGRADE_RANGE_TEXT)) {
            totalCost += tower.getRangeUpgradeCost();
            lines.add("range: " + tower.getRangeUpgradeCost());
        }

        if (gameScreen.sideBar.isButtonSelected(SideBar.BUTTON_UPGRADE_SPEED_TEXT)) {
            totalCost += tower.getFireSpeedUpgradeCost();
            lines.add("speed: " + tower.getFireSpeedUpgradeCost());
        }

        if (gameScreen.sideBar.isButtonSelected(SideBar.BUTTON_UPGRADE_DAMAGE_TEXT)) {
            totalCost += tower.getDamageUpgradeCost();
            lines.add("damage: " + tower.getDamageUpgradeCost());
        }

        if (totalCost == 0) {
            gameScreen.sideBar.setTowerUpgradeTooltip(null);
            return;
        }

        lines.add("Total: " + totalCost);
        gameScreen.sideBar.setTowerUpgradeTooltip(lines);
    }

    @Override
    public void onClicked(SquareButton button) {
        String text = button.getText();
        System.out.println("** clicked on: " + text);

        if (text.equals(SideBar.BUTTON_MANA_POOL_TEXT)) {
            manaController.tryUpgradeManaPool();
        } else if (text.equals(SideBar.BUTTON_PAUSE_TEXT)) {
            togglePause();
        } else if (text.equals(SideBar.BUTTON_FAST_FORWARD_TEXT)) {
            toggleFastForward();
        } else {
            button.isSelected = !button.isSelected;
        }
    }
}
