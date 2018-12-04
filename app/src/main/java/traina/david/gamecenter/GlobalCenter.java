package traina.david.gamecenter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * A central database containing various information, including references to
 * accounts, their respective localGameCenter, and each game's scoreboard.
 */
public class GlobalCenter implements Serializable, Observer {

    private static final long serialVersionUID = 3333L;

    /**
     * A map of all user accounts with keys as the username
     */
    private final Map<String, Account> accounts = new HashMap<>();
    /**
     * A map of every users LocalGameCenter with keys as the username of the account respective to
     * the LocalGameCenter
     */
    private final Map<String, LocalGameCenter> localCenters = new HashMap<>();
    /**
     * a map of every games scoreboard with keys as the constants for the game name
     */
    private final Map<String, ScoreBoard> scoreboards = new HashMap<>();
    /**
     * The account of the current player
     */
    private Account currentPlayer;

    /**
     * A final static string to be used as the constant for Sliding Tiles game name
     */

    public static final String SLIDING_TILE_GAME_NAME = "slidingTileGame";

    /**
     * A final static string to be used as the constant for minesweeper game name
     */
    public static final String MINESWEEPER_GAME_NAME = "minesweeperGame";

    /**
     * A final static string to be used as the constant for sudoku game name
     */
    public static final String SUDOKU_GAME_NAME = "sudokuGame";

    /**
     * a constructor for GlobalCenter
     */
    GlobalCenter() {
        scoreboards.put(SLIDING_TILE_GAME_NAME, new SlidingTileScoreBoard());
        scoreboards.put(MINESWEEPER_GAME_NAME, new MinesweeperScoreBoard());
        scoreboards.put(SUDOKU_GAME_NAME, new SudokuScoreBoard());
    }

    /**
     * Attempts a login to the system using credentials username and password.
     * Returns true upon a successful sign in. Returns false otherwise.
     *
     * @param username: String
     * @param password: String
     * @return Boolean
     */
    public Boolean signIn(String username, String password) {
        if (accounts.containsKey(username)) {
            Account accountLoggingIn = accounts.get(username);
            if (Objects.requireNonNull(accountLoggingIn).signIn(password)) {
                currentPlayer = accountLoggingIn;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Attempts to create a new account using the username and password provided.
     * Returns true upon a successfully created new account. Also, logs them in.
     * Returns false otherwise.
     */
    public Boolean signUp(String username, String password) {
        if (username.equals("") || password.equals(""))
            return false;
        if (accounts.containsKey(username)) {
            return false;
        } else {
            Account newAccount = new Account(username, password);
            accounts.put(username, newAccount);
            addLocalGameCenter(username);
            signIn(username, password);
            return true;
        }
    }

    /**
     * Adds a new LocalGameCenter to localCenters. Should only be called after signUp.
     *
     * @param username: String
     */
    private void addLocalGameCenter(String username) {
        LocalGameCenter newLocalCenter = new LocalGameCenter(this);
        localCenters.put(username, newLocalCenter);
    }


    /**
     * An accessor for currentPlayer.
     *
     * @return Account.java
     */
    public Account getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Essentially a setter for currentPlayer.
     * Logs out the current account so that a different account can be
     * accessed without having to close the entire application.
     */
    public void signOut() {
        currentPlayer = null;
    }


    /**
     * Returns the LocalGameCenter of the player linked with username.
     * Should only be called after signIn()
     *
     * @param username: String
     * @return LocalGameCenter
     */
    public LocalGameCenter getLocalGameCenter(String username) {
        return localCenters.get(username);
    }

    /**
     * Returns the scoreboard for the game that goes by gameName.
     *
     * @param gameName: String
     * @return ScoreBoard
     */
    public ScoreBoard getScoreBoard(String gameName) {
        return scoreboards.get(gameName);
    }


    /**
     * After receiving an update from a completed game, pulls the score and gameName, and sends
     * it to the game's scoreboard to handle the ranking of the player performance.
     * Also ensures the localGameCenter recognizes the active game is over, and sets the currently
     * active game to null.
     *
     * @param observed: Game
     * @param gameName: String
     */
    @Override
    public void update(Observable observed, Object gameName) {
        if (observed instanceof Game && gameName instanceof String) {
            ScoreBoard scoreboardUsed = scoreboards.get(gameName);
            Game receivedGame = (Game) observed;
            int score = Objects.requireNonNull(scoreboardUsed).calculateScore(receivedGame.getSettings());
            scoreboardUsed.addNewScore(currentPlayer.getUsername(), score);
        }
    }
}
