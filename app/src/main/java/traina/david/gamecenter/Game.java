package traina.david.gamecenter;

import java.util.List;
import java.util.Observable;

public abstract class Game extends Observable {

    /**
     * the GlobalCenter for the app. used to track all information in the GameCenter
     */
    private final GlobalCenter globalCenter = GlobalManager.getGlobalCenter();

    /**
     * The current users GameCenter. Used to track all their game info
     */
    private final LocalGameCenter localCenter = globalCenter.getLocalGameCenter(globalCenter.
            getCurrentPlayer().getUsername());

    /**
     * A private thread to be used for auto-saving while the game is played
     */
    private Thread t;

    /**
     *
     * @return The list of settings describing the game state.
     */
    public abstract List<?> getSettings();

    /**
     * A method that opens a new background thread and saves the game to the auto-save slot
     * every 15 seconds.
     */
    public void turnOnAutoSave() {
        t = new Thread(new Runnable() {
            public void run() {
                while (!t.isInterrupted()) {
                    localCenter.saveGame(getSettings(),
                            localCenter.AUTOSAVE_SAVE_SLOT);
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        t.interrupt();
                    }
                }
            }
        });
        t.start();
    }

    /**
     * a method that interrupts the thread created for auto-save.
     */
    public void turnOffAutoSave(){
        t.interrupt();
    }
}
