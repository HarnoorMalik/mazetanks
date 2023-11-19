package WizardTD.game.board.layer;

import WizardTD.game.board.Board;

public abstract class BoardLayer {
    protected final Board board;

    public BoardLayer(Board board) {
        this.board = board;
    }

    public void create() {

    }

    public void update(long deltaMs) {

    }

    public void draw() {

    }
}
