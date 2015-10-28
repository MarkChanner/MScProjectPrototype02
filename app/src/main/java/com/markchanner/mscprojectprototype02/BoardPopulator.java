package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Implementations of this interface are used to populate the Board with emoticons.
 * For testing purposes, an implementation can populate the board with a set layout,
 * rather than at random, which would make testing difficult.
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public interface BoardPopulator {

    /**
     * Populates the given Board with emoticons
     *
     * @param view element to be populated with emoticons
     */
    void populate(GameView view, Context context, int emoticonWidth, int emoticonHeight);

    /**
     * Creates a random emoticon
     *
     * @return an emoticon
     */
    Emoticon generateRandomEmoticon(int x, int y);

    void createBitmaps(Context context, int emoticonWidth, int emoticonHeight);


    Bitmap getEmptyBitmap();

}
