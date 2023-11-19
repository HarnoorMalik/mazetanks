package WizardTD.game;

import WizardTD.App;
import WizardTD.Resources;
import WizardTD.data.Rectangle;
import WizardTD.game.board.Board;
import WizardTD.game.logic.GameController;
import WizardTD.game.ui.SideBar;
import WizardTD.game.ui.TopBar;
import WizardTD.ui.Group;
import processing.core.PApplet;

/**
 * Component, that draws the screen of the application
 * Consists of top bar, sidebar, game board
 */
public class GameScreen extends Group {
    public final TopBar topBar;
    public final SideBar sideBar;
    public final Board board;

    public GameScreen(PApplet applet, Resources resources) {
        super(applet, new Rectangle(0, 0, App.SCREEN_WIDTH, App.SCREEN_HEIGHT));

        topBar = new TopBar(applet, new Rectangle(0, 0, App.SCREEN_WIDTH, App.TOPBAR_HEIGHT));
        sideBar = new SideBar(applet, new Rectangle(App.BOARD_SIZE, App.TOPBAR_HEIGHT, App.SIDEBAR_WIDTH, App.SCREEN_HEIGHT));
        board = new Board(applet, new Rectangle(0, App.TOPBAR_HEIGHT, App.BOARD_SIZE, App.BOARD_SIZE), resources);

        addChild(board);
        addChild(topBar);
        addChild(sideBar);
        addChild(new GameController(this, resources));
    }
}
