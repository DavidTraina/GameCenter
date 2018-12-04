package traina.david.sudokugame;

import android.content.Context;
import android.widget.Toast;

import traina.david.gamecenter.Controller;
import traina.david.gamecenter.Game;

import static traina.david.sudokugame.SudokuGrid.GRID_SIZE;

/**
 * Processes button presses in SudokuGameActivity.
 */
public class SudokuGridController extends Controller {

    /**
     * The grid containing the SudokuCells on the board.
     */
    private SudokuGrid grid;

    /**
     * The next input to be inserted. -1 corresponds to clearing the cell.
     */
    private int cellInput = -1;

    public SudokuGridController() {
    }

    /**
     * Passes the grid to be manipulated to this controller.
     *
     * @param game The current game.
     */
    public void setGame(Game game) {
        this.addObserver((SudokuGame) game);
        grid = ((SudokuGame) game).getSudokuGrid();
    }

    /**
     * Updates the isConflict values for all cells which conflict with others in the grid.
     */
    private void updateConflicts() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (SudokuRules.findConflicts(grid, row, col).size() != 0) {
                    grid.getSudokuCell(row, col).setConflicting(true);
                } else {
                    grid.getSudokuCell(row, col).setConflicting(false);
                }
            }
        }
    }

    /**
     * Clears cell if it is long-pressed.
     *
     * @param context The current app context
     * @param buttonTapped - int corresponding to position of button tapped
     */
    public void processLongTap(Context context, int buttonTapped){
        if (18 <= buttonTapped && buttonTapped <= 98){
            int row = SudokuGrid.getCellRow(buttonTapped - 18);
            int col = SudokuGrid.getCellCol(buttonTapped - 18);
            SudokuCell cell = grid.getSudokuCell(row, col);
            if (cell.isMutable()){
                cell.setCurrentId(-1);
                updateConflicts();
            }
        }
    }

    /**
     * Sets cellInput to corresponding button tapped if button tapped is from the first two rows.
     *
     * Otherwise, loads cellInput to a selected Cell.
     *
     * @param context The current app context.
     * @param buttonTapped The position of the button tapped on the screen.
     */
    public void processTapMovement(Context context, int buttonTapped) {
        //runs if a button from the top row is pressed.
        if (buttonTapped <= 8) {
            cellInput = buttonTapped + 1;
        } else if (buttonTapped == 9) {
            undo(context);
            updateConflicts();
        } else if (buttonTapped == 17){
            cellInput = -1;
            //runs if a button from the bottom 9x9 grid is pressed.
        } else if (18 <= buttonTapped && buttonTapped <= 98) {
            int row = SudokuGrid.getCellRow(buttonTapped - 18);
            int col = SudokuGrid.getCellCol(buttonTapped - 18);
            SudokuCell cell = grid.getSudokuCell(row, col);
            int prevCurrentId = cell.getCurrentId();
            processNumberPlacement(context, cell);
            grid.addLastMove(row, col, prevCurrentId);
        }
    }

    private void undo(Context context) {
        int[] lastMove = grid.getLastMove();
        if (lastMove.length == 3) {
            SudokuCell cell = grid.getSudokuCell(lastMove[0], lastMove[1]);
            cell.setCurrentId(lastMove[2]);
        } else {
            Toast.makeText(context, "There's nothing to undo", Toast.LENGTH_LONG).show();
        }
    }

    private void processNumberPlacement(Context context, SudokuCell cell) {
        if (cell.isMutable()) {
            if (cell.isEmpty()) {
                cell.setCurrentId(cellInput);
                if (cell.getCurrentId() != cell.getSolvedId()) {
                    grid.increaseWrongMoves();
                } else if (grid.isSolved()) {
                    Toast.makeText(context, "YOU WIN!", Toast.LENGTH_LONG).show();
                    setChanged();
                    notifyObservers();
                    clearChanged();
                }
                updateConflicts();
            } else {
                if (cellInput == -1) {
                    cell.setCurrentId(-1);
                    updateConflicts();
                } else {
                    Toast.makeText(context, "There is already a number here, please remove it and " +
                            "try again.", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(context, "This is a preset cell, you can't place a number here.",
                    Toast.LENGTH_LONG).show();
        }
    }
}