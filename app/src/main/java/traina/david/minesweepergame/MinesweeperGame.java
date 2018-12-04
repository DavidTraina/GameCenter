package traina.david.minesweepergame;

import android.content.Context;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import traina.david.gamecenter.Game;
import traina.david.gamecenter.GlobalCenter;


/**
 * The data class for Minesweeper. Contains information such as grid and mine layout.
 */
public class MinesweeperGame extends Game implements Serializable {

    private final Grid grid;

    /**
     * An arbitrary value that determines the baseline for how long the game has been running.
     * Is measured in nanoseconds, and needs to be converted to tenths of a second by dividing by
     * 100 000 000 after the total passed time has been acquired..
     */
    private long startTime;


    /**
     * A value that stores the amount of time already elapsed in previous saves.
     * When a new Java instance is opened, the procured startTime is not necessarily 0. By storing
     * how much time has accrued on the save file, can understand how much further time has
     * accrued in a new play instance.
     */
    private long elapsedTime;

    /**
     * Builds a new MinesweeperGame that follows the parameters provided.
     * Also saves the timestamp of the Java Virtual Machine.
     * @param gridSize: int. Specifies how many rows and columns of tiles make up the grid.
     * @param numBombs: int. Specifies how many bombs should be distributed randomly across the
     *                grid.
     */
    public MinesweeperGame(int gridSize, int numBombs) {
        grid = new Grid(gridSize, numBombs);
        startTime = System.nanoTime();
    }

    /**
     * Builds a MinesweeperGame identical to one described by the settings provided.
     * @param settings: Settings that describe a certain MinesweeperGame completely.
     */
    public MinesweeperGame(List<Object> settings) {
        this.grid = (Grid) settings.get(0);
        setStartTime((long) (int) settings.get(1) * 100000000);
    }


    /**
     * A getter for grid
     * @return grid the current grid for the game
     */
    public Grid getGrid() {
        return grid;
    }


    /**
     * Upon receiving a click on position (x, y), evaluate the content of the grid at this location.
     * Reveals the grid. If it hides bomb, triggers a game over sequence. If it contains a 0-tile,
     * reveals surrounding tiles.
     *
     * @param x Row Number
     * @param y Column Number
     */
    void processClick(Context context, int x, int y) {
        int size = grid.getGridSize();
        Cell current = grid.getCellAt(x, y);
        if (x >= 0 && y >= 0 && x < size && y < size &&
                !current.isRevealed() && !current.isFlagged()) {

            current.setRevealed();
            winOrLose(context, current);
            checkAdjacentTiles(context, x, y, size);
        } else if(x >= 0 && y >= 0 && x < size && y < size) {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Recursively calls processClick on Tiles adjacent to the current tile if the tile has a value
     * of 0 since revealing all adjacent tiles will not reveal a bomb in this case.
     * @param context the current context
     * @param x the column that the cell is in
     * @param y the row that the cell is in
     * @param size the size of the board (the same in both directions)
     */
    private void checkAdjacentTiles(Context context, int x, int y, int size) {
        if(grid.getCellAt(x , y).getValue() == 0) {
            for (int xt = -1; xt <= 1; xt++) {
                for (int yt = -1; yt <= 1; yt++) {
                    if (xt != 0 || yt != 0) {
                        if (x + xt < size && y + yt < size && x + xt >= 0 && y + yt >= 0 &&
                                !grid.getCellAt(x + xt, y + yt).isRevealed()) {
                            processClick(context, x + xt, y + yt);
                        }
                    }
                }
            }
        }
    }

    /**
     * A method to check if the the game is solved or if the user has clicked on a bomb by taking
     * the current tile that process click is using and seeing if it is a bomb. For solved it simply
     * checks if the grid is solved since the current tile is already revealed in process click.
     * @param context the current app context
     * @param current the current tile that processClick is checking
     */
    private void winOrLose(Context context, Cell current) {
        if(isSolved()){
            Toast.makeText(context, "YOU WIN!", Toast.LENGTH_LONG).show();
            setChanged();
            notifyObservers(GlobalCenter.MINESWEEPER_GAME_NAME);
            clearChanged();
            return;
        }
        if (current.isBomb()) {
            current.setImageId();
            onGameLost(context);
        }
    }

    /**
     * Process a long click and add/remove flag from a non-revealed Cell at position (x, y)
     * which is where the user clicked and held.
     * long click being defined by android default standards as a press and hold.
     *
     * @param x Row Number
     * @param y Column Number
     */
    void processLongClick(Context context, int x, int y) {
        int size = grid.getGridSize();
        Cell current = grid.getCellAt(x, y);
        if (x >= 0 && y >= 0 && x < size && y < size && !current.isRevealed()) {
            if (!current.isFlagged()) {
                current.setFlagged(true);
                if(isSolved()){
                    Toast.makeText(context, "YOU WIN!", Toast.LENGTH_LONG).show();
                    setChanged();
                    notifyObservers(GlobalCenter.MINESWEEPER_GAME_NAME);
                    clearChanged();
                }
            }else {
                current.setFlagged(false);
            }
        }else{
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when a user clicks on a bomb. reveals all cells and makes Toast show the user
     * they lost
     * @param context the current app context
     */
    private void onGameLost(Context context) {
        revealAllCells();
        Toast.makeText(context, "Game Over", Toast.LENGTH_LONG).show();
    }

    /**
     * Reveals all of the tiles in the grid.
     * Call after a game is lost so that a player may see the overall layout of the game, and may
     * see if they misplaced any flags.
     */
    private void revealAllCells() {
        int size = grid.getGridSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (!grid.getCellAt(x,y).isRevealed()) {
                    grid.getCellAt(x,y).setRevealed();
                }
            }
        }
    }

    /**
     * Checks to see whether or not the game is in a solved state.
     * A game is in a solved state if and only if all the non-bomb tiles have been revealed.
     * If a game is determined to be solved, informs the GlobalCenter that the game has been
     * completed so that the player's score can be calculated and placed on the scoreboard.
     *
     * @return boolean: whether or not the game has been solved.
     */
    private boolean isSolved() {
        boolean solved = true;
        for (int x = 0; x < grid.getGridSize(); x++) {
            for (int y = 0; y < grid.getGridSize(); y++) {
                if (!grid.getCellAt(x,y).isBomb() && !grid.getCellAt(x,y).isRevealed()) {
                    solved = false;
                }
            }
        }
        int bombsLeft = grid.getNumBombs();
        for (int x = 0; x < grid.getGridSize(); x++) {
            for (int y = 0; y < grid.getGridSize(); y++) {
                if(grid.getCellAt(x,y).isFlagged() && grid.getCellAt(x,y).isBomb()){
                    bombsLeft--;
                }
            }
        }
        if (bombsLeft == 0) {
            return true;
        }
        return solved;
    }

    /**
     * Returns the total elapsed time, as an integer representing tenths of seconds.
     * Updates the total amount of elapsed time for the game, then resets startTime so that time
     * elapsed since last update will not double-count.
     * This allows the timer to be stopped when the game is paused, or when game play is interrupted
     * for some reason.
     * If a game is paused, then setStartTime should be called when the game is un-paused.
     *
     * @return int. How many tenths of seconds has passed since the beginning of the game.
     */
    private int getElapsedTime() {
        Long endTime = System.nanoTime();
        elapsedTime = endTime - startTime + elapsedTime;
        startTime = System.nanoTime();
        return (int) elapsedTime / 100000000;
    }

    /**
     * Receives the already elapsed time as a long representing the elapsed nanoseconds.
     * Stores this value in nanoseconds as a long, then saves the timestamp of the Java Virtual
     * Machine.
     * Should only be called after un-pausing a game if getElapsedTime was called when the game
     * was paused.
     *
     * @param storedTime: long. The amount of time already elapsed on the save file.
     */
    private void setStartTime(long storedTime) {
        elapsedTime = storedTime;
        startTime = System.nanoTime();
    }

    /**
     * @return List<Object>. A list containing two element:
     *      the grid containing all cells, which
     *          includes where the bombs are, which tiles are safe, which cells have been revealed,
     *          and which cells have been flagged.
     *      a time value, measured in tenths of seconds, which needs to be converted to nanoseconds
     *          and then used to determine what to set the start time to.
     */
    @Override
    public List<Object> getSettings() {
        ArrayList<Object> result = new ArrayList<>();
        result.add(this.grid);
        result.add(getElapsedTime());
        return result;
    }
}
