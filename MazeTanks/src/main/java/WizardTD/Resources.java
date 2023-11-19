package WizardTD;

import WizardTD.data.config.Config;
import WizardTD.util.PImageUtils;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Loads and stores all the resources
 */
public class Resources {
    public final List<PImage> towerImages;
    public final PImage fireballImage;
    public final PImage grassImage;
    public final PImage shrubImage;

    public final Map<String, PImage> monsterImages;
    public final List<PImage> deathAnimation;

    public final PImage pathLeftRightImage;
    public final PImage pathBottomLeftImage;
    public final PImage pathBottomRightImage;

    public final PImage pathTopBottomImage;
    public final PImage pathBottomLeftRight;
    public final PImage pathTopLeftImage;
    public final PImage pathTopRightImage;

    public final PImage pathTopLeftRight;
    public final PImage pathTopBottomLeft;
    public final PImage pathTopBottomRight;
    public final PImage pathTopBottomLeftRightImage;

    public final PImage wizardHouseLeftImage;
    public final PImage wizardHouseRightImage;
    public final PImage wizardHouseTopImage;
    public final PImage wizardHouseBottomImage;

    public final Config config;
    private final PApplet applet;
    public final String[] level;

    Resources(PApplet applet) {
        this.applet = applet;

        // loading config
        config = new Config(applet.loadJSONObject("config.json"));

        // loading current depending on config value
        level = applet.loadStrings(config.layout);

        // loading images
        towerImages = Stream.of(0, 1, 2)
                .map(n -> loadImage("tower" + n))
                .collect(Collectors.toList());

        fireballImage = loadImage("fireball");
        grassImage = loadImage("grass");
        shrubImage = loadImage("shrub");

        // builds map from string to image like:
        // [gremlin = PImage("gremlin.png"), worm = PImage(worm.png),...]
        monsterImages = Stream.of("gremlin", "beetle", "worm")
                .map(s -> new AbstractMap.SimpleEntry<>(s, loadImage(s)))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        deathAnimation = Stream.of(1, 2, 3, 4, 5)
                .map(n -> loadImage("gremlin" + n))
                .collect(Collectors.toList());

        pathLeftRightImage = loadImage("path0");
        pathTopBottomImage = PImageUtils.rotateImageByDegrees(applet, pathLeftRightImage, 90);

        pathBottomLeftImage = loadImage("path1");
        pathBottomRightImage = PImageUtils.rotateImageByDegrees(applet, pathBottomLeftImage, 270);
        pathTopLeftImage = PImageUtils.rotateImageByDegrees(applet, pathBottomLeftImage, 90);
        pathTopRightImage = PImageUtils.rotateImageByDegrees(applet, pathBottomLeftImage, 180);

        pathBottomLeftRight = loadImage("path2");
        pathTopLeftRight = PImageUtils.rotateImageByDegrees(applet, pathBottomLeftRight, 180);
        pathTopBottomLeft = PImageUtils.rotateImageByDegrees(applet, pathBottomLeftRight, 90);
        pathTopBottomRight = PImageUtils.rotateImageByDegrees(applet, pathBottomLeftRight, 270);

        pathTopBottomLeftRightImage = loadImage("path3");

        wizardHouseRightImage = loadImage("wizard_house");
        wizardHouseLeftImage = PImageUtils.rotateImageByDegrees(applet, wizardHouseRightImage, 180);
        wizardHouseTopImage = PImageUtils.rotateImageByDegrees(applet, wizardHouseRightImage, 270);
        wizardHouseBottomImage = PImageUtils.rotateImageByDegrees(applet, wizardHouseRightImage, 90);
    }

    private PImage loadImage(String name) {
        return applet.loadImage("src/main/resources/WizardTD/" + name + ".png");
    }
}
