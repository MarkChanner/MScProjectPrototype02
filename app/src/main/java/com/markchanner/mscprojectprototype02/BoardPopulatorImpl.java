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
     * Populates the given Board object with emoticons that are allocated at random. If
     * placing the emoticon would result in a board that has 3 consecutive emoticons at
     * the start of the game, another emoticon is chosen until one that does not form a match is
     * found { @inheritDocs }
     */
    @Override
    public void populate(GameView view, Context context, int emoticonWidth, int emoticonHeight) {
        createBitmaps(context, emoticonWidth, emoticonHeight);
        int maxX = view.getX_MAX();
        int maxY = view.getY_MAX();
        Emoticon[][] emoticonArray = view.getEmoticonArray();
        Emoticon newEmoticon;
        for (int x = 0; x < maxX; x++) {
            for (int y = 0; y < maxY; y++) {
                do {
                    newEmoticon = generateRandomEmoticon(x, y);
                } while ((y >= 2 &&
                        (newEmoticon.getType().equals(emoticonArray[x][y - 1].getType()) &&
                                newEmoticon.getType().equals(emoticonArray[x][y - 2].getType()))) ||
                        (x >= 2 &&
                                (newEmoticon.getType().equals(emoticonArray[x - 1][y].getType()) &&
                                        newEmoticon.getType().equals(emoticonArray[x - 2][y].getType()))));

                emoticonArray[x][y] = newEmoticon;
            }
        }
    }

    /**
     * Returns one of five emoticons that are chosen at random
     *
     * @return a subclass of AbstractEmoticon (AbstractEmoticon implements Emoticon interface)
     */
    @Override
    public Emoticon generateRandomEmoticon(int x, int y) {
        Emoticon emoticon = null;
        Random random = new Random();
        int value = random.nextInt(5);
        switch (value) {
            case 0:
                emoticon = new AngryEmoticon(x, y, angryBitmap);
                break;
            case 1:
                emoticon = new DelightedEmoticon(x, y, delightedBitmap);
                break;
            case 2:
                emoticon = new EmbarrassedEmoticon(x, y, embarrassedBitmap);
                break;
            case 3:
                emoticon = new SurprisedEmoticon(x, y, surprisedBitmap);
                break;
            case 4:
                emoticon = new UpsetEmoticon(x, y, upsetBitmap);
                break;
            default:
                break;
        }
        return emoticon;
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

    @Override
    public Bitmap getEmptyBitmap() {
        return emptyBitmap;
    }
}