package traina.david.gamecenter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * Stores game saving slot for each specific game and its' settings, monitoring current
 * running game, in charge of saving game state, loading game, adding new game, removing a game
 * and auto-save.
 */

public class LocalGameCenter extends Observable implements Serializable {

    private static final long serialVersionUID = 4444L;
    /**
     * The maximum number of save slots available per game. Not including the Auto-save slot.
     */
    private final int MAX_SAVE_SLOTS_PER_GAME = 3;

    /**
     * The save slot used for auto-save.
     */
    public final int AUTOSAVE_SAVE_SLOT = 0;

    /**
     * The container for storing a game correlate to the game settings in its save slot.
     */
    private final Map<String, LinkedList<List<?>>> localGames;


    /**
     * the name of the currently active game. this should always be a key in the localGames map
     */
    private String currentGameName;


    /**
     * construct the local center, add observer to it.
     *
     * @param o the observer
     */
    LocalGameCenter(Observer o) {
        this.addObserver(o);
        localGames = new HashMap<>();
    }

    /**
     * A getter for the currently active game name
     */
    public String getCurrentGameName() {
        return currentGameName;
    }

    /**
     * A setter for the currently active game name
     */
    public void setCurrentGameName(String gameName) {
        currentGameName = gameName;
    }


    /**
     * Adding a new game to the game list of to the user.
     *
     * @param gameName the name of the game.
     */
    public void addGame(String gameName) {
        if (!this.hasGame(gameName)) {
            localGames.put(gameName, new LinkedList<List<?>>() {
            });
            for (int i = 0; i <= MAX_SAVE_SLOTS_PER_GAME; i++) {
                Objects.requireNonNull(localGames.get(gameName)).add(null);
            }
        }
    }

    /**
     * Return whether this local center has the game
     *
     * @param gameName the name of the game
     * @return whether the center has the game
     */

    public boolean hasGame(String gameName) {
        return localGames.containsKey(gameName);
    }

    /**
     * removing a game from the game list of to the user.
     *
     * @param gameName the name of the game.
     */
    public void removeGame(String gameName) {
        localGames.remove(gameName);
    }


    /**
     * save the settings to local center
     *
     * @param settings the game settings
     * @param gameSlot the slot no.
     */
    public void saveGame(List<?> settings, int gameSlot) {
        Objects.requireNonNull(localGames.get(currentGameName)).set(gameSlot, settings);
    }

    /**
     * Loads a the game stored in the save slot specified by gameSlot.
     * Also sets the loaded game as the currently active game.
     * Afterwards, returns an instance of the game.
     *
     * @param gameSlot: int. the save slot from which to load.
     * @return Game: an instance of the saved game, consistent with saved settings.
     */
    public Game loadGame(int gameSlot) {
        GameFactory gameFactory = new GameFactory();
        return gameFactory.getGame(currentGameName, Objects.requireNonNull(localGames.get(currentGameName)).get(gameSlot));
    }
}