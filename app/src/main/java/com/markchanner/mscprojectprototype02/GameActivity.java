package com.markchanner.mscprojectprototype02;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class GameActivity extends Activity {

    private MediaPlayer mediaPlayer;
    private GameView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);
        mediaPlayer = new MediaPlayer();

        try {
            AssetManager assetManager = getAssets();
            AssetFileDescriptor musicDescriptor = assetManager.openFd("shroom_ridge.ogg");  // Temporary music
            mediaPlayer.setDataSource(musicDescriptor.getFileDescriptor(),
                    musicDescriptor.getStartOffset(), musicDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        } catch (IOException e) {
            mediaPlayer = null;
        }

        view = new GameView(this, size.x, size.y, new BoardPopulatorImpl());
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        view.resume();
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
        view.pause();
    }
}
