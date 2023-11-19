package WizardTD.game.board.layer;

import WizardTD.App;
import WizardTD.data.Point;
import WizardTD.game.board.Board;
import processing.core.PImage;

/**
 * Draws fireballs on the Board
 */
public class BackgroundLayer extends BoardLayer {
    public BackgroundLayer(Board board) {
        super(board);
    }

    public void draw() {
        for (Point point : board.gameMap.grassPoints) {
            drawImage(point, board.resources.grassImage);
        }

        for (Point point : board.gameMap.shrubPoints) {
            drawImage(point, board.resources.shrubImage);
        }
    }

    private void drawImage(Point point, PImage image) {
        Point screenPoint = board.boardPointToScreenPoint(point);
        board.applet.image(image, screenPoint.x, screenPoint.y, App.CELL_SIZE, App.CELL_SIZE);
    }
}
