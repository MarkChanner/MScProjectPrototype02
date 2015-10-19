package com.markchanner.mscprojectprototype02;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class Tile {

    private String bitmapType;

    public Tile(String t) {
        bitmapType = t;
    }

    public String getBitmapType() {
        return bitmapType;
    }

    public void setBitmapType(String bt) {
        bitmapType = bt;
    }
}
