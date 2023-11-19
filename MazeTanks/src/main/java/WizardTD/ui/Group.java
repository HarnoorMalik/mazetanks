package WizardTD.ui;

import WizardTD.data.Rectangle;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Game component, that consists of children components
 */
public class Group extends Component {
    private final List<Component> children = new ArrayList<>();

    public Group(PApplet applet, Rectangle rect) {
        super(applet, rect);
    }

    public void addChild(Component component) {
        children.add(component);
    }

    // If true, every child of this group will receive an input event
    public boolean forwardInputEvents = true;

    public boolean visible = true;

    @Override
    public void update(long deltaMs) {
        children.forEach(c -> c.update(deltaMs));
    }

    @Override
    public void draw() {
        if (!visible) return;

        super.draw();
        children.forEach(Component::draw);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (forwardInputEvents) {
            children.forEach(c -> c.keyPressed(e));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (forwardInputEvents) {
            children.forEach(c -> c.keyReleased(e));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (forwardInputEvents) {
            children.forEach(c -> c.mousePressed(e));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (forwardInputEvents) {
            children.forEach(c -> c.mouseReleased(e));
        }
    }
}
