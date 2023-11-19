package WizardTD.ui;

import WizardTD.data.Rectangle;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * Basic thing, that represents an independent thing in the game.
 * Components draw themselves, and handle user input.
 * <p>
 * Component is drawn by calls to PApplet.
 * Component size and position is stored in a Box object.
 * Component draws its background with backgroundColor property
 */
public abstract class Component {

    // Use it when a component requires a color, but you don't need it
    public static final int NO_COLOR = 0;
    public final Rectangle rect;
    public final PApplet applet;
    public int backgroundColor = NO_COLOR;

    public Component(PApplet applet, Rectangle rect) {
        this.applet = applet;
        this.rect = rect;
    }

    /**
     * Here component should perform calculations (e.g. size, position, animation, etc)
     *
     * @param deltaMs - amount of milliseconds passed since last update
     */
    public void update(long deltaMs) {

    }

    public void draw() {
        if (backgroundColor != NO_COLOR) {
            drawBackground();
        }
    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    // draws full-size rectangle filled with backgroundColor
    private void drawBackground() {
        applet.noStroke();
        applet.fill(backgroundColor);
        applet.rect(rect.x, rect.y, rect.width, rect.height);
    }


}
