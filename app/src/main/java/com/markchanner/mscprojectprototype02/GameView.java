package com.markchanner.mscprojectprototype02;

import android.view.SurfaceView;
import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.view.MotionEvent;

import java.io.IOException;

import android.util.Log;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class GameView extends SurfaceView implements Runnable {

    //private Context context;
    private int emoWidth;
    private int emoHeight;
    private Emoticon[][] emoticonArray;
    private BoardPopulator populator;
    private MatchFinder matchFinder;
    private SoundPool soundPool;
    int swapSoundEffectID = -1;
    private SurfaceHolder holder;
    private Thread gameThread = null;
    volatile boolean running = false;
    private final Rect highlightSelection = new Rect();
    private Paint backgroundColour;
    private Paint gridLineColour;
    private Paint selectionColour;
    private final int X_MAX = 8;
    private final int Y_MAX = 7;
    public static final int X_VAL = 0;
    public static final int Y_VAL = 1;
    public static final int ZERO = 0;
    public static final String EMPTY = "EMPTY";
    private boolean firstSelectionMade;
    private int[] selection1 = new int[2];
    private int[] selection2 = new int[2];

    public GameView(Context context, int screenX, int screenY, BoardPopulator populator) {
        super(context);
        //this.context = context;
        context.getResources();
        this.emoWidth = screenX / X_MAX;
        this.emoHeight = screenY / Y_MAX;
        this.emoticonArray = new AbstractEmoticon[X_MAX][Y_MAX];
        this.populator = populator;
        this.populator.populate(this, context, emoWidth, emoHeight);
        this.matchFinder = new MatchFinderImpl();
        this.soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor = assetManager.openFd("swap.ogg");
            // Second parameter specifies priority of sound effect
            swapSoundEffectID = soundPool.load(descriptor, 0);
        } catch (IOException e) {
            Log.e("Error", "swapSoundEffectID sound file failed to load!");
        }

        holder = getHolder();
        backgroundColour = new Paint();
        backgroundColour.setColor(Color.parseColor("#7EC0EE"));
        gridLineColour = new Paint();
        gridLineColour.setStrokeWidth(2f);
        gridLineColour.setColor(Color.BLACK);
        selectionColour = new Paint();
        selectionColour.setColor(Color.parseColor("#fff2a8"));
        resetUserSelections();
        draw();
    }

    private void resetUserSelections() {
        firstSelectionMade = false;
        selection1[X_VAL] = -1;
        selection1[Y_VAL] = -1;
        selection2[X_VAL] = -1;
        selection2[Y_VAL] = -1;
    }

    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start(); // starts a new thread, which begins with the below run method
    }

    @Override
    public void run() {
        while (running) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        for (int y = Y_MAX - 1; y >= 0; y--) {
            for (int x = 0; x < X_MAX; x++) {
                if (emoticonArray[x][y].swapUpActivated()) {
                    emoticonArray[x][y].swapUp();
                } else if (emoticonArray[x][y].swapDownActivated()) {
                    emoticonArray[x][y].swapDown();
                } else if (emoticonArray[x][y].swapLeftActivated()) {
                    emoticonArray[x][y].swapLeft();
                } else if (emoticonArray[x][y].swapRightActivated()) {
                    emoticonArray[x][y].swapRight();
                } else if (emoticonArray[x][y].shiftDownActivated()) {
                    emoticonArray[x][y].shiftDown();
                }
            }
        }
    }

    private void draw() {
        Canvas canvas;
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();

            // Erase the last frame
            canvas.drawColor(Color.argb(255, 0, 0, 0));
            canvas.drawRect(ZERO, ZERO, getWidth(), getHeight(), backgroundColour);

            // Highlight a selected emoticon
            canvas.drawRect(highlightSelection, selectionColour);

            for (int i = 0; i < X_MAX; i++) {
                // Draws horizontal grid lines
                canvas.drawLine(ZERO, i * emoHeight, getWidth(), i * emoHeight, gridLineColour);
                //Draw vertical grid lines
                canvas.drawLine(i * emoWidth, ZERO, i * emoWidth, getHeight(), gridLineColour);
            }

            // Draws emoticons
            for (int x = 0; x < X_MAX; x++) {
                for (int y = 0; y < Y_MAX; y++) {
                    Emoticon e = emoticonArray[x][y];
                    canvas.drawBitmap(e.getBitmap(), e.getScreenXPosition(), e.getScreenYPosition(), null);
                }
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {

        }
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                gameThread.join();
                return;
            } catch (InterruptedException e) {
                // retry
            }
        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                selectEmoticon(x / emoWidth, y / emoHeight);
                break;
        }
        return true;
    }

    public void selectEmoticon(int x, int y) {
        if (!(firstSelectionMade)) {
            selection1[X_VAL] = x;
            selection1[Y_VAL] = y;
            highlightEmoticon(selection1[X_VAL], selection1[Y_VAL]);
            firstSelectionMade = true;
        } else {
            selection2[X_VAL] = x;
            selection2[Y_VAL] = y;
            checkValidSelections();
        }
    }

    private void checkValidSelections() {
        unHighlightEmoticon();
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
        highlightSelection.set(x * emoWidth, y * emoHeight, x * emoWidth + emoWidth, y * emoHeight + emoHeight);
    }

    private void unHighlightEmoticon() {
        highlightSelection.setEmpty();
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
        soundPool.play(swapSoundEffectID, 1, 1, 0, 0, 1);
        animateSwap();

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

    private void animateSwap() {
        Emoticon e1 = emoticonArray[selection1[X_VAL]][selection1[Y_VAL]];
        Emoticon e2 = emoticonArray[selection2[X_VAL]][selection2[Y_VAL]];

        if (e1.getX() == e2.getX()) {
            if (e1.getY() < e2.getY()) {
                e1.setSwapDown(true);
                e2.setSwapUp(true);
                while (e1.swapDownActivated() && e2.swapUpActivated()) {
                    // wait
                }
            } else if (e1.getY() > e2.getY()) {
                e1.setSwapUp(true);
                e2.setSwapDown(true);
                while (e1.swapUpActivated() && e2.swapDownActivated()) {
                    // wait
                }
            }
        } else if (e1.getY() == e2.getY()) {
            if (e1.getX() < e2.getX()) {
                e1.setSwapRight(true);
                e2.setSwapLeft(true);
                while (e1.swapRightActivated() && e2.swapLeftActivated()) {
                    // wait
                }
            } else if (e1.getX() > e2.getX()) {
                e1.setSwapLeft(true);
                e2.setSwapRight(true);
                while (e1.swapLeftActivated() && e2.swapRightActivated()) {
                    // wait
                }
            }
        }
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
                if (!(emoticonArray[x][y].getType().equals(EMPTY))) {
                    Bitmap emptyBitmap = populator.getEmptyBitmap();
                    emoticonArray[x][y] = new EmptyEmoticon(x, y, emoWidth, emoHeight, emptyBitmap);
                }
            }
        }
    }

    private void shiftIconsDown() {
        int rowToShift;
        Bitmap emptyBitmap = populator.getEmptyBitmap();
        for (int x = 0; x < X_MAX; x++) {
            for (int y = Y_MAX - 1; y >= 0; y--) {
                rowToShift = 1;
                if (emoticonArray[x][y].getType().equals(EMPTY)) {
                    while ((y - rowToShift >= 0) && (emoticonArray[x][y - rowToShift].getType().equals(EMPTY))) {
                        rowToShift++;
                    }
                    // we now have the appropriate gap for this column to move everything down by
                    while (y >= 0) {
                        if (y - rowToShift >= 0) {
                            emoticonArray[x][y] = emoticonArray[x][y - rowToShift];
                            emoticonArray[x][y].setShiftDistance(rowToShift);
                            emoticonArray[x][y].setShiftDown(true); /** trying this */
                        } else {
                            emoticonArray[x][y] = new EmptyEmoticon(x, y, emoWidth, emoHeight, emptyBitmap);
                        }
                        y--;
                    }
                }
            }
        }

        boolean waiting = true;
        while (waiting) {
            waiting = false;
            for (int x = 0; x < X_MAX; x++) {
                for (int y = Y_MAX - 1; y >= 0; y--) {
                    if (emoticonArray[x][y].shiftDownActivated()) {
                        waiting = true;
                    }
                }
            }
        }
        updateEmoticonArray();
    }

    private void updateEmoticonArray() {
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                emoticonArray[x][y].setX(x);
                emoticonArray[x][y].setY(y);
            }
        }
    }

    private void insertNewEmoticons() {
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                if (emoticonArray[x][y].getType().equals(EMPTY)) {
                    Emoticon newEmoticon = populator.generateRandomEmoticon(x, y, emoWidth, emoHeight);
                    emoticonArray[x][y] = newEmoticon;
                }
            }
        }
    }
}