package traina.david.sudokugame;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static traina.david.sudokugame.SudokuGrid.GRID_SIZE;
import static traina.david.sudokugame.SudokuGrid.getCellPosition;
import static traina.david.sudokugame.SudokuRules.findConflicts;
import static org.junit.Assert.assertEquals;

public class SudokuRulesTest {

    @Test
    public void testGenerateSudokuBoardReturnSolvableBoard() {
        SudokuCell[][] cells = SudokuRules.generateSudokuBoard(2);
        setAllCurrentIdsToSolvedIds(cells); // findConflicts() checks the currentIds.
        Set<Integer> conflicts = new HashSet<>();
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                conflicts.addAll(findConflicts(cells, row, col));
            }
        }
        assertEquals(0, conflicts.size());
    }

    @Test
    public void testGenerateSudokuBoardRevealsCorrectNumberOfCells0() {
        SudokuCell[][] cells = SudokuRules.generateSudokuBoard(0);
        int revealedCount = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col].getCurrentId() != -1) {
                    revealedCount++;
                }
            }
        }
        assertEquals(60, revealedCount);
    }

    @Test
    public void testGenerateSudokuBoardRevealsCorrectNumberOfCells1() {
        SudokuCell[][] cells = SudokuRules.generateSudokuBoard(1);
        int revealedCount = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col].getCurrentId() != -1) {
                    revealedCount++;
                }
            }
        }
        assertEquals(50, revealedCount);
    }

    @Test
    public void testGenerateSudokuBoardRevealsCorrectNumberOfCells2() {
        SudokuCell[][] cells = SudokuRules.generateSudokuBoard(2);
        int revealedCount = 0;
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (cells[row][col].getCurrentId() != -1) {
                    revealedCount++;
                }
            }
        }
        assertEquals(40, revealedCount);
    }

    @Test
    public void testFindConflictConflictInRowReturnConflictingCell() {
        SudokuCell[][] cells = generateBlankBoard();
        SudokuCell conflictingCell = cells[0][5];
        SudokuCell conflictingCell2 = cells[0][0];
        conflictingCell2.setCurrentId(3);
        conflictingCell.setCurrentId(3);

        Set<Integer> expected = new HashSet<>();
        expected.add(SudokuGrid.getCellPosition(0, 5));
        Set<Integer> actual = findConflicts(cells, 0, 0);
        assertEquals(expected, actual);

        expected = new HashSet<>();
        expected.add(SudokuGrid.getCellPosition(0, 0));
        actual = findConflicts(cells, 0, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindConflictConflictInColReturnConflictingCell() {
        SudokuCell[][] cells = generateBlankBoard();
        SudokuCell conflictingCell = cells[0][0];
        SudokuCell conflictingCell2 = cells[5][0];
        conflictingCell2.setCurrentId(2);
        conflictingCell.setCurrentId(2);

        Set<Integer> expected = new HashSet<>();
        expected.add(SudokuGrid.getCellPosition(0, 0));
        Set<Integer> actual = findConflicts(cells, 5, 0);
        assertEquals(expected, actual);

        expected = new HashSet<>();
        expected.add(SudokuGrid.getCellPosition(5, 0));
        actual = findConflicts(cells, 0, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindConflictConflictInSubGridNotRowOrColReturnConflictingCell() {
        SudokuCell[][] cells = generateBlankBoard();
        SudokuCell conflictingCell = cells[7][5];
        SudokuCell conflictingCell2 = cells[8][3];
        conflictingCell2.setCurrentId(6);
        conflictingCell.setCurrentId(6);

        Set<Integer> expected = new HashSet<>();
        expected.add(SudokuGrid.getCellPosition(7, 5));
        Set<Integer> actual = findConflicts(cells, 8, 3);
        assertEquals(expected, actual);

        expected = new HashSet<>();
        expected.add(SudokuGrid.getCellPosition(8, 3));
        actual = findConflicts(cells, 7, 5);
        assertEquals(expected, actual);
    }

    @Test
    public void testFindConflictsGrid() {
        SudokuGrid grid = new SudokuGrid(2);
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                grid.getSudokuCell(row, col).setCurrentId(-1);
                grid.getSudokuCell(row, col).setSolvedId(-1);
            }
        }
        Set<Integer> expected = new HashSet<>();
        // Set entire row to conflict.
        for (int col = 0; col < GRID_SIZE; col++) {
            grid.getSudokuCell(4, col).setCurrentId(8);
            expected.add(getCellPosition(4, col));
        }

        // Set entire column to conflict.
        for (int row = 0; row < GRID_SIZE; row++) {
            grid.getSudokuCell(row, 6).setCurrentId(8);
            expected.add(getCellPosition(row, 6));

        }

        //Set entire sub-grid to conflict.
        for (int subGridRow = 3; subGridRow < 6; subGridRow++) {
            for (int subGridCol = 6; subGridCol < 9; subGridCol++) {
                grid.getSudokuCell(subGridRow, subGridCol).setCurrentId(8);
                expected.add(getCellPosition(subGridRow, subGridCol));

            }
        }

        // Remove the position of the cell at row 4 column 6 because we are checking conflicts for it.
        expected.remove(getCellPosition(4, 6));
        Set<Integer> actual = findConflicts(grid, 4, 6);
        assertEquals(expected, actual);

    }
    @Test
    public void testFindConflictMultipleConflictsReturnConflictingCells() {
        SudokuCell[][] cells = generateBlankBoard();
        Set<Integer> expected = new HashSet<>();
        // Set entire row to conflict.
        for (int col = 0; col < GRID_SIZE; col++) {
            cells[4][col].setCurrentId(8);
            expected.add(getCellPosition(4, col));
        }

        // Set entire column to conflict.
        for (int row = 0; row < GRID_SIZE; row++) {
            cells[row][6].setCurrentId(8);
            expected.add(getCellPosition(row, 6));

        }

        //Set entire sub-grid to conflict.
        for (int subGridRow = 3; subGridRow < 6; subGridRow++) {
            for (int subGridCol = 6; subGridCol < 9; subGridCol++) {
                cells[subGridRow][subGridCol].setCurrentId(8);
                expected.add(getCellPosition(subGridRow, subGridCol));

            }
        }

        // Remove the position of the cell at row 4 column 6 because we are checking conflicts for it.
        expected.remove(getCellPosition(4, 6));
        Set<Integer> actual = findConflicts(cells, 4, 6);
        assertEquals(expected, actual);
    }

    private SudokuCell[][] generateBlankBoard() {
        SudokuCell[][] cells = new SudokuCell[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = new SudokuCell();
            }
        }
        return cells;
    }

    private void setAllCurrentIdsToSolvedIds(SudokuCell[][] cells) {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col].setCurrentId(cells[row][col].getSolvedId());
            }
        }
    }
}