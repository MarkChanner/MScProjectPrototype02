package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class Emoticon {

    private Bitmap bitmap;
    private String type;
    private int row;
    private int column;

    public Emoticon(Bitmap b, int r, int c) {
        bitmap = b;
        type = ""; // this will give the type of emoticon for comparison
        row = r;
        column = c;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap b) {
        bitmap = b;
    }

    public String getType() {
        return type;
    }

    public void setType(String t) {
        type = t;
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
