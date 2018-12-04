package traina.david.sudokugame;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import static traina.david.sudokugame.SudokuRules.generateSudokuBoard;

/**
 * Represents a Sudoku board. A grid of SudokuCells.
 */
class SudokuGrid extends Observable implements Observer, Serializable {

    /**
     * An ID number used for serialization.
     */
    private static final long serialVersionUID = 54676L;

    /**
     * The number of rows and columns in the square grid. Static because it is constant and the same
     * for all SudokuGrids.
     */
    static final int GRID_SIZE = 9;

    /**
     * The cells in the grid in row-major order.
     */
    private final SudokuCell[][] cells;

    /**
     * The difficulty of the grid: 0 = easy (60 revealed segments), 1 = medium (50 revealed), 2 = hard (40 revealed).
     */
    private final int difficultyLevel;

    /**
     * Number of wrong moves made by the player
     */
    private int wrongMoves = 0;

    /**
     * Stack of moves made by the player. Elements are arrays of ints in the format [row, col, previous value]
     */
    private final LinkedList<int[]> moveStack = new LinkedList<>();

    /**
     * Create a new Solvable SudokuGrid with a number of revealed cells dependent on difficulty.
     *
     * @param difficulty 0 = easy (40 revealed segments), 1 = medium (30 revealed), 2 = hard (20 revealed).
     */
    SudokuGrid(int difficulty) {
        difficultyLevel = difficulty;
        cells = generateSudokuBoard(difficulty);
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].addObserver(this);
            }
        }
    }

    /**
     * Removes and returns the last element in moveStack
     *
     * @return int[] in the format [row, col, previous value]
     */
    public int[] getLastMove() {
        if (moveStack.size() > 0) {
            return moveStack.removeLast();
        } else {
            return new int[0];
        }
    }

    /**
     * Adds a new entry in moveStack in the format [row, col, prevCurrentID]
     *
     * @param row           - the row of the cell
     * @param col           - the column of the cell
     * @param prevCurrentId - the previous value of the cell
     */
    public void addLastMove(int row, int col, int prevCurrentId) {
        moveStack.addLast(new int[]{row, col, prevCurrentId});
    }

    /**
     * A getter for grid difficulty
     *
     * @return difficulty of grid
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    /**
     * Getter for wrong_moves
     * @return number of wrong moves made by the player
     */
    public int getWrongMoves() {
        return wrongMoves;
    }

    public void increaseWrongMoves(){
        this.wrongMoves++;
    }

    /**
     * A utility method to return the position of a cell (row-major order) in the grid, given the
     * row row and column col of the cell in the grid.
     *
     * @param row The row of the grid where the cell is. First index of cells.
     * @param col The column of the grid where the cell is. Second index of cells.
     * @return The position of the cell in the grid (row-major order).
     */
    static int getCellPosition(int row, int col) {
        return row * GRID_SIZE + col;
    }

    /**
     * A utility method to return the row in the grid where a cell is, given the cell's position in
     * the grid (row-major order).
     *
     * @param position The position of the cell in the grid (row-major order).
     * @return The row in the grid where a cell is.
     */
    static int getCellRow(int position) {
        return position / GRID_SIZE;
    }

    /**
     * A utility method to return the column in the grid where a cell is, given the cell's position
     * in the grid (row-major order).
     *
     * @param position The position of the cell in the grid (row-major order).
     * @return The column in the grid where a cell is.
     */
    static int getCellCol(int position) {
        return position % GRID_SIZE;
    }

    /**
     * Return the SudokuCell at row row and column col in the grid.
     *
     * @param row The row in the grid.
     * @param col The column in the grid.
     * @return The SudokuCell object in row row and column col.
     */
    SudokuCell getSudokuCell(int row, int col) {
        return cells[row][col];
    }

    /**
     * Return true iff the SudokuGrid is solved, meaning the currentId equals the solvedId for every
     * cell in the grid.
     *
     * @return true iff the SudokuGrid is solved.
     */
    boolean isSolved() {
        for (SudokuCell[] cellRow : cells) {
            for (SudokuCell cell : cellRow) {
                if (cell.getCurrentId() != cell.getSolvedId()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
        clearChanged();
    }

}


