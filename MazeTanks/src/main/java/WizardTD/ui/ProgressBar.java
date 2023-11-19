package WizardTD.ui;

import WizardTD.data.Rectangle;
import WizardTD.util.Utils;
import processing.core.PApplet;

/**
 * Shows the progress percent within a rectangle
 */
public class ProgressBar extends Component {
    private final Box backgroundBox;
    private final Box progressBox;
    private final Label label;


    public ProgressBar(PApplet applet, Rectangle rect, int backgroundColor, int borderColor, int borderSize,
                       int progressColor, int textColor) {
        super(applet, rect);

        backgroundBox = new Box(applet, rect.copy(), backgroundColor, borderColor, borderSize);

        progressBox = new Box(applet, rect.copy(), progressColor, borderColor, borderSize);

        float textSize = rect.height / 1.2f;
        int textX = rect.x + rect.width / 2 - (int) (textSize * 2);
        label = new Label(applet, "0 / 0", textX, rect.y, textSize, textColor);

        setProgress(0, 1);
    }

    /**
     * Sets text "current / max" and fills the corresponding percent of the bar
     */
    public void setProgress(int current, int max) {
        label.setText(current + " / " + max);
        float multiplier = Utils.progressToMultiplier(current, max);
        progressBox.rect.width = Math.round(rect.width * multiplier);
    }

    @Override
    public void draw() {
        super.draw();
        backgroundBox.draw();
        progressBox.draw();
        label.draw();
    }
}
