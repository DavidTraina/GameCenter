package traina.david.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.slidingtilegame.R;

/**
 * Displays the top 10 scores for the current user.
 */
public class LocalScoreBoardActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_score_board);

        int[] scores = {
                R.id.no1,
                R.id.no2,
                R.id.no3,
                R.id.no4,
                R.id.no5,
                R.id.no6,
                R.id.no7,
                R.id.no8,
                R.id.no9,
                R.id.no10
        };
        int[] users = {
                R.id.usr1,
                R.id.usr2,
                R.id.usr3,
                R.id.usr4,
                R.id.usr5,
                R.id.usr6,
                R.id.usr7,
                R.id.usr8,
                R.id.usr9,
                R.id.usr10
        };
        GlobalCenter globalCenter = GlobalManager.getGlobalCenter();
        String currentPlayer = globalCenter.getCurrentPlayer().getUsername();
        String currentGameName = globalCenter.getLocalGameCenter(currentPlayer)
                .getCurrentGameName();
        Integer[] topLocalScores = globalCenter.getScoreBoard(currentGameName)
                .getUserScores(currentPlayer);

        for (int i = 0; i < scores.length; i++) {
            String user;
            String score;

            if (topLocalScores == null || topLocalScores[i] == null) {
                user = "Empty";
                score = "";
            } else {
                user = currentPlayer;
                score = "Score: " + topLocalScores[i];
            }
            ((TextView) findViewById(users[i])).setText(user);
            ((TextView) findViewById(scores[i])).setText(score);
        }
    }
}
