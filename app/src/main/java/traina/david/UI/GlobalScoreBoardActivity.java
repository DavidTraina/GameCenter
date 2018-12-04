package traina.david.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.slidingtilegame.R;

/**
 * Displays the top 10 scores for the current game across all users.
 */
public class GlobalScoreBoardActivity extends AppCompatActivity {

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
        String currentGameName = globalCenter.getLocalGameCenter(
                globalCenter.getCurrentPlayer().getUsername()).getCurrentGameName();
        ArrayList<ArrayList<Object>> topGlobalScores =
                globalCenter.getScoreBoard(currentGameName).getTopGlobalScores();

        for (int i = 0; i < scores.length; i++) {
            String user;
            String score;
            try {
                user = (String) topGlobalScores.get(i).get(0);
                score = "Score: " + topGlobalScores.get(i).get(1);
            } catch (IndexOutOfBoundsException e) {
                user = "Empty";
                score = "";
            }
            ((TextView) findViewById(users[i])).setText(user);
            ((TextView) findViewById(scores[i])).setText(score);
        }
    }
}
