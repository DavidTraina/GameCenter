package traina.david.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.gamecenter.LocalGameCenter;
import traina.david.slidingtilegame.R;

import static traina.david.gamecenter.GlobalCenter.MINESWEEPER_GAME_NAME;
import static traina.david.gamecenter.GlobalCenter.SLIDING_TILE_GAME_NAME;
import static traina.david.gamecenter.GlobalCenter.SUDOKU_GAME_NAME;

/**
 * The activity that allows the user to add or remove game.
 */
public class GameListActivity extends AppCompatActivity {

    /**
     * A bool used to flag if the user would like to add game(true) or delete game(false)
     */
    private boolean AddGame;
    /**
     * the current GlobalCenter used to track all GameCenter data
     */
    private final GlobalCenter globalCenter = GlobalManager.getGlobalCenter();

    /**
     * The current LocalGameCenter used to track the current users data.
     */
    private final LocalGameCenter localCenter = globalCenter.getLocalGameCenter(globalCenter.
            getCurrentPlayer().getUsername());

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AddGame = getIntent().getBooleanExtra("AddGame?", true);
        setContentView(R.layout.game_list);
        addSlidingTileButtonListener();
        addSudokuButtonListener();
        addMinesweeperButtonListener();
    }

    /**
     * Sets the visibility of the game button related to the game identified by GameName to be
     * appropriate for when the user is trying to add games
     *
     * @param gameName the name of the game that was clicked
     *      on in camel case followed by Game. eg. slidingTileGame
     */
    private void setAddVisibility(String gameName) {
        int resID = getResources().getIdentifier(gameName,
                "id", getPackageName());
        if (localCenter.hasGame(gameName)) {
            Button gameButton = findViewById(resID);
            gameButton.setVisibility(View.GONE);
        }
    }

    /**
     * Set the visibility of the game button related to gameName to the appropriate state for if the
     * user is trying to delete games.
     *
     * @param gameName the name of the game that was clicked
     *      *                 on in camel case followed by Game. eg. slidingTileGame
     */
    private void setRemoveVisibility(String gameName) {
        int resID = getResources().getIdentifier(gameName,
                "id", getPackageName());
        if (!localCenter.hasGame(gameName)) {
            Button gameButton = findViewById(resID);
            gameButton.setVisibility(View.GONE);
        }
    }

    /**
     * Activate the SlidingTile button and sets the correct visibility based on if the user is
     * adding or deleting games and already has Sliding Tile.
     */
    private void addSlidingTileButtonListener() {
        Button slidingTileButton = findViewById(R.id.slidingTileGame);
        slidingTileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGame(SLIDING_TILE_GAME_NAME);
            }
        });
        if (AddGame) {
            setAddVisibility(SLIDING_TILE_GAME_NAME);
        } else {
            setRemoveVisibility(SLIDING_TILE_GAME_NAME);
        }
    }

    /**
     * activates the Sudoku button and set the correct visibility based on if the user is deleting
     * or adding games and if they already have sudoku as a game.
     */
    private void addSudokuButtonListener(){
        Button slidingTileButton = findViewById(R.id.sudokuGame);
        slidingTileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGame(SUDOKU_GAME_NAME);
            }
        });
        if (AddGame) {
            setAddVisibility(SUDOKU_GAME_NAME);
        } else {
            setRemoveVisibility(SUDOKU_GAME_NAME);
        }
    }

    /**
     * activates the Minesweeper Button and sets the correct visibility based on if the user is
     * adding or deleting games and already has minesweeper as a game.
     */
    private void addMinesweeperButtonListener(){
        Button slidingTileButton = findViewById(R.id.minesweeperGame);
        slidingTileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGame(MINESWEEPER_GAME_NAME);
            }
        });
        if (AddGame) {
            setAddVisibility(MINESWEEPER_GAME_NAME);
        } else {
            setRemoveVisibility(MINESWEEPER_GAME_NAME);
        }
    }

    /**
     * a method to determine if the game should be added or deleted from LocalGameCenter then take
     * the appropriate action (delete or add it).
     * @param gameName the name of the game that was clicked
     *                 on in camel case followed by Game. eg. slidingTileGame
     */
    private void addGame(String gameName) {
        if (AddGame) {
            localCenter.addGame(gameName);
            GlobalManager.saveAll(getApplicationContext());
            setAddVisibility(gameName);
        } else {
            localCenter.removeGame(gameName);
            GlobalManager.saveAll(getApplicationContext());
            setRemoveVisibility(gameName);
        }
    }
}
