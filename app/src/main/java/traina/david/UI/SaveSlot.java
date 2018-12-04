package traina.david.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import traina.david.gamecenter.Game;
import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.gamecenter.LocalGameCenter;
import traina.david.minesweepergame.MinesweeperActivity;
import traina.david.slidingtilegame.GameActivity;
import traina.david.slidingtilegame.R;
import traina.david.sudokugame.SudokuGameActivity;

public class SaveSlot extends AppCompatActivity {

    /**
     * the current GlobalCenter used to track all GameCenter data
     */
    private final GlobalCenter globalCenter = GlobalManager.getGlobalCenter();

    /**
     * The current LocalGameCenter used to track the current users data.
     */
    private final LocalGameCenter localCenter = globalCenter.getLocalGameCenter(globalCenter.
            getCurrentPlayer().getUsername());
    /**
     * true if user is saving a game, false if the user is loading a game
     */
    private Boolean save;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_games);
        save = getIntent().getBooleanExtra("save?", true);

        if (save) {
            Button autoSaveButton = findViewById(R.id.autosave);
            autoSaveButton.setVisibility(View.GONE);
        } else {
            Button autoSaveButton = findViewById(R.id.autosave);
            autoSaveButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * the onclick method for the auto-save button
     */
    public void autoSaveClick(View v) {
        saveOrLoad(localCenter.AUTOSAVE_SAVE_SLOT);
    }


    /**
     * activates the saveSlot buttons. this method has been assigned to them by xml.
     * <p>
     * PRECONDITION: any button that is assigned this onclick method by xml has the last character
     * of it's text as the number that identifies it's save slot (this starts at 1 since slot 0 is
     * the autosave slot).
     *
     * @param v the current view
     */
    public void onClick(View v) {
        Button b = (Button) v;
        String buttonText = b.getText().toString();
        Integer slot = Integer.parseInt(String.valueOf
                (buttonText.charAt(buttonText.length() - 1)));
        saveOrLoad(slot);
    }

    /**
     * saves or loads a game from the identified slot based on if the user is trying to save or load
     * games.
     *
     * @param slot the slot (out of 3) that the user is trying to save to.
     */
    private void saveOrLoad(int slot) {
        if (save) {
            Game game = localCenter.loadGame(localCenter.AUTOSAVE_SAVE_SLOT);
            if (game != null) {
                localCenter.saveGame(game.getSettings(), slot);
                Toast.makeText(getApplicationContext(), "Game saved to slot " + slot,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Game loadedGame = localCenter.loadGame(slot);
            if (loadedGame != null) {
                localCenter.saveGame(loadedGame.getSettings(), localCenter.AUTOSAVE_SAVE_SLOT);
                Toast.makeText(getApplicationContext(), "Game loaded from slot " + slot,
                        Toast.LENGTH_SHORT).show();
                switchToGame();
            } else {
                Toast.makeText(getApplicationContext(), "No saved game found.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Switch to the GameActivity view to play the game. Checks with LocalGameCenter to see what the
     * current game is
     */
    private void switchToGame() {
        String currentGame = localCenter.getCurrentGameName();
        switch (currentGame) {
            case (GlobalCenter.SLIDING_TILE_GAME_NAME):
                Intent tmp = new Intent(this, GameActivity.class);
                startActivity(tmp);
                break;
            case (GlobalCenter.MINESWEEPER_GAME_NAME):
                Intent tmp1 = new Intent(this, MinesweeperActivity.class);
                startActivity(tmp1);
                break;
            case (GlobalCenter.SUDOKU_GAME_NAME):
                Intent tmp2 = new Intent(this, SudokuGameActivity.class);
                startActivity(tmp2);
        }
    }
}

