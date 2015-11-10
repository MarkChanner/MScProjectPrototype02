package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class SoundManager {

    private SoundPool soundPool;

    private int invalidMoveID = -1;
    private int matchFoundID = -1;
    /**
     * variables to be used once sound files added
     */
    private int angryID = -1;
    private int delightedID = -1;
    private int embarrassedID = -1;
    private int surprisedID = -1;
    private int upsetID = -1;

    public void loadSound(Context context) {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Second parameter specifies priority of sound effect
            descriptor = assetManager.openFd("match_found.ogg");
            matchFoundID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("swap_back.ogg");
            invalidMoveID = soundPool.load(descriptor, 0);
        } catch (IOException e) {
            Log.e("Error", "sound file failed to load!");
        }
    }

    public void playSound(String sound) {
        switch (sound) {
            case "INVALID_MOVE":
                soundPool.play(invalidMoveID, 1, 1, 0, 0, 1);
                break;

            case "MATCH_FOUND":
                soundPool.play(matchFoundID, 1, 1, 0, 0, 1);
                break;

            /** Sound files yet to be made */
            case "angryID":
                soundPool.play(angryID, 1, 1, 0, 0, 1);
                break;

            case "delightedID":
                soundPool.play(delightedID, 1, 1, 0, 0, 1);
                break;

            case "embarrassedID":
                soundPool.play(embarrassedID, 1, 1, 0, 0, 1);
                break;

            case "surprisedID":
                soundPool.play(surprisedID, 1, 1, 0, 0, 1);
                break;

            case "upsetID":
                soundPool.play(upsetID, 1, 1, 0, 0, 1);
                break;
        }
    }
}
