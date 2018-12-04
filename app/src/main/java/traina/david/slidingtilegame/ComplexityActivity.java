package traina.david.slidingtilegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.gamecenter.LocalGameCenter;

/**
 * The settings for SlidingTileGame activity.
 */
public class ComplexityActivity extends AppCompatActivity {

    private SlidingTileGame game;
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
        setContentView(R.layout.complexity);
    }

    /**
     * an onclick method for every new game button.
     * PRECONDITION: the last character of every button is the number or rows/columns the board
     * should have if that button is clicked
     * @param v the current view
     */
    public void onClick(View v) {
        Button b = (Button) v;
        String buttonText = b.getText().toString();
        Integer complexity = Integer.parseInt(String.valueOf
                (buttonText.charAt(buttonText.length() - 1)));
        switchToGame(complexity);
    }

    /**
     * a method to get the number of undos a user has set in the undo steps input
     * @param v the current view
     */
    public void setUndos(View v) {
        EditText UndoSteps = findViewById(R.id.undo_steps_input);
        String steps = UndoSteps.getText().toString();
        if (!steps.equals("")) {
            game.setUndoStep(Integer.parseInt(steps));
            Toast.makeText(getApplicationContext(), "Successful setting",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Enter Something",
                    Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Creates the correct size board for the game then swaps to it
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame(Integer complexity) {
        game = new SlidingTileGame(complexity);
        localCenter.saveGame(game.getSettings(), localCenter.AUTOSAVE_SAVE_SLOT);
        Intent tmp = new Intent(this, GameActivity.class);
        startActivity(tmp);
    }
}
