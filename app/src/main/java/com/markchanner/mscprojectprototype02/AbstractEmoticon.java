package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public abstract class AbstractEmoticon implements Emoticon {

    private int x;
    private int y;
    private int emoWidth;
    private int emoHeight;
    private int screenPosX;
    private int screenPosY;
    private Bitmap bitmap;
    private String emotionType;
    volatile boolean swapUp;
    volatile boolean swapDown;
    volatile boolean swapRight = false;
    volatile boolean swapLeft = false;
    volatile boolean shiftDown = false;
    private int shiftDistance = 0;

    public AbstractEmoticon(int gridX, int gridY, int emoWidth, int emoHeight, Bitmap bitmap, String emotionType) {
        this.x = gridX;
        this.y = gridY;
        this.emoWidth = emoWidth;
        this.emoHeight = emoHeight;
        this.screenPosX = emoWidth * x;
        this.screenPosY = emoHeight * y;
        this.bitmap = bitmap;
        this.emotionType = emotionType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        this.screenPosX = emoWidth * x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        this.screenPosY = emoHeight * y;
    }

    public int getScreenXPosition() {
        return screenPosX;
    }

    public int getScreenYPosition() {
        return screenPosY;
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

    public void setSwapUp(boolean swapUp) {
        this.swapUp = swapUp;
    }

    public boolean swapUpActivated() {
        return swapUp;
    }

    public void swapUp() {
        int newPosition = emoHeight * (y - 1);
        int pixelsToMove = 64;
        while (screenPosY - pixelsToMove < newPosition) {
            pixelsToMove /= 2;
        }
        screenPosY -= pixelsToMove;
        if (screenPosY <= newPosition) swapUp = false;
    }

    public void setSwapDown(boolean swapDown) {
        this.swapDown = swapDown;
    }

    public boolean swapDownActivated() {
        return swapDown;
    }

    public void swapDown() {
        int destination = emoHeight * (y + 1);
        int pixelsToMove = 64;
        while (screenPosY + pixelsToMove > destination) {
            pixelsToMove /= 2;
        }
        screenPosY += pixelsToMove;
        if (screenPosY >= destination) swapDown = false;
    }

    public void setSwapRight(boolean swapRight) {
        this.swapRight = swapRight;
    }

    public boolean swapRightActivated() {
        return swapRight;
    }

    public void swapRight() {
        int destination = emoWidth * (x + 1);
        int pixelsToMove = 64;
        while (screenPosX + pixelsToMove > destination) {
            pixelsToMove /= 2;
        }
        screenPosX += pixelsToMove;
        if (screenPosX >= destination) swapRight = false;
    }

    public void setSwapLeft(boolean swapLeft) {
        this.swapLeft = swapLeft;
    }

    public boolean swapLeftActivated() {
        return swapLeft;
    }

    public void swapLeft() {
        int destination = emoWidth * (x - 1);
        int pixelsToMove = 64;
        while (screenPosX - pixelsToMove < destination) {
            pixelsToMove /= 2;
        }
        screenPosX -= pixelsToMove;
        if (screenPosX <= destination) swapLeft = false;
    }

    public void setShiftDown(boolean shiftDown) {
        this.shiftDown = shiftDown;
    }

    public boolean shiftDownActivated() {
        return shiftDown;
    }

    public void shiftDown() {
        int destination = emoHeight * (y + shiftDistance);
        int pixelsToMove = 16;
        while (screenPosY + pixelsToMove > destination) {
            pixelsToMove /= 2;
        }
        screenPosY += pixelsToMove;
        if (screenPosY >= destination) {
            shiftDown = false;
            setShiftDistance(0);
        }
    }

    public void setShiftDistance(int shiftDistance) {
        this.shiftDistance = shiftDistance;
    }
}
