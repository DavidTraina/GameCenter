package traina.david.slidingtilegame;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import traina.david.UI.GestureDetectGridView;
import traina.david.gamecenter.CustomAdapter;
import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.gamecenter.LocalGameCenter;

/**
 * The game activity.
 */
public class GameActivity extends AppCompatActivity implements Observer {

    /**
     * The board manager.
     */
    private SlidingTileGame slidingTileGame;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * The grid view and calculated column height and width based on device size
     */
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

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
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    private void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupGame();
        createTileButtons(this);
        setupGridView();
    }

    /**
     * Sets up the GridView for the sliding tile game.
     */
    private void setupGridView() {
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(slidingTileGame.getBoard().getNumCols());
        gridView.setGame(slidingTileGame);

        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / slidingTileGame.getBoard().getNumCols();
                        columnHeight = displayHeight / slidingTileGame.getBoard().getNumRows();

                        display();
                    }
                });
    }

    /**
     * does the setup for the sliding tile game.
     */
    private void setupGame(){
        slidingTileGame = (SlidingTileGame) localCenter.loadGame(localCenter.AUTOSAVE_SAVE_SLOT);
        slidingTileGame.turnOnAutoSave();
        slidingTileGame.addObserver(globalCenter);
        slidingTileGame.getBoard().addObserver(this);
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = slidingTileGame.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != slidingTileGame.getBoard().getNumRows(); row++) {
            for (int col = 0; col != slidingTileGame.getBoard().getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = slidingTileGame.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / slidingTileGame.getBoard().getNumRows();
            int col = nextPos % slidingTileGame.getBoard().getNumCols();
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalManager.saveAll(getApplicationContext());
        slidingTileGame.turnOffAutoSave();
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }
}
