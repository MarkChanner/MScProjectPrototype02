package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Implementations of this interface are used to populate the Board with game pieces.
 * For testing purposes, an implementation can populate the board with a set layout,
 * rather than at random, which would make testing difficult.
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 *
 */
public interface BoardPopulator {

    /**
     * Populates the given Board with game pieces
     *
     * @param view element to be populated with game pieces
     */
    void populate(GameView view, Context context, int emoticonWidth, int emoticonHeight);

    /**
     *
     *
     *
     */
    void createBitmaps(Context context, int emoticonWidth, int emoticonHeight);

    /**
     * Creates a game piece
     *
     * @return a game piece
     */
    Emoticon generateRandomEmoticon();

    Bitmap getEmptyBitmap();

}
