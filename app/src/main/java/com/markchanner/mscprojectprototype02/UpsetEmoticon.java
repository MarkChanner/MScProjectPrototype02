package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class UpsetEmoticon extends AbstractEmoticon {

    public static final String EMOTION_TYPE = "UPSET";

    public UpsetEmoticon(int x, int y, Bitmap upsetBitmap) {
        super (x, y, upsetBitmap, EMOTION_TYPE);
    }
}



