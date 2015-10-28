package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class SurprisedEmoticon extends AbstractEmoticon {

    public static final String EMOTION_TYPE = "SURPRISED";

    public SurprisedEmoticon(int x, int y, Bitmap surprisedBitmap) {
        super (x, y, surprisedBitmap, EMOTION_TYPE);
    }
}



