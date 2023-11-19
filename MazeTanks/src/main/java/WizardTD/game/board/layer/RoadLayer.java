package WizardTD.game.board.layer;

import WizardTD.App;
import WizardTD.data.Point;
import WizardTD.game.board.Board;
import processing.core.PImage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Draws roads on the Board
 */
public class RoadLayer extends BoardLayer {
    private List<PImage> images;

    public RoadLayer(Board board) {
        super(board);
    }

    @Override
    public void create() {
        images = board.gameMap.roadPoints.stream()
                .map(p -> getImageForRoad(p.y, p.x))
                .collect(Collectors.toList());
    }

    public void draw() {
        for (int i = 0; i < board.gameMap.roadPoints.size(); i++) {
            PImage image = images.get(i);
            Point point = board.gameMap.roadPoints.get(i);
            Point screenPoint = board.boardPointToScreenPoint(point);

            board.applet.image(image, screenPoint.x, screenPoint.y, App.CELL_SIZE, App.CELL_SIZE);
        }
    }

    private PImage getImageForRoad(int i, int j) {
        String info = board.gameMap.pathInfo.cellConnectionTable[i][j];
        if (info.equals("0")) {
            throw new RuntimeException("Invalid cell connection info for cell: " + i + ", " + j);
        }

        // 1 direction
        if (info.equals("b") || info.equals("t") || info.equals("tb")) {
            return board.resources.pathTopBottomImage;
        }
        if (info.equals("l") || info.equals("r") || info.equals("lr")) {
            return board.resources.pathLeftRightImage;
        }

        // 2 directions
        if (info.equals("tl")) {
            return board.resources.pathTopLeftImage;
        }
        if (info.equals("tr")) {
            return board.resources.pathTopRightImage;
        }
        if (info.equals("bl")) {
            return board.resources.pathBottomLeftImage;
        }
        if (info.equals("br")) {
            return board.resources.pathBottomRightImage;
        }

        // 3 directions
        if (info.equals("tlr")) {
            return board.resources.pathTopLeftRight;
        }
        if (info.equals("tbl")) {
            return board.resources.pathTopBottomLeft;
        }
        if (info.equals("blr")) {
            return board.resources.pathBottomLeftRight;
        }

        // 4 directions
        if (info.equals("tblr")) {
            return board.resources.pathTopBottomLeftRightImage;
        }

        throw new RuntimeException("Invalid road placement: " + info);
    }

}
