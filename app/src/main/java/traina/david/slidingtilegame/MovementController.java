package traina.david.slidingtilegame;

import android.content.Context;
import android.widget.Toast;

import traina.david.gamecenter.Controller;
import traina.david.gamecenter.Game;


public class MovementController extends Controller {

    /**
     * The SlidingTileGame that the Movement Controller will controller
     */
    private SlidingTileGame slidingTileGame = null;

    public MovementController() {
    }

    /**
     * A setter for slidingTileGame
     *
     * @param slidingTileGame a SlidingTileGame for the MovementController to control
     */
    public void setGame(Game slidingTileGame) {
        this.slidingTileGame = (SlidingTileGame) slidingTileGame;
    }


    /**
     * How the MovementController should react to a tap movement. It checks first if the move is
     * valid or an undo then makes it if so. After the move it checks if the game has been solved.
     * If the move is an undo it checks to make sure that the user has enough undos left before
     * making the undo. If it is neither an undo or a valid tap it makes toast say invalid tap.
     *
     * @param context  the current app context
     * @param position the position of the tap in GridView
     */
    public void processTapMovement(Context context, int position) {
        if (slidingTileGame.isValidTap(position)) {
            slidingTileGame.touchMove(position);
            if (slidingTileGame.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_LONG).show();
            }
        } else if (slidingTileGame.isUndoTap(position)) {
            if (slidingTileGame.getUndoListSize() == 0)
                Toast.makeText(context, "No more undo", Toast.LENGTH_SHORT).show();
            else {
                slidingTileGame.undo();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void processLongTap(Context context, int position) {

    }
}
