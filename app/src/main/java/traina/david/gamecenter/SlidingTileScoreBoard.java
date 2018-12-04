package traina.david.gamecenter;


import java.io.Serializable;
import java.util.List;

import traina.david.slidingtilegame.Board;

/**
 * The SlidingTileScoreBoard
 */

class SlidingTileScoreBoard extends ScoreBoard implements Serializable {

    /**
     * calculate scores based on settings
     *
     * @param settings the boardSize at index 0 and moves at index 1 of slidingTileGame
     * @return score based on settings
     * Precondition: boardSize need to be at index 0 and number of moves took should at index 1
     */

    @Override
    public int calculateScore(List<?> settings) {
        int maxScore = 1000;
        Integer boardSize = ((Board) settings.get(0)).getNumRows();
        Integer moves = (Integer) settings.get(1);
        int roughScore = 0;
        if (boardSize.equals(5)) {
            roughScore = maxScore - moves;
        } else if (boardSize.equals(4)) {
            roughScore = maxScore - 3 * moves;
        } else if (boardSize.equals(3)) {
            roughScore = maxScore - 5 * moves;
        }
        if (roughScore > 300) {
            return roughScore;
        } else {
            return 300;
        }
    }
}