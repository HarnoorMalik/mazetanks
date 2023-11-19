package WizardTD.game.board.object;

import WizardTD.App;
import WizardTD.data.Point;
import WizardTD.data.Rectangle;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Collections;
import java.util.List;

public class Tower {
    private final PApplet applet;
    private final List<PImage> images;
    public final Point boardPosition;
    public final Rectangle position;
    private int range;
    private float fireSpeed;
    private int damage;
    private int cooldownMs;

    private boolean isHovered;

    private int rangeUpgradeLevel;
    private int fireSpeedUpgradeLevel;
    private int damageUpgradeLevel;

    private int totalUpgradeLevel;

    private String rangeUpgradeString = "";
    private String damageUpgradeString = "";

    private final float textSize = App.CELL_SIZE / 3f;

    private static final int MAX_VISIBLE_LEVEL = 3;

    public Tower(PApplet applet, List<PImage> images, Point boardPosition, Point screenPosition,
                 int range, float fireSpeed, int damage) {
        this.applet = applet;
        this.images = images;
        this.boardPosition = boardPosition;
        this.position = new Rectangle(screenPosition.x, screenPosition.y, App.CELL_SIZE, App.CELL_SIZE);
        this.range = range;
        this.fireSpeed = fireSpeed;
        this.damage = damage;
    }

    public void update(long deltaMs) {
        cooldownMs -= deltaMs;
    }

    public void goOnCooldown() {
        cooldownMs = Math.round(1000 / fireSpeed);
    }

    public boolean isReadyToShoot() {
        return cooldownMs <= 0;
    }

    public int getRange() {
        return range;
    }

    public float getFireSpeed() {
        return fireSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public int getUpgradeCost(int level) {
        int cost = 20;
        for (int i = 0; i < level; i++) {
            cost += 10;
        }
        return cost;
    }

    public int getRangeUpgradeCost() {
        return getUpgradeCost(rangeUpgradeLevel);
    }

    public int getFireSpeedUpgradeCost() {
        return getUpgradeCost(fireSpeedUpgradeLevel);
    }

    public int getDamageUpgradeCost() {
        return getUpgradeCost(damageUpgradeLevel);
    }

    public void upgradeRange(int newValue) {
        System.out.printf("Upgrading range from %d to %d\n", range, newValue);

        range = newValue;
        rangeUpgradeLevel++;
        updateTotalUpgradeLevel();
    }

    public void upgradeFireSpeed(float newValue) {
        System.out.printf("Upgrading fire speed from %.2f to %.2f\n", fireSpeed, newValue);

        fireSpeed = newValue;
        fireSpeedUpgradeLevel++;
        updateTotalUpgradeLevel();
    }

    public void upgradeDamage(int newValue) {
        System.out.printf("Upgrading damage from %d to %d\n", damage, newValue);

        damage = newValue;
        damageUpgradeLevel++;
        updateTotalUpgradeLevel();
    }

    public void updateHovered() {
        boolean inXBounds = applet.mouseX >= position.x && applet.mouseX <= position.x + App.CELL_SIZE;
        boolean inYBounds = applet.mouseY >= position.y && applet.mouseY <= position.y + App.CELL_SIZE;
        isHovered = inXBounds && inYBounds;
    }

    public void draw() {
        applet.image(images.get(totalUpgradeLevel), position.x, position.y, App.CELL_SIZE, App.CELL_SIZE);
        drawRadius();
        drawUpgrades();

    }

    private void updateTotalUpgradeLevel() {
        if (rangeUpgradeLevel >= 2 && fireSpeedUpgradeLevel >= 2 && damageUpgradeLevel >= 2) {
            totalUpgradeLevel = 2;
        } else if (rangeUpgradeLevel >= 1 && fireSpeedUpgradeLevel >= 1 && damageUpgradeLevel >= 1) {
            totalUpgradeLevel = 1;
        } else {
            totalUpgradeLevel = 0;
        }


        int rangeUpgradeVisibleLevel = Math.min(rangeUpgradeLevel - totalUpgradeLevel, MAX_VISIBLE_LEVEL);
        if (rangeUpgradeVisibleLevel > 0) {
            rangeUpgradeString = String.join("", Collections.nCopies(rangeUpgradeVisibleLevel, "o"));
        }

        int damageUpgradeVisibleLevel = Math.min(damageUpgradeLevel - totalUpgradeLevel, MAX_VISIBLE_LEVEL);
        if (damageUpgradeVisibleLevel > 0) {
            damageUpgradeString = String.join("", Collections.nCopies(damageUpgradeVisibleLevel, "x"));
        }
    }

    private void drawRadius() {
        if (!isHovered) return;

        Point center = position.center();
        applet.stroke(applet.color(254, 250, 12));
        applet.strokeWeight(2 * App.UI_SCALE);
        applet.noFill();
        applet.ellipse(center.x, center.y, range * 2, range * 2);
    }

    private void drawUpgrades() {
        drawFireSpeedUpgrade();
        drawRangeUpgrade();
        drawDamageUpgrade();
    }

    private void drawFireSpeedUpgrade() {
        int level = fireSpeedUpgradeLevel - totalUpgradeLevel;
        if (level <= 0) return;
        if (level > 3) level = 3;

        float boardWeight = 1.2f * App.UI_SCALE;
        boardWeight *= level;

        float padding = App.CELL_SIZE / 6f;
        float size = App.CELL_SIZE - padding * 2;
        float x = position.x + padding;
        float y = position.y + padding;
        float r = padding;

        applet.noFill();
        applet.stroke(applet.color(129, 181, 254)); // blue
        applet.strokeWeight(boardWeight);
        applet.rect(x, y, size, size, r);
    }

    private void drawRangeUpgrade() {
        if (rangeUpgradeLevel - totalUpgradeLevel <= 0) return;

        float y = position.y + textSize / 2f;

        applet.fill(applet.color(247, 67, 245)); // magenta
        applet.textSize(textSize);
        applet.text(rangeUpgradeString, position.x, y);
    }

    private void drawDamageUpgrade() {
        if (damageUpgradeLevel - totalUpgradeLevel <= 0) return;

        float y = position.y + App.CELL_SIZE;

        applet.fill(applet.color(247, 67, 245)); // magenta
        applet.textSize(textSize);
        applet.text(damageUpgradeString, position.x, y);
    }
}
