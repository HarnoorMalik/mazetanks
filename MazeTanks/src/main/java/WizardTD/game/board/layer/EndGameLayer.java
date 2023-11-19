package WizardTD.game.board.layer;

import WizardTD.App;
import WizardTD.data.Point;
import WizardTD.game.board.Board;
import WizardTD.ui.Label;

public class EndGameLayer extends BoardLayer {

    private final Label youLostLabel;
    private final Label youWinLabel;
    private final Label pressRLabel;


    public EndGameLayer(Board board) {
        super(board);

        Point lostTitlePos = board.boardPointToScreenPoint(new Point(8, 6));
        youLostLabel = new Label(board.applet, "YOU LOST", lostTitlePos.x, lostTitlePos.y,
                48f * App.UI_SCALE, board.applet.color(204, 0, 0));

        Point pressRPos = board.boardPointToScreenPoint(new Point(8, 10));
        pressRLabel = new Label(board.applet, "Press 'r' to restart", pressRPos.x, pressRPos.y,
                24f * App.UI_SCALE, board.applet.color(204, 0, 0));

        youWinLabel = new Label(board.applet, "YOU WIN", lostTitlePos.x, lostTitlePos.y,
                48f * App.UI_SCALE, board.applet.color(206, 20, 190));
    }

    @Override
    public void draw() {
        if (board.gameStatus == Board.GameStatus.LOST) {
            youLostLabel.draw();
            pressRLabel.draw();
        } else if (board.gameStatus == Board.GameStatus.WON) {
            youWinLabel.draw();
        }
    }
}
