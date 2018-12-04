package traina.david.UI;

/*
Adapted from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/GestureDetectGridView.java

This extension of GridView contains built in logic for handling swipes between buttons
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

import traina.david.gamecenter.Controller;
import traina.david.gamecenter.Game;
import traina.david.gamecenter.GlobalCenter;
import traina.david.gamecenter.GlobalManager;
import traina.david.gamecenter.LocalGameCenter;
import traina.david.minesweepergame.GridController;
import traina.david.slidingtilegame.MovementController;
import traina.david.sudokugame.SudokuGridController;

import static traina.david.gamecenter.GlobalCenter.MINESWEEPER_GAME_NAME;
import static traina.david.gamecenter.GlobalCenter.SLIDING_TILE_GAME_NAME;
import static traina.david.gamecenter.GlobalCenter.SUDOKU_GAME_NAME;


public class GestureDetectGridView extends GridView {
    private static final int SWIPE_MIN_DISTANCE = 100;
    private GestureDetector gDetector;
    private Controller mController;
    private boolean mFlingConfirmed = false;
    private float mTouchX;
    private float mTouchY;

    /**
     * a constructor for GridView
     * @param context the current context
     */
    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    /**
     * initializes the grid view to have the correct controller based on the current game then
     * adds a gestureListener to the GridView with overridden methods for tap events
     * @param context the current context
     */
    private void init(final Context context) {
        GlobalCenter globalCenter = GlobalManager.getGlobalCenter();
        LocalGameCenter localCenter = globalCenter.getLocalGameCenter(globalCenter.
                getCurrentPlayer().getUsername());
        switch(localCenter.getCurrentGameName()){
            case(SLIDING_TILE_GAME_NAME):
                mController = new MovementController();
                break;
            case (SUDOKU_GAME_NAME):
                mController = new SudokuGridController();
                break;
            case(MINESWEEPER_GAME_NAME):
                mController = new GridController();
                break;
        }

        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                int position = GestureDetectGridView.this.pointToPosition
                        (Math.round(event.getX()), Math.round(event.getY()));

                mController.processTapMovement(context, position);
                return true;
            }

            @Override
            public void onLongPress(MotionEvent event) {
                if (mController instanceof GridController || mController instanceof SudokuGridController) {
                    int position = GestureDetectGridView.this.pointToPosition
                            (Math.round(event.getX()), Math.round(event.getY()));
                    mController.processLongTap(context, position);
                }
            }

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }

    /**
     * Passes the current game to the private controller for this GridView
     * @param game the current game
     */
    public void setGame(Game game) {
        mController.setGame(game);
    }
}
