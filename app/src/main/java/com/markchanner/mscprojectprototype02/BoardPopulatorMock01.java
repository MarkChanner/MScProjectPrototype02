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
    public void populate(GameView view, Context context, int emoticonWidth, int emoticonHeight) {

        createBitmaps(context, emoticonWidth, emoticonHeight);
        int rows = view.getRows();
        int cols = view.getCols();
        Tile[][] tiles = view.getTiles();
        int counter = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (tiles[row][col] == null) {
                    String str = counter <= 9 ? "0" + counter : "" + counter;
                    tiles[row][col] = new TileImpl(row, col, new MockEmoticon(emptyBitmap, str));
                    counter++;
                }
            }
        }

        // Sets up tiles so that a match of HappyEmoticons in a row
        // and a match of EmbarrassedEmoticons in a column can occur
        // when tiles 3,0 and 4,0 are selected
        tiles[1][0] = new TileImpl(1, 0, new SurprisedEmoticon(surprisedBitmap));
        tiles[2][0] = new TileImpl(2, 0, new SurprisedEmoticon(surprisedBitmap));
        tiles[3][0] = new TileImpl(3, 0, new EmbarrassedEmoticon(embarrassedBitmap));
        tiles[4][0] = new TileImpl(4, 0, new SurprisedEmoticon(surprisedBitmap));
        tiles[4][1] = new TileImpl(4, 1, new EmbarrassedEmoticon(embarrassedBitmap));
        tiles[4][2] = new TileImpl(4, 2, new EmbarrassedEmoticon(embarrassedBitmap));

        // Sets up tiles so that a match of AngryEmoticons in a row
        // and a match of UpsetEmoticons in a column can occur when
        // tiles at locations (3,3) and (3,4) are selected
        tiles[3][3] = new TileImpl(3, 3, new UpsetEmoticon(upsetBitmap));
        tiles[3][4] = new TileImpl(3, 4, new AngryEmoticon(angryBitmap));
        tiles[3][5] = new TileImpl(3, 5, new UpsetEmoticon(upsetBitmap));
        tiles[3][6] = new TileImpl(3, 6, new UpsetEmoticon(upsetBitmap));
        tiles[4][4] = new TileImpl(4, 4, new UpsetEmoticon(upsetBitmap));
        tiles[5][4] = new TileImpl(5, 4, new UpsetEmoticon(upsetBitmap));
    }

    @Override
    public void createBitmaps(Context context, int emoticonWidth, int emoticonHeight) {

        Bitmap temp;
        // Retrieves graphics from drawable, scales down, then assigns to Bitmap object
        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_tile);
        emptyBitmap = Bitmap.createScaledBitmap(temp, emoticonWidth, emoticonHeight, false);

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

    /** This method is currently not in use */
    @Override
    public Emoticon generateRandomEmoticon() {
       return new EmptyEmoticon(emptyBitmap);
    }
}
