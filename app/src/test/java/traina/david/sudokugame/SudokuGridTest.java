package traina.david.sudokugame;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SudokuGridTest {

    private SudokuGrid solveGrid(SudokuGrid grid) {
        for (int row = 0; row < SudokuGrid.GRID_SIZE; row++) {
            for (int col = 0; col < SudokuGrid.GRID_SIZE; col++) {
                grid.getSudokuCell(row, col).setCurrentId(grid.getSudokuCell(row, col).getSolvedId());
            }
        }
        return grid;
    }

    @Test
    public void testIsSolvedUnsolvedGridReturnFalse() {
        SudokuGrid grid = new SudokuGrid(0);
        assertFalse(grid.isSolved());

    }

    @Test
    public void testisSolvedSolvedGridReturnTrue() {
        SudokuGrid grid = solveGrid(new SudokuGrid(2));
        assertTrue(grid.isSolved());
    }
}