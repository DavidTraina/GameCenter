package traina.david.minesweepergame;

import android.content.Context;

import traina.david.gamecenter.Controller;
import traina.david.gamecenter.Game;

public class GridController extends Controller {

    /**
     * the current minesweeper game for the controller
     */
    private MinesweeperGame minesweeperGame;


    /**
     * a method to calculate a tap's location on the grid then have the minesweeper game
     * take the correct action
     * @param context the current app context
     * @param position the position of the tap in GridView
     */
    public void processTapMovement(Context context, int position) {
        int row = position / minesweeperGame.getGrid().getGridSize();
        int col = position % minesweeperGame.getGrid().getGridSize();
        minesweeperGame.processClick(context, col, row);
    }

    /**
     * a method to calculate a long-tap's location on the grid then have the minesweeper game
     * take the correct action
     * @param context the current app context
     * @param position the position of the tap in GridView
     */
    public void processLongTap(Context context, int position){
        int row = position / minesweeperGame.getGrid().getGridSize();
        int col = position % minesweeperGame.getGrid().getGridSize();
        minesweeperGame.processLongClick(context, col, row);
    }

    /**
     * A setter for minesweeperGame
     * @param game the current game
     */
    public void setGame(Game game){
        this.minesweeperGame = (MinesweeperGame) game;
    }
}
