package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 *
 * @author Mark Channer for second prototype of Birkbeck MSc Computer Science final project
 */
public class Emoticon {

    private Bitmap bitmap;
    private int row;
    private int column;

    public Emoticon(Bitmap b, int r, int c) {
        bitmap = b;
        row = r;
        column = c;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap b) {
        bitmap = b;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int c) {
        column = c;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int r) {
        row = r;
    }
}
