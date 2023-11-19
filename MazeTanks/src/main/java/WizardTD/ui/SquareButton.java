package WizardTD.ui;

import WizardTD.App;
import WizardTD.data.Point;
import WizardTD.data.Rectangle;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class SquareButton extends Component {
    private final int hoverColor;
    private final int selectedColor;
    public ClickListener clickListener;

    public boolean isHovered = false;
    public boolean isSelected = false;

    private final Box box;
    private final Label label;

    private Tooltip tooltip;

    public SquareButton(PApplet applet, Rectangle rect, String text, float textSize, int textColor, int hoverColor,
                        int selectedColor, int borderColor, float borderSize, boolean hasTooltip) {
        super(applet, rect);
        this.hoverColor = hoverColor;
        this.selectedColor = selectedColor;

        box = new Box(applet, rect, getBackgroundColor(), borderColor, borderSize);

        int labelOffset = Math.round(rect.width / 12f);
        label = new Label(applet, text, rect.x + labelOffset, rect.y + labelOffset, textSize, textColor);

        if (hasTooltip) {
            setupTooltip();
        }
    }

    private void setupTooltip() {
        int width = Math.round(100 * App.UI_SCALE);
        float textSize = 16 * App.UI_SCALE;
        float borderSize = App.UI_SCALE;
        int margin = Math.round(20 * App.UI_SCALE);

        int x = rect.x - width - margin;
        int y = rect.y;
        int height = Math.round(textSize + borderSize * 3f);

        Rectangle tooltipRect = new Rectangle(x, y, width, height);
        tooltip = new Tooltip(applet, tooltipRect, textSize, borderSize);
    }

    @Override
    public void update(long deltaMs) {
        isHovered = rect.contains(applet.mouseX, applet.mouseY);
        box.backgroundColor = getBackgroundColor();
    }

    @Override
    public void draw() {
        super.draw();

        box.draw();
        label.draw();

        if (tooltip != null && isHovered) {
            tooltip.draw();
        }
    }

    public String getText() {
        return label.getText();
    }

    private int getBackgroundColor() {
        if (isSelected) return selectedColor;
        if (isHovered) return hoverColor;
        return NO_COLOR;
    }

    public void setTooltipText(String text) {
        if (tooltip != null) {
            tooltip.setText(text);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = new Point(e.getX(), e.getY());
        if (rect.contains(point) && clickListener != null) {
            clickListener.onClicked(this);
        }
    }

    public interface ClickListener {
        void onClicked(SquareButton button);
    }
}
