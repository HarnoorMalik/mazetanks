package WizardTD.ui;

import WizardTD.App;
import WizardTD.data.Rectangle;
import processing.core.PApplet;

import java.util.List;

public class MultilineTooltip extends Component {
    private List<String> costList;

    private final float textSize = 13f * App.UI_SCALE;
    private final float padding = 3f * App.UI_SCALE;

    private final float lineHeight = textSize + padding * 3;
    private final float strokeWeight = App.UI_SCALE;
    private final int mainColor;

    public MultilineTooltip(PApplet applet, Rectangle rect) {
        super(applet, rect);
        backgroundColor = applet.color(255, 255, 255);
        mainColor = applet.color(0, 0, 0);
    }

    // lines = {"header", "line 1", "line 2", "footer"}
    public void setLines(List<String> lines) {
        this.costList = lines;
        if (lines != null) {
            rect.height = Math.round(costList.size() * lineHeight);
        }
    }

    @Override
    public void draw() {
        if (costList == null || costList.size() <= 2) return;
        super.draw();
        drawTooltip();
    }

    private void drawTooltip() {
        drawBackground();

        drawText(0, costList.get(0));
        drawDivider(Math.round(lineHeight));

        int i;
        for (i = 1; i < costList.size() - 1; i++) {
            String line = costList.get(i);
            float offsetY = i * lineHeight;
            drawText(Math.round(offsetY), line);
        }

        drawDivider(Math.round(lineHeight * i));
        drawText(Math.round(lineHeight * i), costList.get(costList.size() - 1));
    }

    private void drawBackground() {
        applet.noFill();
        applet.strokeWeight(strokeWeight);
        applet.stroke(mainColor);
        applet.rect(rect.x, rect.y, rect.width, rect.height);
    }

    private void drawText(int offsetY, String text) {
        applet.fill(mainColor);
        applet.textSize(textSize);
        int x = rect.x + Math.round(padding);
        int y = rect.y + offsetY + Math.round(textSize + padding);
        applet.text(text, x, y);
    }

    private void drawDivider(int offsetY) {
        applet.noFill();
        applet.strokeWeight(strokeWeight);
        applet.stroke(mainColor);

        int x = rect.x;
        int x1 = rect.x + rect.width;
        int y = rect.y + offsetY;

        applet.line(x, y, x1, y);
    }
}
