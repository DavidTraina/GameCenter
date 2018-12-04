package traina.david.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.gamecenter.LocalGameCenter;
import traina.david.minesweepergame.MinesweeperSettings;
import traina.david.slidingtilegame.ComplexityActivity;
import traina.david.slidingtilegame.R;
import traina.david.sudokugame.SudokuSettingsActivity;

import static traina.david.gamecenter.GlobalCenter.MINESWEEPER_GAME_NAME;
import static traina.david.gamecenter.GlobalCenter.SLIDING_TILE_GAME_NAME;
import static traina.david.gamecenter.GlobalCenter.SUDOKU_GAME_NAME;

/**
 * The initial activity for all games.
 */
public class StartingActivity extends AppCompatActivity {

    /**
     * the current GlobalCenter used to track all GameCenter data
     */
    private final GlobalCenter globalCenter = GlobalManager.getGlobalCenter();

    /**
     * the current LocalGameCenter used to track the current users data
     */
    private final LocalGameCenter localCenter = globalCenter.getLocalGameCenter(globalCenter.
            getCurrentPlayer().getUsername());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_);

        updateTextView();
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addMyScoreButtonListener();
        addGlobalScoreButtonListener();
    }

    /**
     * dynamically update the TextView to match the correct game

     */
    private void updateTextView() {
        TextView textView = findViewById(R.id.GameText);
        String currentGameName = localCenter.getCurrentGameName();
        switch (currentGameName) {
            case (SLIDING_TILE_GAME_NAME):
                String tmp = getResources().getString(R.string.slidingTileStarting);
                textView.setText(tmp);
                break;
            case (SUDOKU_GAME_NAME):
                String tmp1 = getResources().getString(R.string.sudokuStarting);
                textView.setText(tmp1);
                break;
            case (MINESWEEPER_GAME_NAME):
                String tmp2 = getResources().getString(R.string.minesweeperStarting);
                textView.setText(tmp2);
                break;
        }
    }
    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame();
            }
        });
    }

    /**
     * Activate the myScore button.
     */

    private void addMyScoreButtonListener() {
        Button myScoreButton = findViewById(R.id.my_score_button);
        myScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLocalScore();
            }
        });
    }

    /**
     * Activate the myScore button.
     */

    private void addGlobalScoreButtonListener() {
        Button myScoreButton = findViewById(R.id.global_score_button);
        myScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGlobalScore();
            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(false);
            }
        });
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(true);
            }
        });
    }

    private void save(Boolean save) {
        Intent tmp = new Intent(this, SaveSlot.class);
        tmp.putExtra("save?", save);
        startActivity(tmp);
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame() {
        String currentGameName = localCenter.getCurrentGameName();
        switch (currentGameName) {
            case(SLIDING_TILE_GAME_NAME):
                Intent tmp = new Intent(this, ComplexityActivity.class);
                startActivity(tmp);
                break;
            case (SUDOKU_GAME_NAME):
                Intent tmp1 = new Intent(this, SudokuSettingsActivity.class);
                startActivity(tmp1);
                break;
            case(MINESWEEPER_GAME_NAME):
                Intent tmp2 = new Intent(this, MinesweeperSettings.class);
                startActivity(tmp2);
                break;
        }
    }

    /**
     * Switch to the Scoreboard view for individual scoreboard.
     */
    private void switchToLocalScore() {
        Intent tmp = new Intent(this, LocalScoreBoardActivity.class);
        startActivity(tmp);
    }

    /**
     * Switch to the Scoreboard view for individual scoreboard.
     */
    private void switchToGlobalScore() {
        Intent tmp = new Intent(this, GlobalScoreBoardActivity.class);
        startActivity(tmp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalManager.saveAll(getApplicationContext());
    }
}
