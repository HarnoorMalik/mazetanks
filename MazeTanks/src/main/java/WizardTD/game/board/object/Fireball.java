package WizardTD.game.board.object;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Fireball {
    private final PApplet applet;
    private final PImage image;
    private final int size;
    private final PVector posVector;
    private final Monster target;
    private final float speed;
    private final int damage;

    private boolean isReachedTarget;

    public Fireball(PApplet applet, PImage image, int size, PVector screenPosition, Monster target, float speed, int damage) {
        this.applet = applet;
        this.image = image;
        this.size = size;
        this.posVector = screenPosition;
        this.target = target;
        this.speed = speed;
        this.damage = damage;
    }

    public void update(long deltaMs) {
        if (isReachedTarget) return;

        // move in the center of the target monster
        PVector targetPos = this.target.pos.center();

        // absolute distance between our X(or Y) and target X (or Y)
        float xDist = Math.abs(targetPos.x - posVector.x);
        float yDist = Math.abs(targetPos.y - posVector.y);

        // check if we've reached the target
        if (xDist < 5f && yDist < 5f) {
            isReachedTarget = true;
            this.target.receiveDamage(damage);
            return;
        }

        // Here I divide x or y step value to make movements smoother
        float k, xStep, yStep;
        if (xDist > yDist) {
            k = xDist / yDist;
            xStep = Math.min(speed, xDist); // I want to move on "step" distance or less if I am very close
            yStep = Math.min(speed, yDist) / k;
        } else {
            k = yDist / xDist;
            xStep = Math.min(speed, xDist) / k;
            yStep = Math.min(speed, yDist);
        }

        // Add step value we just calculated
        if (posVector.x > targetPos.x) {
            posVector.x -= xStep;
        } else {
            posVector.x += xStep;
        }

        if (posVector.y > targetPos.y) {
            posVector.y -= yStep;
        } else {
            posVector.y += yStep;
        }
    }

    public boolean isReachedTarget() {
        return isReachedTarget;
    }

    public void draw() {
        applet.image(image, posVector.x - size / 2f, posVector.y - size / 2f, size, size);
    }
}
