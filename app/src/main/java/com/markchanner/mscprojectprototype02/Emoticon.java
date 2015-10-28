package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public interface Emoticon {

    int getX();

    void setX(int x);

    int getY();

    void setY(int y);

    Bitmap getBitmap();

    void setBitmap(Bitmap b);

    String getType();
}

