package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * A game piece, such as an emoticon for a tile matching game.
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public interface Emoticon {

    /**
     *
     * @return Bitmap the bitmap representing the emotion
     */
    Bitmap getBitmap();

    /**
     * @param b
     */
    void setBitmap(Bitmap b);

    /**
     * Sets the position of the element that implements GamePiece in the board.
     *
     * @param row    the row number that the piece is located on the board
     * @param column the column number that the piece is located on the board
     */
    void setCoordinates(int row, int column);

    /**
     * Called from a board Tile that needs to access the Emoticon it is housing
     *
     * @return the Emoticon situated on that Tile
     */
    Emoticon retrieveEmoticon();

    /**
     * Gives the particular type of the Emoticon (such as 'HAPPY', 'ANGRY', etc)
     *
     * @return a String that gives the type of the GamePiece
     */
    String showType();

}

