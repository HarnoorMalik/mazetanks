package WizardTD.game.board;

import WizardTD.App;
import WizardTD.Resources;
import WizardTD.data.Point;
import WizardTD.data.Rectangle;
import WizardTD.data.config.MonsterWaveInfo;
import WizardTD.game.GameMap;
import WizardTD.game.board.layer.*;
import WizardTD.game.board.object.Monster;
import WizardTD.game.board.object.Tower;
import WizardTD.ui.Component;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.Arrays;
import java.util.List;

/**
 * Component, that draws the game board
 */
public class Board extends Component {
    public final Resources resources;
    public GameMap gameMap;

    private final MonsterLayer monsterLayer = new MonsterLayer(this);
    private final TowerLayer towerLayer = new TowerLayer(this);
    private final FireballLayer fireballLayer = new FireballLayer(this);

    public Callbacks callbacks;

    public GameStatus gameStatus;

    public Tower findTower(Point boardPoint) {
        return towerLayer.findTower(boardPoint);
    }

    public enum GameStatus {
        PAUSE, IN_PROGRESS, WON, LOST
    }

    private final List<BoardLayer> layers = Arrays.asList(
            new BackgroundLayer(this),
            new RoadLayer(this),
            monsterLayer,
            new WizardHouseLayer(this),
            towerLayer,
            fireballLayer,
            new EndGameLayer(this)
    );

    public Board(PApplet applet, Rectangle rect, Resources resources) {
        super(applet, rect);
        this.resources = resources;

        restart();
    }

    @Override
    public void draw() {
        super.draw();
        for (BoardLayer layer : layers) {
            layer.draw();
        }
    }

    @Override
    public void update(long deltaMs) {
        for (BoardLayer layer : layers) {
            layer.update(deltaMs);
        }
    }

    public void updateHovered() {
        towerLayer.updateHovered();
    }

    public Tower getHoveredTower() {
        return towerLayer.getHoveredTower();
    }

    public void spawnMonster(MonsterWaveInfo monsterToSpawn) {
        monsterLayer.spawnMonster(monsterToSpawn);
    }

    public void restart() {
        gameMap = new GameMap(resources.level);
        gameStatus = GameStatus.IN_PROGRESS;

        for (BoardLayer layer : layers) {
            layer.create();
        }

        monsterLayer.callbacks = monster -> {
            if (callbacks == null) return;
            if (monster.status == Monster.Status.DEAD) {
                callbacks.onMonsterKilled(monster);
            } else if (monster.status == Monster.Status.ARRIVED) {
                callbacks.onMonsterArrived(monster);
            }
        };
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point screenPoint = new Point(e.getX(), e.getY());
        Point boardPoint = screenPointToBoardPoint(screenPoint);
        if (boardPoint == null) return;
        if (callbacks != null) {
            callbacks.onBoardClicked(boardPoint);
        }
    }

    public void spawnTower(Point boardPoint) {
        towerLayer.spawnTower(boardPoint);
    }

    public Point screenPointToBoardPoint(Point screenPoint) {
        int boardX = screenPoint.x - rect.x;
        int boardY = screenPoint.y - rect.y;

        float rowF = (float) boardY / App.CELL_SIZE;
        float colF = (float) boardX / App.CELL_SIZE;
        if (boardX < 0 || boardY < 0 || boardX > rect.width || boardY > rect.height) {
            return null;
        }
        return new Point((int) colF, (int) rowF);
    }

    public Point boardPointToScreenPoint(Point boardPoint) {
        int x = boardPoint.x * App.CELL_SIZE + rect.x;
        int y = boardPoint.y * App.CELL_SIZE + rect.y;
        return new Point(x, y);
    }

    public void spawnFireball(Tower tower, Monster monster) {
        if (monsterLayer.monsters.isEmpty()) {
            return;
        }
        fireballLayer.spawnFireball(tower, monster);
    }

    public List<Monster> getMonsters() {
        return monsterLayer.monsters;
    }

    /**
     * Used to notify game logic about board events
     */
    public interface Callbacks {
        void onMonsterKilled(Monster monster);

        void onMonsterArrived(Monster monster);

        void onBoardClicked(Point boardPoint);
    }
}
