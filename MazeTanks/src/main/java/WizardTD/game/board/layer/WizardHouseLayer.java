package WizardTD.game.board.layer;

import WizardTD.App;
import WizardTD.data.Point;
import WizardTD.game.board.Board;
import processing.core.PImage;

/**
 * Draws wizard house
 */
public class WizardHouseLayer extends BoardLayer {
    private PImage image;
    private Point screenPoint;

    public WizardHouseLayer(Board board) {
        super(board);
    }

    @Override
    public void create() {
        image = getImageForWizardHouse(board.gameMap.wizardHousePoint.y, board.gameMap.wizardHousePoint.x);
        screenPoint = board.boardPointToScreenPoint(board.gameMap.wizardHousePoint);
    }

    public void draw() {
        // background grass
        board.applet.image(board.resources.grassImage, screenPoint.x, screenPoint.y, App.CELL_SIZE, App.CELL_SIZE);

        float size = App.CELL_SIZE * 1.5f;

        float x = screenPoint.x - size / 8;
        float y = screenPoint.y - size / 8;
        board.applet.image(image, x, y, size, size);
    }

    private PImage getImageForWizardHouse(int i, int j) {
        String info = board.gameMap.pathInfo.cellConnectionTable[i][j];
        if (info.contains("t")) {
            return board.resources.wizardHouseTopImage;
        } else if (info.contains("b")) {
            return board.resources.wizardHouseBottomImage;

        } else if (info.contains("l")) {
            return board.resources.wizardHouseLeftImage;
        } else if (info.contains("r")) {
            return board.resources.wizardHouseRightImage;
        }

        throw new RuntimeException("Invalid wizard house placement: " + info);
    }
}
