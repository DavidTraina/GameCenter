package traina.david.gamecenter;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Abstract Class ScoreBoard that keeps the top 10(default) scores for each user
 * Keeps the top 10 scores of the Game among every users (A global board)
 * New scores can be added to ScoreBoard and topScores and topUserScores will change accordingly.
 */

public abstract class ScoreBoard implements Serializable {

    /**
     * An ArrayList containing the top scores from every user in the GlobalCenter
     */
    private final ArrayList<ArrayList<Object>> topGlobalScores = new ArrayList<>();

    /**
     * An ArrayList containing the top scores from an individual user.
     */
    private final HashMap<String, Integer[]> topUserScores = new HashMap<>();

    /**
     * Add a new score to both topUserScores and topGlobalScores(with a call from helper function
     * updateTopScores).
     * Create the array of scores if this is the first score of the user
     * The score will only be added if its among the highest scores that user achieve
     * @param userName which user the score belongs to
     * @param score the score needed to be add
     */
    void addNewScore(String userName, Integer score) {
        final int MAX_SCORES_PER_USER = 10;
        Integer[] userScores;
        if (topUserScores.containsKey(userName)) {
            userScores = topUserScores.get(userName);
            for (int i = 0; i < MAX_SCORES_PER_USER; i++) {
                if (Objects.requireNonNull(userScores)[i] == null || score > userScores[i]) {
                    // Insert score into userScores, removing lowest score (sorted high to low).
                    System.arraycopy(userScores, i, userScores, i + 1, userScores.length - 1 - i);
                    userScores[i] = score;
                    break;
                }
            }
        } else {
            userScores = new Integer[MAX_SCORES_PER_USER];
            userScores[0] = score;
            topUserScores.put(userName, userScores);
        }
        updateTopScores(userName, score);
    }

    /**
     * Return a list of Integer of the top 10 scores for the user userName.
     * @param userName The user you want the scores of.
     * @return a list of Integer of the top 10 scores for the user userName.
     */
    public Integer[] getUserScores(String userName) {
        return topUserScores.get(userName);
    }

    /**
     * Return the top 10 scores as a 2D ArrayList in the format: [String username, Integer score].
     * @return the top 10 scores as a 2D ArrayList in the format: [String username, Integer score]
     */
    public ArrayList<ArrayList<Object>> getTopGlobalScores() {
        return topGlobalScores;
    }

    /**
     * Insert score and associated userName into topGlobalScores if score qualifies in the top 10.
     * @param userName the username associates with the new score.
     * @param score the new score to possibly insert.
     */
    private void updateTopScores(String userName, Integer score) {
        final int MAX_GLOBAL_SCORES = 10;
        ArrayList<Object> newEntry = new ArrayList<Object>(Arrays.asList(userName, score));
        if (topGlobalScores.size() == 0) {
            topGlobalScores.add(newEntry);
        } else {
            for (int i = 0; i < MAX_GLOBAL_SCORES; i++) {
                if(i > topGlobalScores.size() - 1){
                    topGlobalScores.add(i, newEntry);
                    break;
                } else if (score > (Integer) topGlobalScores.get(i).get(1)) {
                    topGlobalScores.add(i, newEntry);
                    if (topGlobalScores.size() > MAX_GLOBAL_SCORES) {
                        topGlobalScores.remove(topGlobalScores.size() - 1);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Implement a scoring system for the game.
     * @param settings The settings for the game, which are needed to calculate the score.
     * @return Score for the game.
     */
    abstract int calculateScore(List<?> settings);
}

