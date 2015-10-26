package com.markchanner.mscprojectprototype02;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class GameActivity extends Activity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sets to full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /** removed MatchFinderImpl parameter */
        GameView view = new GameView(this, new BoardPopulatorMock01());
        setContentView(view);

        mediaPlayer = new MediaPlayer();
        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor musicDescriptor = assetManager.openFd("shroom_ridge.ogg");  // Temporary music
            mediaPlayer.setDataSource(musicDescriptor.getFileDescriptor(),
                    musicDescriptor.getStartOffset(), musicDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(false);
        } catch (IOException e) {
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /**
     * Consider resources to release here. Also consider instantiating
     * SoundPool in onCreate()
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            if (isFinishing()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }
}
