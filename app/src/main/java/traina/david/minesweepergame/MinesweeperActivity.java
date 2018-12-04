package traina.david.minesweepergame;

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
import traina.david.slidingtilegame.R;

public class MinesweeperActivity extends AppCompatActivity implements Observer {

    /**
     * The minesweeper game controller.
     */
    private MinesweeperGame minesweeperGame;

    /**
     * The list of cell buttons.
     */
    private ArrayList<Button> cellButtons;

    /**
     * the current GlobalCenter used to track all Game Center data
     */
    private final GlobalCenter globalCenter = GlobalManager.getGlobalCenter();

    /**
     * the current LocalGameCenter used to track the current users data
     */
    private final LocalGameCenter localCenter = globalCenter.getLocalGameCenter(globalCenter.
            getCurrentPlayer().getUsername());

    /**
     * The grid view and calculated column height and width based on device size
     */
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);
        setupGame();
        createButtons(this);
        setupGridView();
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    private void display() {
        updateCellButtons();
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateCellButtons() {
        Grid grid = minesweeperGame.getGrid();
        int nextPos = 0;
        for (Button b : cellButtons) {
            int row = nextPos / grid.getGridSize();
            int col = nextPos % grid.getGridSize();
            Cell cell = grid.getCellAt(col, row);
            if(cell.isRevealed()) {
                b.setBackgroundResource(cell.getImageId());
            }else if(cell.isFlagged()){
                b.setBackgroundResource(R.drawable.flag);
            } else{
                b.setBackgroundResource(cell.getBlankImageID());
            }
            nextPos++;
        }
    }

    /**
     * sets up the GridView for the Minesweeper game
     */
    private void setupGridView() {
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(minesweeperGame.getGrid().getGridSize());
        gridView.setGame(minesweeperGame);

        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / minesweeperGame.getGrid().getGridSize();
                        columnHeight = displayHeight / minesweeperGame.getGrid().getGridSize();

                        display();
                    }
                });
    }

    /**
     * sets up the current minesweeper game and adds the correct observers.
     */
    private void setupGame(){
        minesweeperGame = (MinesweeperGame) localCenter.loadGame(localCenter.AUTOSAVE_SAVE_SLOT);
        minesweeperGame.turnOnAutoSave();
        minesweeperGame.addObserver(globalCenter);
        minesweeperGame.getGrid().addObserver(this);
    }

    /**
     * Create the buttons for displaying the cells.
     *
     * @param context the context
     */
    private void createButtons(Context context) {
        Grid grid = minesweeperGame.getGrid();
        cellButtons = new ArrayList<>();
        for (int row = 0; row != minesweeperGame.getGrid().getGridSize(); row++) {
            for (int col = 0; col != minesweeperGame.getGrid().getGridSize(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(grid.getCellAt(row, col).getBlankImageID());
                this.cellButtons.add(tmp);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalManager.saveAll(getApplicationContext());
        minesweeperGame.turnOffAutoSave();
    }

    @Override
    public void update(Observable observable, Object o) {
        display();
    }
}
