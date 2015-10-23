package com.markchanner.mscprojectprototype02;

/**
 * Each AbstractEmoticon represents a square on the board. It provides methods for
 * accessing the location of the AbstractEmoticon's position, and for retrieving
 * both the type of GamePiece occupying it and what type GamePiece it is
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public interface Tile {

    /**
     * Returns the column at the specified position in the array
     *
     * @return the column location of the AbstractEmoticon
     */
    int getX();

    /**
     * Returns the row at the specified position in the array
     *
     * @return the row location of this tile
     */
    int getY();

    /**
     * Returns the GameInitializer Piece occupying the AbstractEmoticon
     *
     * @return the GameInitializer Piece occupying the AbstractEmoticon
     * @throws NullPointerException if the GamePiece has not been set
     */
    Emoticon getEmoticon();

    /**
     * Sets a GamePiece to occupy the AbstractEmoticon
     *
     * @param e the GamePiece to occupy the AbstractEmoticon
     */
    void setEmoticon(Emoticon e);

    /**
     * Returns a String representing the type of the game piece
     *
     * @return the game piece type
     * @throws NullPointerException
     */
    String getEmoticonType();

}
