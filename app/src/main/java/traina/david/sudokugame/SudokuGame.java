package traina.david.sudokugame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import traina.david.gamecenter.Game;

import static traina.david.gamecenter.GlobalCenter.SUDOKU_GAME_NAME;

/**
 * A game of Sudoku. Used as a way to reference a game in the game center, regardless of game
 * implementation.
 */
public class SudokuGame extends Game implements Observer, Serializable {

    /**
     * The SudokuGrid for the game, which contains all the SudokuCells.
     */
    private final SudokuGrid grid;

    /**
     * Instantiates a SudokuGame based on settings.
     *
     * @param settings a list containing just one element, a grid for a game in progress.
     */
    public SudokuGame(List<?> settings) {
        grid = (SudokuGrid) settings.get(0);
    }

    /**
     * Instantiates a new SudokuGame.
     *
     * @param difficulty The difficulty setting of the game. (0 = easy, 1 = medium, 2 = hard).
     */
    public SudokuGame(int difficulty) {
        grid = new SudokuGrid(difficulty);
    }

    /**
     * Return the SudokuGrid for the game.
     *
     * @return the SudokuGrid for the game.
     */
    SudokuGrid getSudokuGrid() {
        return grid;
    }

    @Override
    public List<Object> getSettings() {
        ArrayList<Object> settings = new ArrayList<>();
        settings.add(grid);
        settings.add(grid.getDifficultyLevel());
        settings.add(grid.getWrongMoves());
        return settings;
    }

    /**
     * Updates observers if the game was solved.
     *
     * @param o   - observable object
     * @param arg - argument passed by observable. In this case, a bool indicating whether a game is solved.
     */
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(SUDOKU_GAME_NAME);
        hasChanged();
    }
}
