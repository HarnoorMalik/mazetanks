package WizardTD.ui;

import WizardTD.data.Rectangle;
import processing.core.PApplet;

// Draws "box" (square) with the specified colors
public class Box extends Component {
    private final int borderColor;
    private final float borderSize;

    public Box(PApplet applet, Rectangle rect, int backgroundColor, int borderColor, float borderSize) {
        super(applet, rect);
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.borderSize = borderSize;
    }

    @Override
    public void draw() {
        super.draw();

        if (borderColor != NO_COLOR) {
            applet.stroke(borderColor);
            applet.strokeWeight(borderSize);
        } else {
            applet.noStroke();
        }

        applet.noFill();
        applet.rect(rect.x, rect.y, rect.width, rect.height);
    }
}
