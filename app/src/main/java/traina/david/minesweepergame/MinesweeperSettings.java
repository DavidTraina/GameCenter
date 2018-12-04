package traina.david.minesweepergame;

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
import traina.david.slidingtilegame.R;

public class MinesweeperSettings extends AppCompatActivity {

    /**
     * the current GlobalCenter used to track all GameCenter data
     */
    private final GlobalCenter globalCenter = GlobalManager.getGlobalCenter();

    /**
     * the current LocalGameCenter used to track the current users data
     */
    private final LocalGameCenter localCenter = globalCenter.getLocalGameCenter(globalCenter.
            getCurrentPlayer().getUsername());
    /**
     * The gridSize of the minesweeper game to be made
     */
    private Integer gridSize;
    /**
     * the number of bombs in the minesweeper game to be made. set to 3 by default
     */
    private Integer bombs = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper_settings);
    }

    /**
     * an onclick method for every newGame button.
     * PRECONDITION: the last character of every button is the number or rows/columns the board
     * should have if that button is clicked
     * @param v the current view
     */
    public void onClick(View v) {
        Button b = (Button) v;
        String buttonText = b.getText().toString();
        gridSize = Integer.parseInt(String.valueOf
                (buttonText.charAt(buttonText.length() - 1)));
        if(!gridSize.equals(8)){
            gridSize += 10;
        }

        switchToGame();
    }

    /**
     * an onclick method for the set bombs button that gets the current text in the number of bombs
     * field and sets numBombs equal to this.
     * @param v the current view
     */
    public void setBombs(View v){
        EditText bombInput = findViewById(R.id.bombs_input);
        String bombText = bombInput.getText().toString();
        if (!bombText.equals("")) {
            bombs = Integer.parseInt(bombText);
            if (bombs >= 50) {
                bombs = 49;
                Toast.makeText(getApplicationContext(), "Too many bombs, please enter a number" +
                                " less than 50",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Successful setting",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Enter Something",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Creates the minesweeper game with the correct gridSize and number of bombs then swaps to
     * the minesweeper game
     */
    private void switchToGame() {
        MinesweeperGame minesweeperGame = new MinesweeperGame(gridSize, bombs);
        localCenter.saveGame(minesweeperGame.getSettings(), localCenter.AUTOSAVE_SAVE_SLOT);
        Intent tmp = new Intent(this, MinesweeperActivity.class);
        startActivity(tmp);
    }
}
