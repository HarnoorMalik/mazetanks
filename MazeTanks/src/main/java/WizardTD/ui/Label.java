package WizardTD.ui;

import WizardTD.data.Rectangle;
import processing.core.PApplet;

/**
 * Draws text
 */
public class Label extends Component {
    private String text;
    private final float textSize;
    private final int textColor;

    public Label(PApplet applet, String text, int x, int y, float textSize, int textColor) {
        super(applet, new Rectangle(x, y, 0, 0));
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw() {
        super.draw();
        applet.fill(textColor);
        applet.textSize(textSize);
        applet.text(text, rect.x, rect.y + textSize);
    }
}
