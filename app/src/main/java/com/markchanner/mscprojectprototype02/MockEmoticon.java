package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;


/**
 * This emoticon differs from others in that it takes a constructor which allows
 * its type to be set. This is used in the BoardPopulatorMock to populate the board
 * with unique values that will not lead to a match and cause difficulties with
 * running tests
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class MockEmoticon extends AbstractEmoticon {

    public MockEmoticon(int x, int y, Bitmap emptyBitmap, String mockType) {
        super (x, y, emptyBitmap, mockType);
    }
}