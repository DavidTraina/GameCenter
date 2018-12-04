package traina.david.sudokugame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.gamecenter.LocalGameCenter;
import traina.david.slidingtilegame.R;

public class SudokuSettingsActivity extends AppCompatActivity {

    /**
     * the current GlobalCenter used to track all GameCenter data
     */
    private final GlobalCenter globalCenter = GlobalManager.getGlobalCenter();

    /**
     * the current LocalGameCenter used to track the current users data
     */
    private final LocalGameCenter localCenter = globalCenter.getLocalGameCenter(globalCenter.
            getCurrentPlayer().getUsername());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_settings);
    }

    /**
     * Launches the SudokuGameActivity based on the button clicked
     * @param v - the current view
     */
    public void onClick(View v) {
        Button b = (Button) v;
        String buttonText = b.getText().toString();
        Integer difficulty = 0;
        switch(buttonText){
            case("Hard"):
                difficulty = 2;
                break;
            case("Medium"):
                difficulty = 1;
                break;
            case("Easy"):
                difficulty = 0;
                break;
        }
        switchToGame(difficulty);
    }

    /**
     * Creates a new game and launches it
     *
     * @param difficulty - difficulty setting for the sudokuGame.
     */
    private void switchToGame(Integer difficulty) {
        SudokuGame game = new SudokuGame(difficulty);
        localCenter.saveGame(game.getSettings(), localCenter.AUTOSAVE_SAVE_SLOT);
        Intent tmp = new Intent(this, SudokuGameActivity.class);
        startActivity(tmp);
    }

}
