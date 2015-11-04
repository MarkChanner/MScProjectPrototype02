package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class AngryEmoticon extends AbstractEmoticon {

    public static final String EMOTION_TYPE = "ANGRY";

    public AngryEmoticon(int x, int y, int emoWidth, int emoHeight, Bitmap angryBitmap) {
        super (x, y, emoWidth, emoHeight, angryBitmap, EMOTION_TYPE);
    }
}



