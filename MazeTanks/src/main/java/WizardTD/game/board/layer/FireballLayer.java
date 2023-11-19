package WizardTD.game.board.layer;

import WizardTD.App;
import WizardTD.data.Rectangle;
import WizardTD.game.board.Board;
import WizardTD.game.board.object.Fireball;
import WizardTD.game.board.object.Monster;
import WizardTD.game.board.object.Tower;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Holds, draws and updates fireballs. Should be placed in the Board
 */
public class FireballLayer extends BoardLayer {
    private static final int FIREBALL_SPEED = 5;
    private List<Fireball> fireballs;

    public FireballLayer(Board board) {
        super(board);
    }

    @Override
    public void create() {
        fireballs = new ArrayList<>();
    }

    public void update(long deltaMs) {
        Iterator<Fireball> iterator = fireballs.iterator();
        while (iterator.hasNext()) {
            Fireball fireball = iterator.next();
            fireball.update(deltaMs);
            if (fireball.isReachedTarget()) {
                iterator.remove();
            }
        }
    }

    public void draw() {
        for (Fireball fireball : fireballs) {
            fireball.draw();
        }
    }

    public void spawnFireball(Tower tower, Monster target) {
        Rectangle pos = tower.position;

        PVector centerPoint = new PVector(pos.x + App.CELL_SIZE / 2f, pos.y + App.CELL_SIZE / 2f);
        Fireball fireball = new Fireball(board.applet, board.resources.fireballImage,
                Math.round(App.CELL_SIZE / 4f), centerPoint, target, FIREBALL_SPEED * App.OBJECT_SPEED_SCALE, tower.getDamage());
        fireballs.add(fireball);
    }
}
