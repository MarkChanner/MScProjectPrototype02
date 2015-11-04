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
public class BoardPopulatorMock01 implements BoardPopulator {

    private Bitmap angryBitmap;
    private Bitmap delightedBitmap;
    private Bitmap embarrassedBitmap;
    private Bitmap surprisedBitmap;
    private Bitmap upsetBitmap;
    private Bitmap emptyBitmap;

    @Override
    public void populate(GameView view, Context context, int emoWidth, int emoHeight) {

        createBitmaps(context, emoWidth, emoHeight);
        int maxX = view.getX_MAX();
        int maxY = view.getY_MAX();
        Emoticon[][] tiles = view.getEmoticonArray();
        int counter = 0;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                if (tiles[x][y] == null) {
                    String str = counter <= 9 ? "0" + counter : "" + counter;
                    /**BELOW WAS: tiles[x][y] = new TileImpl(x, y, new MockEmoticon(emptyBitmap, str));*/
                    tiles[x][y] = new MockEmoticon(x, y, emoWidth, emoHeight, emptyBitmap, str);
                    counter++;
                }
            }
        }
        // Sets up emoticons so that a match of HappyEmoticons in a row
        // and a match of EmbarrassedEmoticons in a column can occur
        // when tiles 0,3 and 0,4 are selected
        tiles[0][1] = new SurprisedEmoticon(0, 1, emoWidth, emoHeight, surprisedBitmap);
        tiles[0][2] = new SurprisedEmoticon(0, 2, emoWidth, emoHeight, surprisedBitmap);
        tiles[0][3] = new EmbarrassedEmoticon(0, 3, emoWidth, emoHeight, embarrassedBitmap);
        tiles[0][4] = new SurprisedEmoticon(0, 4, emoWidth, emoHeight, surprisedBitmap);
        tiles[1][4] = new EmbarrassedEmoticon(1, 4, emoWidth, emoHeight, embarrassedBitmap);
        tiles[2][4] = new EmbarrassedEmoticon(2, 4, emoWidth, emoHeight, embarrassedBitmap);

        // Sets up tiles so that a match of AngryEmoticons in a row
        // and a match of UpsetEmoticons in a column can occur when
        // tiles at locations (3,3) and (4,3) are selected
        tiles[3][3] = new UpsetEmoticon(3, 3, emoWidth, emoHeight, upsetBitmap);
        tiles[4][3] = new AngryEmoticon(4, 3, emoWidth, emoHeight, angryBitmap);
        tiles[5][3] = new UpsetEmoticon(5, 3, emoWidth, emoHeight, upsetBitmap);
        tiles[6][3] = new UpsetEmoticon(6, 3, emoWidth, emoHeight, upsetBitmap);
        tiles[4][4] = new UpsetEmoticon(4, 4, emoWidth, emoHeight, upsetBitmap);
        tiles[4][5] = new UpsetEmoticon(4, 5, emoWidth, emoHeight, upsetBitmap);
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

    public Bitmap getEmptyBitmap() {
        return emptyBitmap;
    }

    /**
     * This method is currently not in use
     */
    @Override
    public Emoticon generateRandomEmoticon(int x, int y, int emoWidth, int emoHeight) {
        return new EmptyEmoticon(x, y, emoWidth, emoHeight, emptyBitmap);
    }
}
