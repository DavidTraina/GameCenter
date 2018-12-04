package traina.david.gamecenter;

import java.io.Serializable;
import java.util.List;

import traina.david.minesweepergame.Grid;

/**
 * A scoreboard that calculates scores when receiving a solved minesweeper game.
 * Short elapsed times and a greater proportion of mines contribute to higher scores.
 *      Time translates into a score, based on a tiered system.
 *          < 10 seconds is a perfect score. (100)
 *          30 - 10 seconds is a shallow increase in score. (90-100)
 *          90 - 30 seconds is a noticeable increase in score. (
 *          > 90 seconds is a shallow drop. ( 90s = 30 )
 *      Bombs are a multiplier, with 20% of the board being covered by mines equating to a 100%
 *          multiplier. Scales linearly down to 0% at 0 mines, and upwards to 200% at 100% mines.
 */
class MinesweeperScoreBoard extends ScoreBoard implements Serializable {


    /**
     * The number of tenths of seconds that elapsed over the course of play.
     */
    private int elapsedTime;

    /**
     * Calculates score based on the size of the grid for the Minesweeper game, the number of
     * mines in play, and the amount of time that passed during play.
     *
     * @param settings The settings for the game, which are needed to calculate the score.
     * @return int: the player's score.
     */
    @Override
    int calculateScore(List<?> settings) {
        Grid grid = (Grid) settings.get(0);
        int gridSize = grid.getGridSize();
        int bombCount = grid.getNumBombs();
        int normalBombs = (gridSize * gridSize + 4) / 5;
        elapsedTime = (Integer) settings.get(1);

        double score = getFlatTimeScore();

        return getBombMultiplier(bombCount, normalBombs, score);
    }

    /**
     * Calculates the a flat score based on the elapsed time.
     * @return int: the player's performance based purely off of time.
     */
    private double getFlatTimeScore() {
        double score = 100;

        elapsedTime = elapsedTime - 100;

        if (elapsedTime < 200){
            while (elapsedTime > 0){
                elapsedTime = elapsedTime - 20;
                score--;
            }
        } else if (elapsedTime < 800){
            elapsedTime = elapsedTime - 200;
            score = 90;
            while (elapsedTime > 0){
                elapsedTime = elapsedTime - 10;
                score--;
            }
        } else {
            elapsedTime = elapsedTime - 800;
            score = 30;
            while (elapsedTime > 0){
                elapsedTime = elapsedTime - 30;
                score--;
            }
        }
        return score;
    }

    /**
     * Receives a score and applies modifiers based on the proportion of bombs to safety.
     * @param bombCount: int. The number of bombs in the grid.
     * @param normalBombs: int. A typical amount of bombs to put in the grid.
     * @param score: double. How well a player has performed without considering bombs or grid size.
     * @return int: the player's performance based after considering bomb proportion.
     */
    private int getBombMultiplier(int bombCount, int normalBombs, double score) {
        double modifier = score / normalBombs;

        if (bombCount < normalBombs){
            while (bombCount > 0){
                bombCount--;
                score = score - modifier;
            }
        } else if (bombCount > normalBombs){
            bombCount = bombCount - normalBombs;
            while (bombCount > 0){
                bombCount--;
                score = score + (modifier / 4);
            }
        }
        return (int) score;
    }
}
