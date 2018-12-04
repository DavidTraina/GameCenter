package traina.david.gamecenter;

import android.content.Context;

import java.util.Observable;

public abstract class Controller extends Observable {

    /**
     * how the controller handles a tap on the screen
     * @param context the current app context
     * @param position the position of the tap in GridView
     */
    public abstract void processTapMovement(Context context, int position);

    /**
     * How the controller handles a long tap on the screen
     * @param context the current app context
     * @param position the position of the tap in GridView
     */
    public abstract void processLongTap(Context context, int position);

    /**
     * a setting for the game of the controller
     * @param game the current game
     */
    public abstract void setGame(Game game);
}
