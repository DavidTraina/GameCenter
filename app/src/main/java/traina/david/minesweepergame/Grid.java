package traina.david.minesweepergame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;


/**
 * A class containing a square (nxn) array of empty cells. Each cell in the grid contains either
 * a bomb or a value that shows how many bombs are adjacent to it, including diagonals.
 *
 * Also contains various accessor and mutator methods for interacting with the grid for use in a
 * Minesweeper game.
 */
public class Grid extends Observable implements Serializable, Observer {

    /**
     * The size of the square nxn grid.
     */
    private final int gridSize;
    /**
     * The number of bombs to be distributed across the grid.
     */
    private final int numBombs;
    /**
     * A nested array of Cells that represent the grid.
     */
    private final Cell[][] cells;

    Grid(int gridSize, int numBombs) {
        this.gridSize = gridSize;
        this.numBombs = numBombs;
        this.cells = constructGrid(gridSize, numBombs);
        for (int j = 0; j < gridSize; j++) {
            for (int i = 0; i < gridSize; i++) {
                cells[j][i].addObserver(this);
            }
        }
    }

    /**
     * Construct a nxn grid of with numBombs and gridSize. Includes setting locations of mines
     * and values of each Cell.
     * @param gridSize The size of the square grid.
     * @param numBombs The number of mines to place on this Grid.
     * @return A Cell[][] with updated values.
     */
    private Cell[][] constructGrid(int gridSize, int numBombs) {
        Cell[][] newGrid = new Cell[gridSize][gridSize];

        Random r = new Random();
        // Set locations of mines.
        while (numBombs > 0) {
            int x = r.nextInt(gridSize);
            int y = r.nextInt(gridSize);
            Cell mineCell = new Cell(-1);
            if (newGrid[x][y] != null && newGrid[x][y].getValue() != -1) {
                newGrid[x][y] = mineCell;
                numBombs--;
            } else if (newGrid[x][y] == null) {
                newGrid[x][y] = mineCell;
                numBombs--;
            }
        }
        // Update the values of surrounding Cells after mines have been set.
        newGrid = updateNeighbors(newGrid, gridSize);

        return newGrid;
    }

    /**
     * Update the value associated with each Cell by determining the number of neighbouring
     * bombs.
     * @param newGrid The grid to update.
     * @param gridSize The size of the grid.
     * @return The updated Grid.
     */
    private Cell[][] updateNeighbors(Cell[][] newGrid, int gridSize) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (newGrid[i][j] != null && !newGrid[i][j].isBomb()) {
                    List<Cell> neighbours = getAllNeighbours(newGrid, i,j,gridSize);
                    newGrid[i][j] = getNeighbouringBombs(neighbours);
                } else if (newGrid[i][j] == null) {
                    List<Cell> neighbours = getAllNeighbours(newGrid, i,j,gridSize);
                    newGrid[i][j] = getNeighbouringBombs(neighbours);
                }
            }
        }
        return newGrid;
    }

    /**
     * Return a new Cell with value equal to the number of neighbouring bombs.
     * @param neighbours A list of surrounding Cells.
     * @return Cell
     */
    private Cell getNeighbouringBombs(List<Cell> neighbours) {
        int count = 0;

        for (int i=0; i < neighbours.size(); i++) {
            if(neighbours.get(i) != null) {
                if (neighbours.get(i).isBomb()) {
                    count++;
                }
            }
        }
        return new Cell(count);
    }

    /**
     * Generate a list of all neighbouring Cells at position (x, y).
     * @param newGrid THe grid to update.
     * @param x Column of Cell position.
     * @param y Row of Cell position.
     * @param gridSize The size of the grid.
     * @return A list of Cells that surround the Cell at position (x, y).
     */
    private List<Cell> getAllNeighbours(Cell[][] newGrid, int x, int y, int gridSize) {
        List<Cell> result = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                boolean withinRange = (x+i) >=0 && (y+j) >=0 && (x+i) < gridSize && (y+j) < gridSize;
                if (!(i==0 && j==0) && withinRange) {
                    result.add(newGrid[x+i][y+j]);
                }
            }
        }
        return result;
    }


    /**
     * A getter for cell based on it's x and y coordinate
     * @param x the column that the cell is in
     * @param y the row that the cell is in
     * @return a cell at the specified location
     */
    Cell getCellAt(int x, int y) {
        return this.cells[x][y];
    }

    /**
     * A getter for gridSize.
     * @return gridSize: the size of the grid (same number of columns / rows)
     */
    public int getGridSize() {
        return gridSize;
    }

    /**
     * A getter for numBombs.
     * @return numBombs: the numbers of cells that are bombs in this grid
     */
    public int getNumBombs() { return numBombs; }

    @Override
    public void update(Observable observable, Object o) {
        setChanged();
        notifyObservers();
    }
}