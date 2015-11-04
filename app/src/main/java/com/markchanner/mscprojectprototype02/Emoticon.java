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

    int getScreenXPosition();

    int getScreenYPosition();

    Bitmap getBitmap();

    void setBitmap(Bitmap b);

    String getType();

    void setSwapUp(boolean swapUp);

    boolean swapUpActivated();

    void swapUp();

    void setSwapDown(boolean swapDown);

    boolean swapDownActivated();

    void swapDown();

    void setSwapRight(boolean swapRight);

    boolean swapRightActivated();

    void swapRight();

    void setSwapLeft(boolean swapLeft);

    boolean swapLeftActivated();

    void swapLeft();

    void setShiftDown(boolean shiftDown);

    boolean shiftDownActivated();

    void shiftDown();

    void setShiftDistance(int shiftDistance);

}

