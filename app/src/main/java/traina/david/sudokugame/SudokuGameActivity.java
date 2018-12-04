package traina.david.sudokugame;

import android.content.Context;
import android.graphics.Color;
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

/**
 * The game activity.
 */
public class SudokuGameActivity extends AppCompatActivity implements Observer {

    private SudokuGame sudokuGame;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> cellButtons;

    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView gridView;
    private static int columnWidth, rowHeight;

    private GlobalCenter globalCenter;
    private LocalGameCenter localCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_game);
        globalCenter = GlobalManager.getGlobalCenter();
        localCenter = globalCenter.getLocalGameCenter(globalCenter.
                getCurrentPlayer().getUsername());

        setupGame();
        createTileButtons(this);
        setupGridView();
    }

    /**
     * Loads a new/saved game state, adds the game as an Observable by globalCenter (for scoring
     * purposes), adds the grid as an Observable by this class (for displaying onto the screen)
     */
    private void setupGame() {
        sudokuGame = (SudokuGame) localCenter.loadGame(localCenter.AUTOSAVE_SAVE_SLOT);
        sudokuGame.turnOnAutoSave();
        sudokuGame.addObserver(globalCenter);
        sudokuGame.getSudokuGrid().addObserver(this);
    }

    /**
     * Creates buttons arranged in a 11x9 grid on the screen. The first 9 are for inputs, the next
     * 9 is for clear, and the last 81 correspond to the Sudoku Grid, in row-major order.
     *
     * @param context - the context
     */
    private void createTileButtons(Context context) {
        SudokuGrid grid = sudokuGame.getSudokuGrid();
        cellButtons = new ArrayList<>();

        for (int row = 0; row != SudokuGrid.GRID_SIZE + 2; row++) {
            for (int col = 0; col != SudokuGrid.GRID_SIZE; col++) {
                Button tmp = new Button(context);
                if (row == 0) {
                    tmp.setBackgroundColor(Color.argb(255, 255, 153, 51));
                    tmp.setText(String.valueOf(col + 1));
                } else if (row == 1) {
                    if (col == 0) {
                        tmp.setText("Undo");
                    } else if (col == 8) {
                        tmp.setText("Clear");
                    } else {
                        tmp.setText("");
                        tmp.setClickable(false);
                    }
                    tmp.setBackgroundColor(Color.argb(100, 255, 255, 255));
                } else {
                    SudokuCell cell = grid.getSudokuCell(row - 2, col);
                    if (!cell.isMutable()) {
                        tmp.setBackgroundColor(Color.argb(100, 179, 224, 255));
                        tmp.setText(String.valueOf(cell.getSolvedId()));
                        tmp.setClickable(false);
                    } else {
                        tmp.setBackgroundColor(Color.argb(100, 230, 230, 230));
                        tmp.setText("");
                    }
                }
                this.cellButtons.add(tmp);
            }
        }
    }

    /**
     * Sets up the GridView - sets the size and arrangement of the buttons to squares in a 11x9 grid
     */
    private void setupGridView() {
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(SudokuGrid.GRID_SIZE);
        gridView.setGame(sudokuGame);

        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        columnWidth = displayWidth / SudokuGrid.GRID_SIZE;
                        rowHeight = displayWidth / SudokuGrid.GRID_SIZE;

                        display();
                    }
                });
    }

    private void display() {
        updateCellButtons();
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, rowHeight));
    }

    /**
     * Checks the state of the last 81 buttons and
     * (1) Changes their color depending on whether cells have conflicts
     * (2) Changes their number according to the input they receive.
     */
    private void updateCellButtons() {
        SudokuGrid grid = sudokuGame.getSudokuGrid();
        int cellPos = 0;
        int cellRow;
        int cellCol;
        for (Button button : cellButtons.subList(18, cellButtons.size())) {
            cellRow = SudokuGrid.getCellRow(cellPos);
            cellCol = SudokuGrid.getCellCol(cellPos);
            SudokuCell cell = grid.getSudokuCell(cellRow, cellCol);
            if (cell.isMutable()) {
                if (cell.getCurrentId() == -1) {
                    button.setText("");
                } else {
                    button.setText(String.valueOf(cell.getCurrentId()));
                }
            }
            if (cell.isConflicting()) {
                button.setTextColor(Color.argb(255, 255, 0, 0));
            } else {
                button.setTextColor(Color.argb(255, 0, 0, 0));
            }
            cellPos++;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalManager.saveAll(getApplicationContext());
        sudokuGame.turnOffAutoSave();
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }
}
