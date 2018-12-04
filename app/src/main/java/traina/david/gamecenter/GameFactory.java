package traina.david.gamecenter;

import java.util.List;

import traina.david.minesweepergame.MinesweeperGame;
import traina.david.slidingtilegame.SlidingTileGame;
import traina.david.sudokugame.SudokuGame;

import static traina.david.gamecenter.GlobalCenter.SLIDING_TILE_GAME_NAME;
import static traina.david.gamecenter.GlobalCenter.SUDOKU_GAME_NAME;

/**
 * A factory that creates Games based on what string it receives. If it receives a settings list,
 * applies those settings to the game before returning the newly created instance.
 */
class GameFactory {

    /**
     * Receives a game's unique string identifier and a set of settings, and creates a new
     * game based on the specifications.
     * <p>
     * getGame() assumes that the settings provided to the factory are applicable to the game
     * that is being requested. Does several simple checks to limit bad inputs, but improper
     * information formatting will result in crashes.
     *
     * @param gameName: String. The string identifier of the requested game.
     * @param settings: List<?> || int. Information detailing how the game should be made.
     * @return Game: the instance of the game as requested.
     */
    public Game getGame(String gameName, Object settings) {
        if (gameName.equals(SLIDING_TILE_GAME_NAME)) {
            if (settings instanceof List) {
                return getSlidingTileGame((List<Object>) settings);
            } else if (settings instanceof Integer) {
                return getSlidingTileGame((int) settings);
            }
        }
        if (gameName.equals(SUDOKU_GAME_NAME)){
            if (settings instanceof List){
                return getSudokuGame((List) settings);
            } else if (settings instanceof Integer){
                return getSudokuGame((Integer) settings);
            }
        }

        if (gameName.equals(GlobalCenter.MINESWEEPER_GAME_NAME)) {
            if (settings instanceof List) {
                return getMinesweeperGame((List<Object>) settings);
            }
        }
        return null;
    }

    /**
     * Builds a SlidingTileGame based on the settings provided.
     *
     * @param settings: List<?>. In order, list should contain the Board, step count,
     *                  undo count, and undo board states.
     * @return Game: specifically a SlidingTileGame whose settings match the provided settings.
     */
    private Game getSlidingTileGame(List<Object> settings) {
        return new SlidingTileGame(settings);
    }

    /**
     * Builds a new SlidingTileGame based on the input.
     *
     * @param boardSize: int. The complexity of the game, or how big the board is.
     * @return Game: specifically, a SlidingTileGame with complexity equal to the provided value.
     */
    private Game getSlidingTileGame(int boardSize) {
        return new SlidingTileGame(boardSize);
    }

    /**
     * Builds a new MinesweeperGame using the specifications provided via settings.
     *
     * @param settings: List<Objects>. A list that fully describes the status of a MinesweeperGame.
     * @return Game: A MinesweeperGame that is described by the settings provided.
     */
    private Game getMinesweeperGame(List<Object> settings) {
        return new MinesweeperGame(settings);
    }

    /**
     * Builds a new SudokuGame using the specifications provided via settings.
     *
     * @param settings: A list that fully describes the status of a SudokuGame.
     * @return Game: A SudokuGame that is described by the settings provided.
     */
    private Game getSudokuGame(List settings){
        return new SudokuGame(settings);
    }

    /**
     * builds a new SudokuGame using the the provided difficulty
     *
     * @param difficulty the difficulty of the game either easy(0), medium(1) or hard(2)
     * @return Game: a SudokuGame that is described by the settings provided
     */
    private Game getSudokuGame(Integer difficulty){
        return new SudokuGame(difficulty);
    }
}
