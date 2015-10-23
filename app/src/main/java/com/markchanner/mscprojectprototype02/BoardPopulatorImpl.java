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
        int maxX = view.getX_MAX();
        int maxY = view.getY_MAX();
        Tile[][] tiles = view.getTiles();
        Emoticon newEmoticon;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                do {
                    newEmoticon = generateRandomEmoticon();
                } while ((y >= 2 &&
                        (newEmoticon.showType().equals(tiles[x][y - 1].getEmoticonType()) &&
                                newEmoticon.showType().equals(tiles[x][y - 2].getEmoticonType()))) ||
                        (x >= 2 &&
                                (newEmoticon.showType().equals(tiles[x - 1][y].getEmoticonType()) &&
                                        newEmoticon.showType().equals(tiles[x - 2][y].getEmoticonType()))));

                tiles[x][y] = new TileImpl(x, y, newEmoticon);
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