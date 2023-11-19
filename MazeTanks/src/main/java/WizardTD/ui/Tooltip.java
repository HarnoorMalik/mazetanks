package WizardTD.ui;

import WizardTD.App;
import WizardTD.data.Rectangle;
import processing.core.PApplet;

public class Tooltip extends Group {
    private final Label label;

    public Tooltip(PApplet applet, Rectangle rect, float textSize, float borderSize) {
        super(applet, rect);

        int background = applet.color(255, 255, 255); // white
        int mainColor = applet.color(0, 0, 0); // black

        addChild(new Box(applet, rect, background, mainColor, borderSize));

        int padding = Math.round(4f * App.UI_SCALE);
        label = new Label(applet, "", rect.x + padding, rect.y, textSize, mainColor);
        addChild(label);
    }

    public void setText(String text) {
        label.setText(text);
    }

}
