package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 *
 * Consider having a class that is just for behaviour and does not
 * contain anything related to graphics or sound
 */
public abstract class Emoticon {

    private Bitmap bitmap;
    private String face;
    private int row;
    private int column;
    private int x;
    private int y;

    public Emoticon(Bitmap b, String f, int r, int c) {
        bitmap = b;
        face = f;
        row = r;
        column = c;
        // x = WILL NEED TO GET LOCATION OF EMOTICON ON SCREEN FOR ANIMATION
        // y = WILL NEED TO GET LOCATION OF EMOTICON ON SCREEN FOR ANIMATION
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap b) {
        bitmap = b;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String f) {
        face = f;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = this.x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
