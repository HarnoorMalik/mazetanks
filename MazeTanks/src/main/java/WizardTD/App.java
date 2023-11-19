package WizardTD;

import WizardTD.game.GameScreen;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class App extends PApplet {

    // Multiplies game size by this value. Useful on high-res displays.
    // When it is set to 1f, the game is in its original size
    public static final float UI_SCALE = 1f; // PROBLEM - if I make it bigger size, then fps decreases 30 fps (60fps)

    // Multiplies movement speed of objects (enemies and projectiles)
    // When it is set to 1f, game objects move in their original speed
    //
    // When you increase UI_SCALE, it's necessary also increase SPEED_SCALE
    // For example:
    // with UI_SCALE=1.5f,  SPEED_SCALE=3f is good
    // with UI_SCALE=1.75f, SPEED_SCALE=5f is good
    // with UI_SCALE=2f,    SPEED_SCALE=7f is good
    public static final float OBJECT_SPEED_SCALE = 2f; //affects speed of monster, fireballs. waves timer not affected.

    public static final int CELL_SIZE = Math.round(32 * UI_SCALE);
    public static final int SIDEBAR_WIDTH = Math.round(120 * UI_SCALE);
    public static final int TOPBAR_HEIGHT = Math.round(40 * UI_SCALE);
    public static final int BOARD_CELLS = 20;

    public static final int BOARD_SIZE = CELL_SIZE * BOARD_CELLS; // game field size in pixels

    public static int SCREEN_WIDTH = BOARD_SIZE + SIDEBAR_WIDTH; // total width of the game screen in pixel
    public static int SCREEN_HEIGHT = BOARD_SIZE + TOPBAR_HEIGHT; // total height of the game screen in pixel

    public static final int FPS = 60; //original frame per seconds

    public String configPath;

    public Resources resources; //g

    public GameScreen gameScreen;

    // used to calculate actual FPS in console
    private int actualFrames = 0;
    private long measurementStartedAt;

    // Feel free to add any additional methods or attributes you want. Please put classes in different files.

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
    @Override
    public void settings() {
        size(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */
    @Override
    public void setup() {
        frameRate(FPS);

        resources = new Resources(this);
        gameScreen = new GameScreen(this, resources);
    }

    /**
     * Receive key pressed signal from the keyboard.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        gameScreen.keyPressed(e);
    }

    /**
     * Receive key released signal from the keyboard.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        gameScreen.keyReleased(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        gameScreen.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gameScreen.mouseReleased(e);
    }


//    public void mouseDragged(MouseEvent e) {
//
//    }

    /**
     * Draw all elements in the game by current frame.
     */
    @Override
    public void draw() {
        actualFrames++;
        long now = System.currentTimeMillis();
        long diffMs = now - measurementStartedAt;
        if (diffMs >= 1000) {
            System.out.printf("Per %d ms, frames = %d\n", diffMs, actualFrames);
            actualFrames = 0;
            measurementStartedAt = now;
        }
        gameScreen.draw();
    }

    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }


}
