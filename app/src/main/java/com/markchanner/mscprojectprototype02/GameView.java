package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class GameView extends View {

    private Context context;
    private Paint backgroundColour;
    private Paint gridLineColour;
    private int screenWidth;
    private int screenHeight;
    private Bitmap angryBitmap;
    private Bitmap delightedBitmap;
    private Bitmap embarrassedBitmap;
    private Bitmap surprisedBitmap;
    private Bitmap upsetBitmap;
    private int emoticonWidth;
    private int emoticonHeight;
    private final int ROW = 8;
    private final int COL = 7;
    private Emoticon[][] emoticonArray = new Emoticon[ROW][COL];
    private static final int ZERO = 0;
    private static final int ONE = 1;
    private int[] userSelection01 = new int[2];
    private int[] userSelection02 = new int[2];
    private boolean firstSelectionMade;

    public GameView(Context context) {
        super(context);
        this.context = context;
        context.getResources();
        backgroundColour = new Paint();
        gridLineColour = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        screenWidth = w;
        screenHeight = h;
        emoticonWidth = screenWidth / 8;
        emoticonHeight = screenHeight / 7;
        createEmoticons();
    }

    protected void createEmoticons() {
        Bitmap temp;
        // Retrieves graphics from drawable, scales down, then assigns to Bitmap object
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

        // Populates array with emoticon objects with randomly set bitmaps for emotion
        Emoticon newEmoticon;
        for (int rowID = 0; rowID < 8; rowID++) {
            for (int columnID = 0; columnID < 7; columnID++) {
                newEmoticon = generateRandomEmoticon(rowID, columnID); //this should be in do/while loop that eliminates more than 2 in a row
                /*do {

                } while ((rowID >= 2 &&
                        (newEmoticon.getType().equals(emoticonArray[rowID - 1][columnID].getBitmapType()) &&
                                newEmoticon.getType().equals(emoticonArray[rowID - 2][columnID].getBitmapType()))) ||
                        (columnID >= 2 &&
                                (newEmoticon.getType().equals(emoticonArray[rowID][columnID - 1].getBitmapType()) &&
                                        newEmoticon.getType().equals(emoticonArray[rowID][columnID - 2].getBitmapType()))));*/
                emoticonArray[rowID][columnID] = newEmoticon;
            }
        }
    }

    // Generates an emoticon at random and assigns row and column ID
    public Emoticon generateRandomEmoticon(int rowID, int columnID) {
        Emoticon emoticon = null;
        Random random = new Random();
        int value = random.nextInt(5);
        switch (value) {
            case 0:
                emoticon = new Emoticon(angryBitmap, rowID, columnID);
                break;
            case 1:
                emoticon = new Emoticon(delightedBitmap, rowID, columnID);
                break;
            case 2:
                emoticon = new Emoticon(embarrassedBitmap, rowID, columnID);
                break;
            case 3:
                emoticon = new Emoticon(surprisedBitmap, rowID, columnID);
                break;
            case 4:
                emoticon = new Emoticon(upsetBitmap, rowID, columnID);
                break;
            default:
                break;
        }
        // check that this is not null!!!!
        return emoticon;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draws a rectangle with sky blue centre
        backgroundColour.setColor(Color.parseColor("#7EC0EE"));
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundColour);

        // Draws grid lines within rectangle
        gridLineColour.setStrokeWidth(2f);
        gridLineColour.setColor(Color.BLACK);
        for (int i = 0; i < 8; i++) {
            canvas.drawLine(0, i * emoticonHeight, getWidth(), i * emoticonHeight, gridLineColour);
            canvas.drawLine(i * emoticonWidth, 0, i * emoticonWidth, getHeight(), gridLineColour);
        }
        // Draws emoticonList on canvas
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 7; col++) {
                Emoticon e = emoticonArray[row][col];
                canvas.drawBitmap(e.getBitmap(), row * emoticonWidth, col * emoticonHeight, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectTile(x / emoticonWidth, y / emoticonHeight);
                break;
        }
        invalidate();
        return true;
    }

    public void selectTile(int row, int column) {
        if (!(firstSelectionMade)) {
            firstSelectionMade = true;
            userSelection01[ZERO] = row;
            userSelection01[ONE] = column;
        } else {
            userSelection02[ZERO] = row;
            userSelection02[ONE] = column;
            swapPieces();
            resetUserSelections();
        }
    }

    private void swapPieces() {
        Emoticon tempEmoticon = emoticonArray[userSelection01[ZERO]][userSelection01[ONE]];
        emoticonArray[userSelection01[ZERO]][userSelection01[ONE]] = emoticonArray[userSelection02[ZERO]][userSelection02[ONE]];
        emoticonArray[userSelection02[ZERO]][userSelection02[ONE]] = tempEmoticon;
    }

    private void resetUserSelections() {
        firstSelectionMade = false;
        userSelection01[ZERO] = -1;
        userSelection01[ONE] = -1;
        userSelection02[ZERO] = -1;
        userSelection02[ONE] = -1;
    }
}
