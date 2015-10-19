package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Mark Channer for second prototype of Birkbeck MSc Computer Science final project
 */
public class GameView extends View {

    private Context context;
    private Paint canvasColour;
    private Paint rectangleOutlineColour;
    private Paint rectangleInnerColour;

    private int screenWidth;
    private int screenHeight;

    private Bitmap angryBitmap;
    private Bitmap delightedBitmap;
    private Bitmap embarrassedBitmap;
    private Bitmap surprisedBitmap;
    private Bitmap upsetBitmap;

    int scaledEmoticonSize;
    private List<Emoticon> emoticonList = new ArrayList<>(); // May yet stick with a 2d array


    /**
     * Don't use width and height of a View inside constructor!
     */
    public GameView(Context context) {
        super(context);
        this.context = context;
        context.getResources();
        canvasColour = new Paint();
        rectangleOutlineColour = new Paint();
        rectangleInnerColour = new Paint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        scaledEmoticonSize = (screenWidth / 14);
        createEmoticons();
    }

    protected void createEmoticons() {
        Bitmap temp;
        // Retrieves graphics from drawable, scales down, then assigns to Bitmap object
        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.angry);
        angryBitmap = Bitmap.createScaledBitmap(temp, scaledEmoticonSize, scaledEmoticonSize, false);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.delighted);
        delightedBitmap = Bitmap.createScaledBitmap(temp, scaledEmoticonSize, scaledEmoticonSize, false);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.embarrassed);
        embarrassedBitmap = Bitmap.createScaledBitmap(temp, scaledEmoticonSize, scaledEmoticonSize, false);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.surprised);
        surprisedBitmap = Bitmap.createScaledBitmap(temp, scaledEmoticonSize, scaledEmoticonSize, false);

        temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.upset);
        upsetBitmap = Bitmap.createScaledBitmap(temp, scaledEmoticonSize, scaledEmoticonSize, false);

        // Populates list with emoticon objects with randomly set bitmaps for emotion
        Emoticon tempEmoticon;
        for (int rowID = 0; rowID < 8; rowID++) {
            for (int columnID = 0; columnID < 7; columnID++) {
                tempEmoticon = generateRandomEmoticon(rowID, columnID);
                emoticonList.add(tempEmoticon);
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
        // Sets canvasColour colour and draws it to the canvas
        canvasColour.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getWidth(), getHeight(), canvasColour);

        // Draws a rectangle with green centre
        rectangleOutlineColour.setStrokeWidth(2f);
        rectangleOutlineColour.setColor(Color.BLACK);
        rectangleOutlineColour.setStyle(Paint.Style.STROKE);
        rectangleInnerColour.setColor(Color.GREEN);
        canvas.drawRect(5 * scaledEmoticonSize, scaledEmoticonSize, 13 * scaledEmoticonSize, 8 * scaledEmoticonSize, rectangleInnerColour);

        // Draws grid within rectangle
        for (int i = 5; i < 14; i++) {
            canvas.drawLine(i * scaledEmoticonSize, scaledEmoticonSize, i * scaledEmoticonSize, 8 * scaledEmoticonSize, rectangleOutlineColour);
            for (int j = 1; j < 9; j++) {
                canvas.drawLine(i * scaledEmoticonSize, j * scaledEmoticonSize, 8 * scaledEmoticonSize, j * scaledEmoticonSize, rectangleOutlineColour);
            }
        }
        // Draws emoticonList on canvas
        Emoticon tempEmoticon;
        int counter = 0;
        for (int i = 5; i < 13; i++) {
            for (int j = 1; j < 8; j++) {
                Emoticon e = emoticonList.get(counter);
                canvas.drawBitmap(e.getBitmap(), i * scaledEmoticonSize, j * scaledEmoticonSize, null);
                counter++;
            }
        }
        invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        /*switch (eventAction) {
            case MotionEvent.ACTION_UP:
                if (x > screenWidth - emoticon.getWidth() - 30) &&
                        y > screenHeight - scaledEmoticonSize &&
                        y < screenHeight - scaledEmoticonSize) {

                }
            break;
            invalidate();

        }*/
        return true;
    }
}
