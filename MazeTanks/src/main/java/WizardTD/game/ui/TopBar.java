package WizardTD.game.ui;

import WizardTD.App;
import WizardTD.data.Rectangle;
import WizardTD.ui.Component;
import WizardTD.ui.Label;
import WizardTD.ui.ProgressBar;
import processing.core.PApplet;

/**
 * Component, that draws the top bar of the game
 */
public class TopBar extends Component {
    private final Label titleLabel;
    private final Label manaLabel;
    private final ProgressBar manaBar;

    public TopBar(PApplet applet, Rectangle rect) {
        super(applet, rect);
        backgroundColor = applet.color(132, 115, 74); // brown

        float textSize = rect.height / 1.6f;
        int textColor = applet.color(0); // black
        int titleY = (int) (textSize / 5);
        //int green = applet.color(0, 100, 0); ---> change colour of waves starts ..
        titleLabel = new Label(applet, "", (int) textSize, titleY, textSize, textColor);

        int manaBarBorderSize = Math.round(2 * App.UI_SCALE);
        int manaBarX = (int) (rect.width / 2f);
        int manaBarY = rect.height / 4 + manaBarBorderSize;
        int manaBarWidth = Math.round(rect.width / 2.2f);
        int manaBarHeight = (int) (rect.height / 2f);
        int manaColor = applet.color(0, 214, 214);

        manaBar = new ProgressBar(applet,
                new Rectangle(manaBarX, manaBarY, manaBarWidth, manaBarHeight),
                applet.color(255, 255, 255), textColor, manaBarBorderSize, manaColor, textColor);

        int manaLabelX = manaBar.rect.x - manaBar.rect.width / 4;
        //int red = applet.color(100,0,0); --> change colour of MANA
        manaLabel = new Label(applet, "MANA:", manaLabelX, titleY, textSize, textColor);

    }

    public void setMana(int current, int max) {
        manaBar.setProgress(current, max);
    }

    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    @Override
    public void draw() {
        super.draw();

        titleLabel.draw();
        manaLabel.draw();
        manaBar.draw();
    }
}
