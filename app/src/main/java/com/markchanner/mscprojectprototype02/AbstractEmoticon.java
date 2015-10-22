package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 *
 * Consider having a class that is just for behaviour and does not
 * contain anything related to graphics or sound
 */
public abstract class AbstractEmoticon implements Emoticon {

    private Bitmap bitmap;
    private String type;
    private int[] coordinates;

    public AbstractEmoticon(Bitmap b, String f) {
        bitmap = b;
        type = f;
        coordinates = new int[2];
        coordinates[0] = -1;
        coordinates[1] = -1;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap b) {
        bitmap = b;
    }

    public void setCoordinates(int row, int column) {
        coordinates[0] = row;
        coordinates[1] = column;
    }

    public abstract Emoticon retrieveEmoticon();

    public String showType() {
        return type;
    }
}
