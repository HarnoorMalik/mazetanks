package WizardTD.game.board.object;

import WizardTD.data.Point;
import WizardTD.data.RectangleF;
import WizardTD.util.Utils;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class Monster {
    private final PApplet applet;
    public final RectangleF pos;
    private final int size;
    private PImage image;
    private final List<PImage> deathAnimation;
    private final Queue<Point> pxPathQueue;
    private final float speed;
    private final int maxHealth;
    public int health;
    public final int manaGainedOnKill;
    private final float armour;
    private Point currentTarget;
    public Status status = Status.ALIVE;

    private static final int ANIMATION_FRAME_DURATION = 4;
    private int animationFrameElapsedMs;
    private int currentAnimationFrame;

    public enum Status {
        ALIVE, DYING, DEAD, ARRIVED
    }

    public Monster(PApplet applet, int size, PImage image, List<PImage> deathAnimation, List<Point> screenPointPath,
                   float speed, int maxHealth, int manaGainedOnKill, float armour) {
        this.applet = applet;
        this.size = size;
        this.image = image;
        this.deathAnimation = deathAnimation;
        pxPathQueue = new ArrayDeque<>(screenPointPath);
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.manaGainedOnKill = manaGainedOnKill;
        this.armour = armour;

        assert pxPathQueue.size() >= 1; // We need to have at least one starting point;
        Point initialPoint = pxPathQueue.peek();


        pos = new RectangleF(initialPoint.x, initialPoint.y, size, size);
    }

    public void update() {
        if (status == Status.ALIVE) {
            updatePosition();
        } else if (status == Status.DYING) {
            updateDeathAnimation();
        }
    }

    public void draw() {
        drawMonster();

        if (status == Status.ALIVE) {
            drawHpBar();
        }
    }

    public void receiveDamage(int damage) {
        if (status != Status.ALIVE) return;

        // armour: percentage multiplier to damage received by this monster
        health -= Math.round(damage * armour);
        if (health < damage) {
            status = Status.DYING;
            image = deathAnimation.get(currentAnimationFrame);
        }
    }

    private void drawMonster() {
        float imageSize = size / 2f;
        float x = pos.x + imageSize / 2;
        float y = pos.y + imageSize / 2;

        applet.image(image, x, y, imageSize, imageSize);
    }

    private void drawHpBar() {
        float hpBarHeight = 4f;

        applet.noStroke();
        applet.fill(applet.color(204, 0, 0)); // red
        applet.rect(pos.x, pos.y, pos.width, hpBarHeight);

        float mult = Utils.progressToMultiplier(health, maxHealth);
        float hpWidth = pos.width * mult;

        applet.fill(applet.color(5, 126, 53)); // green
        applet.rect(pos.x, pos.y, hpWidth, hpBarHeight);
    }

    private void updatePosition() {
        // when reached the current cell, take next cell coordinates
        if (currentTarget == null) {
            if (pxPathQueue.isEmpty()) {
                status = Status.ARRIVED;
                return;
            }

            currentTarget = pxPathQueue.poll();
            assert currentTarget != null; // because of isArrived() check above
        }

        // move left or right in the target cell direction
        if (currentTarget.x > pos.x) {
            pos.x += Math.min(speed, currentTarget.x - pos.x);
        } else if (currentTarget.x < pos.x) {
            pos.x -= Math.min(speed, pos.x - currentTarget.x);
        }

        // move top or bottom in the target cell direction
        if (currentTarget.y > pos.y) {
            pos.y += Math.min(speed, currentTarget.y - pos.y);
        } else if (currentTarget.y < pos.y) {
            pos.y -= Math.min(speed, pos.y - currentTarget.y);
        }

        // check if we've reached the target
        if (pos.x == currentTarget.x && pos.y == currentTarget.y) {
            currentTarget = null;
        }
    }

    private void updateDeathAnimation() {
        if (status == Status.DYING) {
            animationFrameElapsedMs++;
        }
        if (animationFrameElapsedMs <= ANIMATION_FRAME_DURATION) {
            return;
        }
        currentAnimationFrame++;
        animationFrameElapsedMs = 0;

        if (currentAnimationFrame >= deathAnimation.size()) {
            status = Status.DEAD;
            return;
        }

        image = deathAnimation.get(currentAnimationFrame);
    }
}
