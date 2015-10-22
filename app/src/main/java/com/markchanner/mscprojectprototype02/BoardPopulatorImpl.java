package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

/**
 * Implementation of the BoardPopulator interface that populates a Board with Emoticons
 * at random. However, as this class is used for a matching game where the objective is
 * to match 3 consecutive Emoticons, it ensures that 3 consecutive pieces would not
 * be formed at the outset.
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class BoardPopulatorImpl implements BoardPopulator {

    private Bitmap angryBitmap;
    private Bitmap delightedBitmap;
    private Bitmap embarrassedBitmap;
    private Bitmap surprisedBitmap;
    private Bitmap upsetBitmap;
    private Bitmap emptyBitmap;

    /**
     * Populates the given Board object with game pieces that are allocated at random. If
     * placing the game piece would result in a board that has 3 consecutive piece types at
     * the start of the game, another game piece is chosen until one that does not form a match is
     * found { @inheritDocs }
     */
    @Override
    public void populate(GameView view, Context context, int emoticonWidth, int emoticonHeight) {
        createBitmaps(context, emoticonWidth, emoticonHeight);
        int rows = view.getRows();
        int cols = view.getCols();
        Tile[][] tiles = view.getTiles();
        Emoticon newEmoticon;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                do {
                    newEmoticon = generateRandomEmoticon();
                } while ((row >= 2 &&
                        (newEmoticon.showType().equals(tiles[row - 1][col].getEmoticonType()) &&
                                newEmoticon.showType().equals(tiles[row - 2][col].getEmoticonType()))) ||
                        (col >= 2 &&
                                (newEmoticon.showType().equals(tiles[row][col - 1].getEmoticonType()) &&
                                        newEmoticon.showType().equals(tiles[row][col - 2].getEmoticonType()))));

                tiles[row][col] = new TileImpl(row, col, newEmoticon);
            }
        }
    }

    /**
     * Returns one of five game piece objects that are chosen at random
     *
     * @return a subclass of AbstractEmoticon (AbstractEmoticon implements Emoticon interface)
     */
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


    // Generates an emoticon at random and assigns row and column ID
    public Emoticon generateRandomEmoticon() {
        Emoticon emoticon = null;
        Random random = new Random();
        int value = random.nextInt(5);
        switch (value) {
            case 0:
                emoticon = new AngryEmoticon(angryBitmap);
                break;
            case 1:
                emoticon = new DelightedEmoticon(delightedBitmap);
                break;
            case 2:
                emoticon = new EmbarrassedEmoticon(embarrassedBitmap);
                break;
            case 3:
                emoticon = new SurprisedEmoticon(surprisedBitmap);
                break;
            case 4:
                emoticon = new UpsetEmoticon(upsetBitmap);
                break;
            default:
                break;
        }
        return emoticon;
    }

    public Bitmap getEmptyBitmap() {
        return emptyBitmap;
    }
}