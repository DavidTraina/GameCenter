package traina.david.gamecenter;

import android.content.Context;
import android.util.Log;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class GlobalManager {

    /**
     * the global center containing data for the entire GameCenter.
     */
    private static GlobalCenter globalCenter;

    public static GlobalCenter getGlobalCenter() {
        return globalCenter;
    }

    /**
     * loading the global center, which is the global database.
     */
    public static void loadGlobalCenter(Context context) {
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new FileInputStream(context.getFilesDir() + "/savefile.ser"));
            globalCenter = (GlobalCenter) in.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            globalCenter = new GlobalCenter();
            saveAll(context);
        } catch (ClassNotFoundException e) {
            Log.e("setup global center", "Class not found: " + e.toString());
        } catch (IOException e) {
            if(e instanceof EOFException){
                globalCenter = new GlobalCenter();
                saveAll(context);
            }
            Log.e("setup global center", "IOException: " + e.toString());
        }
    }

    /**
     *
     * @param context the current context for the app
     */
    public static void saveAll(Context context) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
                    new File(context.getFilesDir()+ "/savefile.ser")));
            out.writeObject(globalCenter);
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("Save global center", "file not found" + e.toString() +"\n" + globalCenter);
        } catch (IOException e) {
            Log.e("Save global center", "IO exception" + e.toString() +"\n" + globalCenter);

        }

    }
}
