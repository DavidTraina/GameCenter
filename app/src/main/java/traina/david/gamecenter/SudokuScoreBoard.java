package traina.david.gamecenter;

import java.util.List;

class SudokuScoreBoard extends ScoreBoard {
    @Override
    int calculateScore(List<?> settings) {
        int difficulty = (Integer) settings.get(1);
        int wrongMoves = (Integer) settings.get(2);
        if (difficulty == 0){
            return 100 - 5*wrongMoves;
        } else if (difficulty == 1){
            return 200 - 5*wrongMoves;
        } else {
            return 300 - 5*wrongMoves;
        }
    }
}
