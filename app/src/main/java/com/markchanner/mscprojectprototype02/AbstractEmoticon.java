package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public abstract class AbstractEmoticon implements Emoticon {

    private int x;
    private int y;
    private Bitmap bitmap;
    private String emotionType;

    public AbstractEmoticon(int x, int y, Bitmap bitmap, String emotionType) {
        this.x = x;
        this.y = y;
        this.bitmap = bitmap;
        this.emotionType = emotionType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getType() {
        return emotionType;
    }
}
