package traina.david.slidingtilegame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import traina.david.gamecenter.Game;
import traina.david.gamecenter.GlobalCenter;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */

public class SlidingTileGame extends Game{

    /**
     * The board being managed.
     */
    private final Board board;

    /**
     * The number of operations of player.
     */
    private int numSteps = 0;

    /**
     * limit for undo step.
     */
    private int undoStep = 3;

    /**
     * list for previous board.
     */
    private LinkedList<int[]> undoList = new LinkedList<>();

    /**
     * Manage a board that has been pre-populated.
     *
     * @param settings the settings of the game.
     *                 Precondition: the first element of the settings should be current board, the second one should
     *                 be numSteps, the third one should be undoStep, and the last one should be
     *                 undoList containing previous board. settings should contain only 4 elements.
     */
    public SlidingTileGame(List<Object> settings) {
        this.board = (Board) settings.get(0);
        this.numSteps = (Integer) settings.get(1);
        this.undoStep = (Integer) settings.get(2);
        this.undoList = (LinkedList<int[]>) settings.get(3);
    }


    /**
     * Manage a new shuffled board with default image, i.e, number on background.
     *
     * @param complexity size of the grid
     *                   precondition: the first element in the LinkedList can only be 3, 4, or 5.
     */
    public SlidingTileGame(int complexity) {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = complexity * complexity;

        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            if (tileNum != numTiles - 1) {
                tiles.add(new Tile(tileNum));
            } else {
                tiles.add(new Tile(-1));
                tiles.get(numTiles - 1).setId(numTiles);
            }
        }
        do {
            Collections.shuffle(tiles);
        } while (!isSolvable(tiles, complexity));

        this.board = new Board(tiles, complexity);
    }

    /**
     * Checks if current tile grid is solvable.
     *
     * Mathematical reasoning based on https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
     * Retrieved in Nov 2018
     * @param tiles ArrayList representation of tile grid
     * @param complexity number of rows and columns of tile grid
     * @return True if the configuration in tiles is solvable, False otherwise.
     */
    private boolean isSolvable(List<Tile> tiles, int complexity){
        if (complexity % 2 == 1){
            return (inversions(tiles) % 2 == 0);
        } else {
            if (find_blank_row(tiles, complexity) % 2 == 0) {
                return (inversions(tiles) % 2 == 1);
            }
            return (inversions(tiles) % 2 == 0);
        }
    }

    /**
     * Calculates the number of inversions in a tile grid.
     *
     * Definition of inversion based on https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
     * Retrieved Nov 2018
     *
     * @param tiles ArrayList representation of tile grid
     * @return number of inversions in tiles.
     */
    private int inversions(List<Tile> tiles){
        int num_inversions = 0;
        for (int i = 0; i < tiles.size() - 1; i++){
            for (int j = i + 1; j < tiles.size(); j++){
                if (tiles.get(i).getId() > tiles.get(j).getId() && tiles.get(i).getId() != tiles.size()){
                    num_inversions++;
                }
            }
        }
        return num_inversions;
    }

    /**
     * Returns the row of the blank tile, where 0 is the first row.
     *
     * @param tiles ArrayList representation of tile grid
     * @param complexity number of rows and columns of tile grid
     * @return the row of the blank tile, where 0 is the first row.
     */
    private int find_blank_row(List<Tile> tiles, int complexity){
        int blank_pos = 0;
        for (int i = 0; i < tiles.size(); i++){
            if (tiles.get(i).getId() == tiles.size()){
                blank_pos = i;
            }
        }
        return blank_pos / complexity;
    }


    int getUndoListSize() {
        return this.undoList.size();
    }

    /**
     * @return the settings information of the SlidingTileGame in an ArrayList, where the first
     * element is the current board, the second one is the number of steps the user used, the third
     * one is the preset undoStep of the game, and the last one is the list containing previous
     * boards for undo.
     */
    @Override
    public List<Object> getSettings() {
        ArrayList<Object> result = new ArrayList<>();
        result.add(this.board);
        result.add(this.numSteps);
        result.add(this.undoStep);
        result.add(this.undoList);
        return result;
    }


    /**
     * Set the undoStep
     *
     * @param undoStep number of customized undoStep
     */
    void setUndoStep(int undoStep) {
        this.undoStep = undoStep;
    }

    /**
     * add board to undoList if the size of undoList is less than undoStep, otherwise, remove the
     * first one and add the board to it.
     *
     * @param positions: first/second number: row/col of first swapped tile;
     *                   third/fourth number: row/col of second swapped tile;
     */
    private void addToUndoList(int[] positions) {
        if (this.undoList.size() < this.undoStep) {
            this.undoList.add(positions);
        } else {
            this.undoList.removeFirst();
            this.undoList.add(positions);
        }
    }

    /**
     * If the undoList is not empty, SlidingTileGame's board is reset to the previous one.
     */
    void undo() {
        if (this.undoList.size() != 0) {
            int[] positions = this.undoList.removeLast();
            this.board.swapTiles(positions[0], positions[1], positions[2], positions[3]);
            this.numSteps += 1;
        }
    }

    /**
     * Return the current board.
     */
    Board getBoard() {
        return board;
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        // the solution for whether the puzzle is solved.
        boolean solved = true;
        // default id.
        int previous = board.getTile(0, 0).getId() - 1;

        for (Tile tile : board) {
            solved = previous + 1 == tile.getId() && solved;
            previous = tile.getId();
        }
        if (solved) {
            setChanged();
            notifyObservers(GlobalCenter.SLIDING_TILE_GAME_NAME);
            clearChanged();
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {

        int row = position / this.board.numCols;
        int col = position % this.board.numCols;
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == this.board.numRows - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == this.board.numCols - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Return whether the player is tapping on the undo tile to perform undo operation.
     *
     * @param position the tile to check
     * @return whether the tile at position is the undo tile
     */
    boolean isUndoTap(int position) {

        int row = position / this.board.numCols;
        int col = position % this.board.numCols;
        int blankId = board.numTiles();
        return board.getTile(row, col).getId() == blankId;
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {
        // Which row for the current tapping Tile.
        int row = position / this.board.numRows;
        // Which column for the current tapping Tile.
        int col = position % this.board.numCols;
        // The blank id for the blank Tile.
        int blankId = this.board.numTiles();

        if (isValidTap(position)) {
            // Determine whether the row or column can reach one more position.
            int rMax, cMax;
            rMax = row == this.board.numRows - 1 ? 1 : 2;
            cMax = col == this.board.numCols - 1 ? 1 : 2;
            for (int r = row == 0 ? 0 : -1; r < rMax; r++) {
                for (int c = col == 0 ? 0 : -1; c < cMax; c++) {
                    if (r - c != 0 && r + c != 0
                            && this.board.getTile(row + r, col + c).getId() == blankId) {
                        this.addToUndoList(new int[]{row, col, row + r, col + c});//add the current state to undoList.
                        this.board.swapTiles(row, col, row + r, col + c);
                        this.numSteps += 1;
                    }
                }
            }
        }
    }
}
