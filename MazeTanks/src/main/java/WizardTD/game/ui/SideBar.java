package WizardTD.game.ui;

import WizardTD.App;
import WizardTD.data.Rectangle;
import WizardTD.ui.Group;
import WizardTD.ui.Label;
import WizardTD.ui.MultilineTooltip;
import WizardTD.ui.SquareButton;
import processing.core.PApplet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Component, that draws the sidebar of the game
 */
public class SideBar extends Group implements SquareButton.ClickListener {
    private final int hoverColor;
    private final int selectedColor;
    private final int mainColor;

    public SquareButton.ClickListener onButtonClickListener;

    public static String BUTTON_FAST_FORWARD_TEXT = "FF";
    public static String BUTTON_PAUSE_TEXT = "P";
    public static String BUTTON_TOWER_TEXT = "T";
    public static String BUTTON_UPGRADE_RANGE_TEXT = "U1";
    public static String BUTTON_UPGRADE_SPEED_TEXT = "U2";
    public static String BUTTON_UPGRADE_DAMAGE_TEXT = "U3";
    public static String BUTTON_MANA_POOL_TEXT = "M";
    private static final String BUTTON_WITH_TOOLTIP = "+tooltip";
    private final String[][] items = new String[][]{
            {BUTTON_FAST_FORWARD_TEXT, "2x speed"},
            {BUTTON_PAUSE_TEXT, "PAUSE"},
            {BUTTON_TOWER_TEXT, "Build", "tower", BUTTON_WITH_TOOLTIP},
            {BUTTON_UPGRADE_RANGE_TEXT, "Upgrade", "range"},
            {BUTTON_UPGRADE_SPEED_TEXT, "Upgrade", "speed"},
            {BUTTON_UPGRADE_DAMAGE_TEXT, "Upgrade", "damage"},
            {BUTTON_MANA_POOL_TEXT, "Mana pool", "cost: ?", BUTTON_WITH_TOOLTIP},
    };

    private final Map<String, SquareButton> buttons = new HashMap<>();

    private Label manaPoolLabel;

    private MultilineTooltip towerUpgradeTooltip;

    public SideBar(PApplet applet, Rectangle rect) {
        super(applet, rect);
        backgroundColor = applet.color(132, 115, 74); // brown
        hoverColor = applet.color(206, 201, 211); // gray
        selectedColor = applet.color(255, 255, 8); // yellow
        mainColor = applet.color(0, 0, 0); // black

        setupButtons();
        setupUpgradeTooltip();
    }

    public void setManaPoolUpgradeCost(int cost) {
        manaPoolLabel.setText("cost: " + cost);
        buttons.get(BUTTON_MANA_POOL_TEXT).setTooltipText("Cost: " + cost);
    }

    public void setTowerCost(int cost) {
        buttons.get(BUTTON_TOWER_TEXT).setTooltipText("Cost: " + cost);
    }

    public void setTowerUpgradeTooltip(List<String> lines) {
        towerUpgradeTooltip.setLines(lines);
    }

    public void setButtonSelected(String text, boolean isSelected) {
        buttons.get(text).isSelected = isSelected;
    }

    public boolean isButtonSelected(String text) {
        return buttons.get(text).isSelected;
    }

    public void toggleButtonSelected(String text) {
        SquareButton button = buttons.get(text);
        button.isSelected = !button.isSelected;
    }

    private void setupButtons() {
        float borderSize = 2f * App.UI_SCALE;
        int padding = Math.round(10f * App.UI_SCALE);
        int size = Math.round(App.CELL_SIZE * 1.2f);
        float textSize = 24f * App.UI_SCALE;
        float smallTextSize = 13f * App.UI_SCALE;
        int smallPadding = Math.round(4f * App.UI_SCALE);

        int yOffset = 0;
        for (String[] item : items) {
            int buttonY = rect.y + padding + yOffset;
            Rectangle buttonRect = new Rectangle(rect.x + padding, buttonY, size, size);
            boolean hasTooltip = item.length > 3 && item[3].equals(BUTTON_WITH_TOOLTIP);

            SquareButton button = new SquareButton(applet, buttonRect,
                    item[0], textSize, mainColor,
                    hoverColor, selectedColor, mainColor, borderSize, hasTooltip);
            button.clickListener = this;
            addChild(button);
            buttons.put(item[0], button);

            int labelX = Math.round(rect.x + padding + size + smallPadding);
            Label label = new Label(applet, item[1], labelX, buttonY, smallTextSize, mainColor);
            addChild(label);

            if (item.length > 2) {
                int bottomLabelY = buttonY + size / 2;
                Label bottomLabel = new Label(applet, item[2], labelX, bottomLabelY, smallTextSize, mainColor);
                addChild(bottomLabel);

                if (item[0].equals(BUTTON_MANA_POOL_TEXT)) {
                    manaPoolLabel = bottomLabel;
                }
            }

            yOffset += size + padding;
        }
    }

    private void setupUpgradeTooltip() {
        int padding = Math.round(10 * App.UI_SCALE);
        int x = rect.x + padding;
        int y = Math.round(rect.y + rect.height * 0.7f);
        int w = Math.round(rect.width * 0.8f);
        towerUpgradeTooltip = new MultilineTooltip(applet, new Rectangle(x, y, w, 0));
        addChild(towerUpgradeTooltip);
    }

    @Override
    public void onClicked(SquareButton button) {
        if (onButtonClickListener != null) {
            onButtonClickListener.onClicked(button);
        }
    }

    public void unselectAllButtons() {
        buttons.values().forEach(b -> b.isSelected = false);
    }
}
