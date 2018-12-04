package traina.david.sudokugame;

import java.io.Serializable;
import java.util.Observable;

/**
 * A cell in a SudokuGrid. One of the 81 cells on a standard Sudoku board.
 */
class SudokuCell extends Observable implements Serializable {

    /**
     * The number currently held (and displayed) in the cell. -1 indicates a blank cell.
     */
    private int currentId;

    /**
     * The what the currentId of the cell should be when the board is solved.
     */
    private int solvedId;

    /**
     * True iff the cell is not one of the given cells, who's solvedIds are revealed at the beginning
     * of the game. Only the currentIds of cells which are mutable should be altered by the controller
     * during a game.
     */
    private boolean isMutable = false;

    /**
     * True iff the cell conflicts with other cells in the SudokuGrid under standard Sudoku rules.
     */
    private boolean isConflicting;

    /**
     * Creates a new SudokuCell with currentId currentId and solvedId solvedId.
     *  */
    SudokuCell() {
        this.solvedId = -1;
        this.currentId = -1;
    }

    /**
     * Return the solvedId of the cell.
     *
     * @return the solvedId of the cell.
     */
    int getSolvedId() {
        return solvedId;
    }

    /**
     * Return True iff the cell is mutable, which is the value of isMutable.
     *
     * @return True iff the cell is mutable.
     */
    boolean isMutable() {
        return isMutable;
    }

    /**
     * Sets the mutability status of the cell to true. Should only be used in the creation of a new
     * SudokuGrid.
     */
    void setMutable() {
        isMutable = true;
    }

    /**
     * Set the solvedId of the cell to solvedId. Should only be used in the creation of a new SudokuGrid.
     *
     * @param solvedId the new solvedId of the cell
     */
    void setSolvedId(int solvedId) {
        this.solvedId = solvedId;
    }

    /**
     * Return the currentId of the cell.
     *
     * @return the currentId of the cell.
     */
    int getCurrentId() {
        return currentId;
    }

    /**
     * Set the currentId of the cell to currentId. Should only be used if isMutable is true or in the
     * creation of a new SudokuGrid.
     *
     * @param currentId The new currentId of the cell.
     */
    void setCurrentId(int currentId) {
        this.currentId = currentId;
        setChanged();
        notifyObservers();
        clearChanged();
    }

    /**
     * Return whether the cell conflicts with other cells in the SudokuGrid; the value of isConflicting.
     *
     * @return Whether the cell conflicts with other cells in the SudokuGrid.
     */
    boolean isConflicting() {
        return isConflicting;
    }

    /**
     * Set the value of isConflicting to conflicting.
     *
     * @param conflicting the new value of isConflicting.
     */
    void setConflicting(boolean conflicting) {
        isConflicting = conflicting;
        setChanged();
        notifyObservers();
        clearChanged();
    }

    /**
     * Return whether the cell is empty of not, meaning currentId == -1;
     *
     * @return Whether the cell is empty of not.
     */
    boolean isEmpty() {
        return currentId == -1;
    }
}