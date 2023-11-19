package WizardTD.game.board.layer;

import WizardTD.App;
import WizardTD.data.Point;
import WizardTD.game.board.Board;
import WizardTD.game.board.object.Monster;
import WizardTD.game.board.object.Tower;
import WizardTD.util.RandomUtils;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws and updates towers on the Board
 */
public class TowerLayer extends BoardLayer {
    private List<Tower> towers;
    private Tower hoveredTower;

    public TowerLayer(Board board) {
        super(board);
    }

    @Override
    public void create() {
        towers = new ArrayList<>();
    }

    public void update(long deltaMs) {
        updateHovered();
        for (Tower tower : towers) {
            tower.update(deltaMs);
            if (!tower.isReadyToShoot()) {
                continue;
            }
            Monster monster = getRandomNearbyMonster(tower);
            if (monster != null) {
                tower.goOnCooldown();
                board.spawnFireball(tower, monster);
            }
        }
    }

    public void updateHovered() {
        hoveredTower = null;
        for (Tower tower : towers) {
            tower.updateHovered();
            if (tower.isHovered()) {
                hoveredTower = tower;
            }
        }
    }

    public Tower getHoveredTower() {
        return hoveredTower;
    }

    private Monster getRandomNearbyMonster(Tower tower) {
        List<Monster> monsters = new ArrayList<>();

        for (Monster monster : board.getMonsters()) {
            if (monster.status != Monster.Status.ALIVE) continue;
            Point towerCenter = tower.position.center();
            PVector monsterCenter = monster.pos.center();
            float dist = PApplet.dist(towerCenter.x, towerCenter.y, monsterCenter.x, monsterCenter.y);

            if (dist <= tower.getRange()) {
                monsters.add(monster);
            }
        }

        return RandomUtils.choiceOrNull(monsters);
    }

    public void draw() {
        for (Tower tower : towers) {
            tower.draw();
        }
    }

    public void spawnTower(Point boardPosition) {
        Point screenPosition = board.boardPointToScreenPoint(boardPosition);

        Tower tower = new Tower(board.applet, board.resources.towerImages,
                boardPosition, screenPosition,
                Math.round(board.resources.config.initialTowerRange * App.UI_SCALE),
                board.resources.config.initialTowerFiringSpeed,
                board.resources.config.initialTowerDamage
        );
        towers.add(tower);
    }

    public Tower findTower(Point boardPoint) {
        return towers.stream()
                .filter(t -> t.boardPosition.equals(boardPoint))
                .findFirst()
                .orElse(null);
    }
}
