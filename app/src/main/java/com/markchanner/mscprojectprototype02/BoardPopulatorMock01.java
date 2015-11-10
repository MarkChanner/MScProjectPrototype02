package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Populates the board with numbers, starting with 0 and incrementing by 1. This is so
 * that no matches can occur except for on the tiles that are set with Emoticons at
 * strategic places for testing
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class BoardPopulatorMock01 { // implements BoardPopulator {
/*
    private Bitmap angryBitmap;
    private Bitmap delightedBitmap;
    private Bitmap embarrassedBitmap;
    private Bitmap surprisedBitmap;
    private Bitmap upsetBitmap;
    private Bitmap emptyBitmap;
    private int X_MAX;
    private int Y_MAX;

    public BoardPopulatorMock01(int X_MAX, int Y_MAX) {
        this.X_MAX = X_MAX;
        this.Y_MAX = Y_MAX;
    }

    @Override
    public void populateBoard(Context context, BoardImpl board, int emoticonWidth, int emoticonHeight) {
        createBitmaps(context, emoticonWidth, emoticonHeight);
        Emoticon[][] emoticons = board.getEmoticons();
        int counter = 0;
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                if (emoticons[x][y] == null) {
                    String str = counter <= 9 ? "0" + counter : "" + counter;
                    emoticons[x][y] = new MockEmoticon(x, y, emoticonWidth, emoticonHeight, emptyBitmap, str);
                    counter++;
                }
            }
        }
        // Sets up emoticons so that a match of HappyEmoticons in a row
        // and a match of EmbarrassedEmoticons in a column can occur
        // when tiles 0,3 and 0,4 are selected
        emoticons[0][1] = new SurprisedEmoticon( 0, 1, emoticonWidth, emoticonHeight, surprisedBitmap);
        emoticons[0][2] = new SurprisedEmoticon(0, 2, emoticonWidth, emoticonHeight, surprisedBitmap);
        emoticons[0][3] = new EmbarrassedEmoticon(0, 3, emoticonWidth, emoticonHeight, embarrassedBitmap);
        emoticons[0][4] = new SurprisedEmoticon(0, 4, emoticonWidth, emoticonHeight, surprisedBitmap);
        emoticons[1][4] = new EmbarrassedEmoticon(X_MAX, Y_MAX, 1, 4, emoticonWidth, emoticonHeight, embarrassedBitmap);
        emoticons[2][4] = new EmbarrassedEmoticon(X_MAX, Y_MAX, 2, 4, emoticonWidth, emoticonHeight, embarrassedBitmap);

        // Sets up tiles so that a match of AngryEmoticons in a row
        // and a match of UpsetEmoticons in a column can occur when
        // tiles at locations (3,3) and (4,3) are selected
        emoticons[3][3] = new UpsetEmoticon(X_MAX, Y_MAX, 3, 3, emoticonWidth, emoticonHeight, upsetBitmap);
        emoticons[4][3] = new AngryEmoticon(X_MAX, Y_MAX, 4, 3, emoticonWidth, emoticonHeight, angryBitmap);
        emoticons[5][3] = new UpsetEmoticon(X_MAX, Y_MAX, 5, 3, emoticonWidth, emoticonHeight, upsetBitmap);
        emoticons[6][3] = new UpsetEmoticon(X_MAX, Y_MAX, 6, 3, emoticonWidth, emoticonHeight, upsetBitmap);
        emoticons[4][4] = new UpsetEmoticon(X_MAX, Y_MAX, 4, 4, emoticonWidth, emoticonHeight, upsetBitmap);
        emoticons[4][5] = new UpsetEmoticon(X_MAX, Y_MAX, 4, 5, emoticonWidth, emoticonHeight, upsetBitmap);
    }

    @Override
    public void createBitmaps(Context context, int emoticonWidth, int emoticonHeight) {

        Bitmap temp;
        // Retrieves graphics from drawable, scales down, then assigns to Bitmap object
        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_tile);
        emptyBitmap = Bitmap.createScaledBitmap(temp, emoticonWidth, emoticonHeight, false);
        emptyBitmap.eraseColor(android.graphics.Color.TRANSPARENT);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.angry);
        angryBitmap = Bitmap.createScaledBitmap(temp, emoticonWidth, emoticonHeight, false);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.delighted);
        delightedBitmap = Bitmap.createScaledBitmap(temp, emoticonWidth, emoticonHeight, false);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.embarrassed);
        embarrassedBitmap = Bitmap.createScaledBitmap(temp, emoticonWidth, emoticonHeight, false);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.surprised);
        surprisedBitmap = Bitmap.createScaledBitmap(temp, emoticonWidth, emoticonHeight, false);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.upset);
        upsetBitmap = Bitmap.createScaledBitmap(temp, emoticonWidth, emoticonHeight, false);

    }


     //This method is currently not in use

    @Override
    public Emoticon generateEmoticon(int x, int y, int emoWidth, int emoHeight) {
        return new EmptyEmoticon(X_MAX, Y_MAX, x, y, emoWidth, emoHeight, emptyBitmap);
    }

    public Emoticon getEmptyEmoticon(int x, int y, int emoticonWidth, int emoticonHeight) {
        return new EmptyEmoticon(X_MAX, Y_MAX, x, y, emoticonWidth, emoticonHeight, emptyBitmap);
    }*/
}
