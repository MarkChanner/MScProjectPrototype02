package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class EmptyEmoticon extends AbstractEmoticon {

    public static final String EMOTION_TYPE = "EMPTY";

    public EmptyEmoticon(int x, int y, Bitmap emptyBitmap) {
        super (x, y, emptyBitmap, EMOTION_TYPE);
    }
}



