package traina.david.UI;

import android.content.Intent;
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
 * The activity that allows the user choose the game to play and choose whether they want to add
 * or remove game.
 */
public class LocalGameCenterActivity extends AppCompatActivity {

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
        setContentView(R.layout.local_game_center);
        addAddGameButtonListener();
        addDeleteGameButtonListener();
        addSignOutButtonListener();
        addSlidingTileButtonListener();
        addSudokuButton();
        addMinesweeperButton();
    }

    @Override
    public void onResume(){
        super.onResume();
        setButtonVisibility(SLIDING_TILE_GAME_NAME);
        setButtonVisibility(SUDOKU_GAME_NAME);
        setButtonVisibility(MINESWEEPER_GAME_NAME);
    }

    /**
     * determines if the users LocalCenter has the game identified by gameName then sets the
     * button for that game to visible if they do or not visible if they do not.
     *
     * @param gameName the name of the game followed by Game. eg. sudokuGame
     */
    private void setButtonVisibility(String gameName) {
        int resID = getResources().getIdentifier(gameName,
                "id", getPackageName());
        if (localCenter.hasGame(gameName)) {
            Button gameButton = findViewById(resID);
            gameButton.setVisibility(View.VISIBLE);
        } else {
            Button gameButton = findViewById(resID);
            gameButton.setVisibility(View.GONE);
        }
    }

    /**
     * Activate the add button.
     */
    private void addAddGameButtonListener() {
        Button addGameButton = findViewById(R.id.add_game);
        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchGameList(true);
            }
        });
    }

    /**
     * Activate the delete button.
     */
    private void addDeleteGameButtonListener() {
        Button deleteGameButton = findViewById(R.id.delete_game);
        deleteGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchGameList(false);
            }
        });
    }

    /**
     * Switch to the GameListActivity view to add or remove.
     */
    private void switchGameList(boolean addGame) {
        Intent tmp = new Intent(this, GameListActivity.class);
        tmp = tmp.putExtra("AddGame?", addGame);
        startActivity(tmp);
    }

    /**
     * Activate the signOut button.
     */
    private void addSignOutButtonListener() {
        Button signOutButton = findViewById(R.id.sign_out);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    /**
     * Switch to the Global view for logging out.
     */
    private void logOut() {
        globalCenter.signOut();
        Intent tmp = new Intent(this, Global.class);
        startActivity(tmp);
        finish();
    }

    /**
     * Activate the slidingTileGame button and sets the currentGameName to the constant for
     * sliding tiles
     */
    private void addSlidingTileButtonListener() {
        Button slidingTileButton = findViewById(R.id.slidingTileGame);
        slidingTileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localCenter.setCurrentGameName(SLIDING_TILE_GAME_NAME);
                switchToStarting();
            }
        });
    }

    /**
     * Activate the sudokuGame button and sets the CurrentGameName to the constant for Sudoku
     */
    private void addSudokuButton(){
        Button sudokuButton = findViewById(R.id.sudokuGame);
        sudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localCenter.setCurrentGameName(SUDOKU_GAME_NAME);
                switchToStarting();
            }
        });
    }

    /**
     * Activate the MinesweeperGame button and sets the CurrentGameName to the constant
     * for Minesweeper
     */
    private void addMinesweeperButton(){
        Button MinesweeperButton = findViewById(R.id.minesweeperGame);
        MinesweeperButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localCenter.setCurrentGameName(MINESWEEPER_GAME_NAME);
                switchToStarting();
            }
        });
    }

    /**
     * Switch to the Starting view to initialize the game.
     */
    private void switchToStarting() {
        Intent tmp = new Intent(this, StartingActivity.class);
        startActivity(tmp);
    }
}

