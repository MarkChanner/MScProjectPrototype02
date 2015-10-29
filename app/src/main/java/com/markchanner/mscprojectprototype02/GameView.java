package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class GameView extends View { //} extends SurfaceView implements Runnable {

    private Context context;
    //private Canvas canvas;
    private SoundPool soundPool;
    //private SurfaceHolder holder;
    private Paint backgroundColour;
    private Paint gridLineColour;
    private Paint selectionColor;
    //volatile boolean playing = false;
    //Thread gameThread = null;

    int swapID = -1;
    private int emoWidth;
    private int emoHeight;
    private final Rect highlightedEmoticon = new Rect();

    private static final int X_VAL = 0;
    private static final int Y_VAL = 1;


    private final int X_MAX = 8;
    private final int Y_MAX = 7;
    private MatchFinder matchFinder;
    private BoardPopulator populator;
    private Emoticon[][] emoticonArray;
    private int[] selection1 = new int[2];
    private int[] selection2 = new int[2];
    private boolean firstSelectionMade;

    public GameView(Context theContext, BoardPopulator bp) {
        super(theContext);
        context = theContext;
        context.getResources();

        // sound file should not exceed 1MB or last over 5 seconds
        // First parameter defines the max number of sounds effects
        // can play simultaneously. Final parameter unused to default 0.
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("swap.ogg");
            // Second parameter specifies priority of sound effect
            swapID = soundPool.load(descriptor, 0);
        } catch (IOException e) {
            // print error message
            Log.e("Error", "swapID sound file failed to load!");
        }

        populator = bp;
        matchFinder = new MatchFinderImpl();
        emoticonArray = new AbstractEmoticon[X_MAX][Y_MAX];
        //holder = getHolder();
        backgroundColour = new Paint();
        gridLineColour = new Paint();
        selectionColor = new Paint();
        resetUserSelections();
    }

    public int getX_MAX() {
        return X_MAX;
    }

    public int getY_MAX() {
        return Y_MAX;
    }

    public Emoticon[][] getEmoticonArray() {
        if (emoticonArray == null) {
            throw new NullPointerException(); /* Exceptions need work */
        } else {
            return emoticonArray;
        }
    }

    private void resetUserSelections() {
        firstSelectionMade = false;
        selection1[X_VAL] = -1;
        selection1[Y_VAL] = -1;
        selection2[X_VAL] = -1;
        selection2[Y_VAL] = -1;
    }

    @Override
    protected void onSizeChanged(int screenWidth, int screenHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(screenWidth, screenHeight, oldWidth, oldHeight);
        emoWidth = screenWidth / X_MAX;
        emoHeight = screenHeight / Y_MAX;
        populator.populate(this, context, emoWidth, emoHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //canvas = holder.lockCanvas();
        canvas.drawColor(Color.argb(255, 0, 0, 0));

        final int ZERO = 0;
        backgroundColour.setColor(Color.parseColor("#7EC0EE"));
        // Draws a screen-sized rectangle with sky blue inside
        canvas.drawRect(ZERO, ZERO, getWidth(), getHeight(), backgroundColour);

        // Highlights a selected emoticon
        selectionColor.setColor(Color.parseColor("#fff2a8"));
        canvas.drawRect(highlightedEmoticon, selectionColor);

        gridLineColour.setStrokeWidth(2f);
        gridLineColour.setColor(Color.BLACK);
        for (int i = 0; i < X_MAX; i++) {
            // Draws  horizontal grid lines
            canvas.drawLine(ZERO, i * emoHeight, getWidth(), i * emoHeight, gridLineColour);
            //Draw  vertical grid lines
            canvas.drawLine(i * emoWidth, ZERO, i * emoWidth, getHeight(), gridLineColour);
        }
        // Draws emoticons
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                Emoticon e = emoticonArray[x][y];
                canvas.drawBitmap(e.getBitmap(), x * emoWidth, y * emoHeight, null);
            }
        }
        //holder.unlockCanvasAndPost(canvas);

    }

    /*private void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            final int ZERO = 0;
            backgroundColour.setColor(Color.parseColor("#7EC0EE"));
            // Draws a screen-sized rectangle with sky blue inside
            canvas.drawRect(ZERO, ZERO, getWidth(), getHeight(), backgroundColour);

            // Highlights a selected  emoticon
            selectionColor.setColor(Color.parseColor("#fff2a8"));
            canvas.drawRect(highlightedEmoticon, selectionColor);

            gridLineColour.setStrokeWidth(2f);
            gridLineColour.setColor(Color.BLACK);
            for (int i = 0; i < X_MAX; i++) {
                // Draws  horizontal grid lines
                canvas.drawLine(ZERO, i * emoHeight, getWidth(), i * emoHeight, gridLineColour);
                //Draw  vertical grid lines
                canvas.drawLine(i * emoWidth, ZERO, i * emoWidth, getHeight(), gridLineColour);
            }
            // Draws emoticons
            for (int x = 0; x < X_MAX; x++) {
                for (int y = 0; y < Y_MAX; y++) {
                    Emoticon e = emoticonArray[x][y].getEmoticon();
                    canvas.drawBitmap(e.getBitmap(), x * emoWidth, y * emoHeight, null);
                }
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectEmoticon(x / emoWidth, y / emoHeight);
                break;
        }
        //invalidate();
        return true;
    }

    public void selectEmoticon(int x, int y) {
        if (!(firstSelectionMade)) {
            selection1[X_VAL] = x;
            selection1[Y_VAL] = y;
            highlightEmoticon(selection1[0], selection1[1]);
            firstSelectionMade = true;
        } else {
            selection2[X_VAL] = x;
            selection2[Y_VAL] = y;
            checkValidSelections();
        }
    }

    private void checkValidSelections() {
        deselectEmoticon();
        if (!sameSquareSelectedTwice()) {
            if (selectedEmoticonsAreAdjacent()) {
                swapPieces();
                findMatches();
                resetUserSelections();
            } else {
                selection1[0] = selection2[0];
                selection1[1] = selection2[1];
                selection2[0] = -1;
                selection2[1] = -1;
                highlightEmoticon(selection1[0], selection1[1]);
            }
        } else {
            resetUserSelections();
        }
    }

    private void highlightEmoticon(int x, int y) {
        invalidate(highlightedEmoticon);
        highlightedEmoticon.set(x * emoWidth, y * emoHeight, x * emoWidth + emoWidth, y * emoHeight + emoHeight);
        invalidate(highlightedEmoticon);
    }

    private void deselectEmoticon() {
        invalidate(highlightedEmoticon);
        highlightedEmoticon.setEmpty();
        invalidate(highlightedEmoticon);
    }

    private boolean sameSquareSelectedTwice() {
        return ((selection1[X_VAL] == selection2[X_VAL]) && (selection1[Y_VAL] == selection2[Y_VAL]));
    }

    private boolean selectedEmoticonsAreAdjacent() {
        if ((selection1[X_VAL] == selection2[X_VAL]) &&
                (selection1[Y_VAL] == (selection2[Y_VAL] + 1) || selection1[Y_VAL] == (selection2[Y_VAL] - 1))) {
            return true;
        } else if ((selection1[Y_VAL] == selection2[Y_VAL]) &&
                (selection1[X_VAL] == (selection2[X_VAL] + 1) || selection1[X_VAL] == (selection2[X_VAL] - 1))) {
            return true;
        }
        return false;
    }

    private void swapPieces() {
        soundPool.play(swapID, 1, 1, 0, 0, 1);
        Emoticon tempEmoticon1 = emoticonArray[selection1[X_VAL]][selection1[Y_VAL]];
        int tempX = tempEmoticon1.getX();
        int tempY = tempEmoticon1.getY();

        emoticonArray[selection1[X_VAL]][selection1[Y_VAL]].setX(emoticonArray[selection2[X_VAL]][selection2[Y_VAL]].getX());
        emoticonArray[selection1[X_VAL]][selection1[Y_VAL]].setY(emoticonArray[selection2[X_VAL]][selection2[Y_VAL]].getY());
        emoticonArray[selection1[X_VAL]][selection1[Y_VAL]] = emoticonArray[selection2[X_VAL]][selection2[Y_VAL]];

        emoticonArray[selection2[X_VAL]][selection2[Y_VAL]].setX(tempX);
        emoticonArray[selection2[X_VAL]][selection2[Y_VAL]].setY(tempY);
        emoticonArray[selection2[X_VAL]][selection2[Y_VAL]] = tempEmoticon1;
    }

    private void findMatches() {
        ArrayList<LinkedList<Emoticon>> matchingX = matchFinder.findMatchingColumns(this);
        ArrayList<LinkedList<Emoticon>> matchingY = matchFinder.findMatchingRows(this);
        if (matchesFound(matchingX, matchingY)) {
            updateBoard(matchingX, matchingY);
        } else {
            swapPiecesBack();
        }
    }

    private boolean matchesFound(ArrayList<LinkedList<Emoticon>> matchingX, ArrayList<LinkedList<Emoticon>> matchingY) {
        return (!(matchingX.isEmpty() && matchingY.isEmpty()));
    }

    private void swapPiecesBack() {
        swapPieces();
    }

    private void updateBoard(ArrayList<LinkedList<Emoticon>> matchingColumns, ArrayList<LinkedList<Emoticon>> matchingRows) {
        do {
            removeFromBoard(matchingColumns);
            removeFromBoard(matchingRows);
            shiftIconsDown();
            insertNewEmoticons();
            matchingColumns = matchFinder.findMatchingColumns(this);
            matchingRows = matchFinder.findMatchingRows(this);
        } while (matchesFound(matchingColumns, matchingRows));
    }

    private void removeFromBoard(ArrayList<LinkedList<Emoticon>> matches) {
        for (List<Emoticon> removeList : matches) {
            for (Emoticon t : removeList) {
                int x = t.getX();
                int y = t.getY();
                if (!(emoticonArray[x][y].getType().equals("EMPTY"))) {
                    Bitmap emptyBitmap = populator.getEmptyBitmap();
                    emoticonArray[x][y] = new EmptyEmoticon(x, y, emptyBitmap);
                }
            }
        }
    }

    private void shiftIconsDown() {
        for (int x = 0; x < X_MAX; x++) {
            for (int y = Y_MAX - 1; y >= 0; y--) {
                // If empty emoticon found, mark emoticon and proceed up column until
                // gets to end of column or finds a non-empty emoticon, in which
                // case this non-empty emoticon should be moved to the previously
                // marked emoticon and then set to empty
                if (emoticonArray[x][y].getType().equals("EMPTY")) {
                    int tempY = y;
                    while ((tempY >= 0) && (emoticonArray[x][tempY].getType().equals("EMPTY"))) {
                        tempY--;
                    }

                    // If all emoticonArray to the top of the column are empty, then
                    // the value of tempY will be not be less than 0,
                    // which means there are no icons in the column to shift down
                    if (tempY >= 0) {
                        Emoticon shiftedEmoticon = emoticonArray[x][tempY];
                        shiftedEmoticon.setX(emoticonArray[x][y].getX());
                        shiftedEmoticon.setY(emoticonArray[x][y].getY());
                        emoticonArray[x][y] = shiftedEmoticon;
                        Bitmap emptyBitmap = populator.getEmptyBitmap();
                        emoticonArray[x][tempY] = new EmptyEmoticon(x, tempY, emptyBitmap);
                    }
                }
            }
        }
    }

    private void insertNewEmoticons() {
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                if (emoticonArray[x][y].getType().equals("EMPTY")) {
                    Emoticon newEmoticon = populator.generateRandomEmoticon(x, y);
                    emoticonArray[x][y] = newEmoticon;
                }
            }
        }
    }
}
