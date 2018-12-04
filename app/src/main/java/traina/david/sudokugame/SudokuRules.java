package traina.david.sudokugame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static traina.david.sudokugame.SudokuGrid.GRID_SIZE;
import static traina.david.sudokugame.SudokuGrid.getCellCol;
import static traina.david.sudokugame.SudokuGrid.getCellPosition;
import static traina.david.sudokugame.SudokuGrid.getCellRow;

/**
 * The logic related to the rules of Sudoku, especially whether a particular SudokuCell in a SudokuGrid
 * conflicts with other cells in the grid according to standard Sudoku rules
 */
class SudokuRules {

    /**
     * A Random object used for the creation of the grid.
     */
    private static final Random random = new Random();

    /**
     * An algorithm that constructs and returns a random solvable sudoku board as a 2D array of
     * SudokuCells with a number of revealed cells dependent on difficulty.
     *
     * @param difficulty 0 = easy (60 revealed cells), 1 = medium (50 revealed), 2 = hard (40 revealed)
     * @return a random solvable sudoku board as a 2D array of SudokuCells
     */
    static SudokuCell[][] generateSudokuBoard(int difficulty) {
        SudokuCell[][] cells = generateBlankBoard();

        // A list of lists of possible Ids for each SudokuCell in cells.
        List<List<Integer>> possibleIds = generatePossibleIds();

        // Iterate through all the positions.
        int position = 0;
        while (position < GRID_SIZE * GRID_SIZE) {
            if (assignValidId(cells, position, possibleIds.get(position))) {
                position++;
            } else {
                // Remove the current Id of the previous cell from the possible Ids for the previous
                // cell to prevent this from happening again. We go back and choose a new Id.
                int prevPosition = position - 1;
                Integer idOfPreviousCell =
                        cells[getCellRow(prevPosition)][getCellCol(prevPosition)].getSolvedId();
                possibleIds.get(prevPosition).remove(idOfPreviousCell);

                resetPossibleIdsForPosition(possibleIds, position);

                position--;
            }
        }
        hideCells(cells, 21 + difficulty * 10);
        return cells;
    }

    /**
     * Return a set of positions (row-major order) of SudokuCells in the grid which conflict with the
     * SudokuCell at row row and column col under standard Sudoku rules. Note that a set is not
     * ordered and contains no duplicates.
     *
     * @param cells A 2D array of SudokuCells representing the grid.
     * @param row   The row in the grid where the given SudokuCell is.
     * @param col   The column in the grid where the given SudokuCell is.
     * @return A set of positions (row-major order) of SudokuCells in the grid which conflict with the
     * SudokuCell at the given position.
     */
    static Set<Integer> findConflicts(SudokuCell[][] cells, int row, int col) {
        Set<Integer> conflicts = findConflictsInRow(cells, row, col);
        conflicts.addAll(findConflictInCol(cells, row, col));
        conflicts.addAll(findConflictInSubGrid(cells, row, col));
        return conflicts;
    }

    /**
     * Return a set of positions (row-major order) of SudokuCells in the grid which conflict with the
     * SudokuCell at row row and column col under standard Sudoku rules. Note that a set is not
     * ordered and contains no duplicates.
     *
     * @param grid The SudokuGrid.
     * @param row  The row in the grid where the given SudokuCell is.
     * @param col  The column in the grid where the given SudokuCell is.
     * @return A set of positions (row-major order) of SudokuCells in the grid which conflict with the
     * SudokuCell at the given position.
     */
    static Set<Integer> findConflicts(SudokuGrid grid, int row, int col) {
        SudokuCell[][] cells = new SudokuCell[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                cells[r][c] = grid.getSudokuCell(r, c);
            }
        }
        return findConflicts(cells, row, col);
    }

    /**
     * Return a new GRID_SIZE x GRID_SIZE 2D array of blank SudokuCells which represents a blank
     * Sudoku board.
     *
     * @return a new GRID_SIZE x GRID_SIZE 2D array of blank SudokuCells (Ids = -1).
     */
    private static SudokuCell[][] generateBlankBoard() {
        SudokuCell[][] cells = new SudokuCell[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new SudokuCell();
            }
        }
        return cells;
    }

    /**
     * Return a list of lists of possible Ids for each SudokuCell in a GRID_SIZE x GRID_SIZE Sudoku
     * board.
     *
     * @return a list of lists of possible Ids for each SudokuCell in a GRID_SIZE x GRID_SIZE Sudoku
     * board.
     */
    private static List<List<Integer>> generatePossibleIds() {
        List<List<Integer>> possibleIds = new ArrayList<>(GRID_SIZE * GRID_SIZE);
        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {
            possibleIds.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
            Collections.shuffle(possibleIds.get(i), random);
        }
        return possibleIds;
    }

    /**
     * Assign a valid solvedId to the SudokuCell at position from possibleNumbersForPosition that
     * does not conflict with any of the previously chosen solvedIds. Return true iff this is done
     * successfully.
     *
     * @param cells                  A 2D array of SudokuCells representing the grid.
     * @param position               The position of the SudokuCell in the grid (row-major order).
     * @param possibleIdsForPosition The pool of numbers we can choose the solvedId from.
     * @return True iff a valid solvedId was assigned to the SudokuCell at position.
     */
    private static boolean assignValidId(SudokuCell[][] cells, int position,
                                         List<Integer> possibleIdsForPosition) {
        int row = getCellRow(position);
        int col = getCellCol(position);
        for (int possibleNumber : possibleIdsForPosition) {
            // findConflicts() uses the currentId.
            cells[row][col].setCurrentId(possibleNumber);
            cells[row][col].setSolvedId(possibleNumber);
            if (findConflicts(cells, row, col).size() == 0) {
                return true;
            }
        }
        cells[row][col].setCurrentId(-1);
        cells[row][col].setSolvedId(-1);
        return false;
    }

    /**
     * Reset the possible Ids for the given position back to 1-9 in a shuffled order.
     *
     * @param possibleIds The list of possible Ids for all positions.
     * @param position    The position to reset.
     */
    private static void resetPossibleIdsForPosition(List<List<Integer>> possibleIds, int position) {
        possibleIds.set(position, new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        Collections.shuffle(possibleIds.get(position), random);
    }

    /**
     * Hide numCellsToHide randomly chosen SudokuCells in the grid by setting their currentId to -1
     * (but leaving their solvedIds unchanged).
     *
     * @param cells          A 2D array of SudokuCells representing the grid.
     * @param numCellsToHide The number of cells to hide.
     */
    private static void hideCells(SudokuCell[][] cells, int numCellsToHide) {
        for (int i = 0; i < numCellsToHide; i++) {
            SudokuCell cellToHide;
            // The while condition ensures the cell is not already hidden.
            do {
                cellToHide = cells[random.nextInt(GRID_SIZE)][random.nextInt(GRID_SIZE)];
            } while (cellToHide.getCurrentId() == -1);
            cellToHide.setMutable();
            cellToHide.setCurrentId(-1);
        }
    }

    /**
     * Return a set of positions (row-major order) of SudokuCells in row row which have the same
     * currentId as the SudokuCell at row row and column col (and thus, conflict).
     *
     * @param cells A 2D array of SudokuCells representing the grid.
     * @param row   The row in the grid where the given SudokuCell is.
     * @param col   The column in the grid where the given SudokuCell is.
     * @return A set of positions (row-major order) of SudokuCells in row row which have the same
     * currentId as the SudokuCell at row row and column col.
     */
    private static Set<Integer> findConflictsInRow(SudokuCell[][] cells, int row, int col) {
        Set<Integer> conflicts = new HashSet<>();
        for (int col2 = 0; col2 < GRID_SIZE; col2++) {
            if (cells[row][col2].getCurrentId() == cells[row][col].getCurrentId()
                    && col != col2) {
                conflicts.add(getCellPosition(row, col2));
            }
        }
        return conflicts;
    }

    /**
     * Return a set of positions (row-major order) of SudokuCells in column col which have the same
     * currentId as the SudokuCell at row row and column col (and thus, conflict).
     *
     * @param cells A 2D array of SudokuCells representing the grid.
     * @param row   The row in the grid where the given SudokuCell is.
     * @param col   The column in the grid where the given SudokuCell is.
     * @return A set of positions (row-major order) of SudokuCells in column col which have the same
     * currentId as the SudokuCell at row row and column col.
     */
    private static Set<Integer> findConflictInCol(SudokuCell[][] cells, int row, int col) {
        Set<Integer> conflicts = new HashSet<>();
        for (int row2 = 0; row2 < GRID_SIZE; row2++) {
            if (cells[row2][col].getCurrentId() == cells[row][col].getCurrentId()
                    && row != row2) {
                conflicts.add(getCellPosition(row2, col));
            }
        }
        return conflicts;
    }

    /**
     * Return a set of positions (row-major order) of SudokuCells in the same sub-grid and have the
     * same currentId as the SudokuCell at row row and column col (and thus, conflict). Note that a
     * sudoku grid is split into 9 sub-grids of 9 cells each.
     *
     * @param cells A 2D array of SudokuCells representing the grid.
     * @param row   The row in the grid where the given SudokuCell is.
     * @param col   The column in the grid where the given SudokuCell is.
     * @return A set of positions (row-major order) of SudokuCells in the same sub-grid and have the
     * same currentId as the SudokuCell at row row and column col.
     */
    private static Set<Integer> findConflictInSubGrid(SudokuCell[][] cells, int row, int col) {
        Set<Integer> conflicts = new HashSet<>();
        int startingSubGridRow = (row / 3) * 3; //Note: Integer division.
        int startingSubGridCol = (col / 3) * 3;
        // Iterate over the sub-grid.
        for (int subGridRow = startingSubGridRow; subGridRow < startingSubGridRow + 3; subGridRow++) {
            for (int subGridCol = startingSubGridCol; subGridCol < startingSubGridCol + 3; subGridCol++) {
                if (cells[subGridRow][subGridCol].getCurrentId() == cells[row][col].getCurrentId() &&
                        !(row == subGridRow && col == subGridCol)) {
                    conflicts.add(getCellPosition(subGridRow, subGridCol));
                }
            }
        }
        return conflicts;
    }
}
