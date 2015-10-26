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
public class GameView extends View {

    private Context context;
    private SoundPool soundPool;
    private Paint backgroundColour;
    private Paint gridLineColour;
    private Paint selectionColor;

    int swapID = -1;
    private int emoticonWidth;
    private int emoticonHeight;
    private final Rect highlightedEmoticon = new Rect();


    private static final int X_VAL = 0;
    private static final int Y_VAL = 1;

    private final int X_MAX = 8;
    private final int Y_MAX = 7;
    private MatchFinder matchFinder;
    private BoardPopulator populator;
    private Tile[][] tiles;
    private int[] userSelection01 = new int[2];
    private int[] userSelection02 = new int[2];
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

        tiles = new TileImpl[X_MAX][Y_MAX];
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

    public Tile[][] getTiles() {
        if (tiles == null) {
            throw new NullPointerException(); /* Exceptions need work */
        } else {
            return tiles;
        }
    }

    private void resetUserSelections() {
        firstSelectionMade = false;
        userSelection01[X_VAL] = -1;
        userSelection01[Y_VAL] = -1;
        userSelection02[X_VAL] = -1;
        userSelection02[Y_VAL] = -1;
    }

    @Override
    protected void onSizeChanged(int screenWidth, int screenHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(screenWidth, screenHeight, oldWidth, oldHeight);
        emoticonWidth = screenWidth / X_MAX;
        emoticonHeight = screenHeight / Y_MAX;
        populator.populate(this, context, emoticonWidth, emoticonHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int ZERO = 0;
        backgroundColour.setColor(Color.parseColor("#7EC0EE"));
        // Draws a screen-sized rectangle with sky blue inside
        canvas.drawRect(ZERO, ZERO, getWidth(), getHeight(), backgroundColour);

        // Highlights a selected  tile
        selectionColor.setColor(Color.parseColor("#fff2a8"));
        canvas.drawRect(highlightedEmoticon, selectionColor);

        gridLineColour.setStrokeWidth(2f);
        gridLineColour.setColor(Color.BLACK);
        for (int i = 0; i < X_MAX; i++) {
            // Draws  horizontal grid lines
            canvas.drawLine(ZERO, i * emoticonHeight, getWidth(), i * emoticonHeight, gridLineColour);
            //Draw  vertical grid lines
            canvas.drawLine(i * emoticonWidth, ZERO, i * emoticonWidth, getHeight(), gridLineColour);
        }
        // Draws emoticons
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                Emoticon e = tiles[x][y].getEmoticon();
                canvas.drawBitmap(e.getBitmap(), x * emoticonWidth, y * emoticonHeight, null);
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
        //invalidate();
        return true;
    }

    public void selectTile(int x, int y) {
        if (!(firstSelectionMade)) {
            userSelection01[X_VAL] = x;
            userSelection01[Y_VAL] = y;
            highlightTile(userSelection01[X_VAL], userSelection01[Y_VAL]);
            firstSelectionMade = true;
        } else {
            userSelection02[X_VAL] = x;
            userSelection02[Y_VAL] = y;
            checkValidSelections();
        }
    }

    private void checkValidSelections() {
        deselectTile();
        if (!sameTileSelectedTwice()) {
            if (selectedTilesAreAdjacent()) {
                swapPieces();
                findMatches();
                resetUserSelections();
            } else {
                userSelection01[X_VAL] = userSelection02[X_VAL];
                userSelection01[Y_VAL] = userSelection02[Y_VAL];
                userSelection02[X_VAL] = -1;
                userSelection02[Y_VAL] = -1;
                highlightTile(userSelection01[X_VAL], userSelection01[Y_VAL]);
            }
        } else {
            resetUserSelections();
        }
    }

    private void highlightTile(int x, int y) {
        invalidate(highlightedEmoticon);
        highlightedEmoticon.set(x * emoticonWidth, y * emoticonHeight, x * emoticonWidth + emoticonWidth, y * emoticonHeight + emoticonHeight);
        invalidate(highlightedEmoticon);
    }

    private void deselectTile() {
        invalidate(highlightedEmoticon);
        highlightedEmoticon.setEmpty();
        invalidate(highlightedEmoticon);
    }

    private boolean sameTileSelectedTwice() {
        return ((userSelection01[X_VAL] == userSelection02[X_VAL]) && (userSelection01[Y_VAL] == userSelection02[Y_VAL]));
    }

    private boolean selectedTilesAreAdjacent() {
        if ((userSelection01[X_VAL] == userSelection02[X_VAL]) &&
                (userSelection01[Y_VAL] == (userSelection02[Y_VAL] + 1) || userSelection01[Y_VAL] == (userSelection02[Y_VAL] - 1))) {
            return true;
        } else if ((userSelection01[Y_VAL] == userSelection02[Y_VAL]) &&
                (userSelection01[X_VAL] == (userSelection02[X_VAL] + 1) || userSelection01[X_VAL] == (userSelection02[X_VAL] - 1))) {
            return true;
        }
        return false;
    }

    private void swapPieces() {
        soundPool.play(swapID, 1, 1, 0, 0, 1);
        Emoticon tempEmoticon = tiles[userSelection01[X_VAL]][userSelection01[Y_VAL]].getEmoticon();
        tiles[userSelection01[X_VAL]][userSelection01[Y_VAL]].setEmoticon(tiles[userSelection02[X_VAL]][userSelection02[Y_VAL]].getEmoticon());
        tiles[userSelection02[X_VAL]][userSelection02[Y_VAL]].setEmoticon(tempEmoticon);
    }

    private void findMatches() {
        ArrayList<LinkedList<Tile>> matchingX = matchFinder.findMatchingColumns(this);
        ArrayList<LinkedList<Tile>> matchingY = matchFinder.findMatchingRows(this);
        if (matchesFound(matchingX, matchingY)) {
            updateBoard(matchingX, matchingY);
        } else {
            swapPiecesBack();
        }
    }

    private boolean matchesFound(ArrayList<LinkedList<Tile>> matchingX, ArrayList<LinkedList<Tile>> matchingY) {
        return (!(matchingX.isEmpty() && matchingY.isEmpty()));
    }

    private void swapPiecesBack() {
        swapPieces();
    }

    private void updateBoard(ArrayList<LinkedList<Tile>> matchingColumns, ArrayList<LinkedList<Tile>> matchingRows) {
        removeFromBoard(matchingColumns);
        removeFromBoard(matchingRows);
        shiftIconsDown();
        //insertNewEmoticons();
        /*if (matchesFound(matchingColumns, matchingRows)) {
            removeFromBoard(matchingColumns);
            removeFromBoard(matchingRows);
            // shiftIconsDown();
            // insertNewEmoticons();
            matchingColumns = matchFinder.findMatchingColumns(this);
            matchingRows = matchFinder.findMatchingRows(this);
            updateBoard(matchingColumns, matchingRows);
        }*/
    }

    private void removeFromBoard(ArrayList<LinkedList<Tile>> matches) {
        for (List<Tile> removeList : matches) {
            for (Tile t : removeList) {
                int x = t.getX();
                int y = t.getY();
                if (!(tiles[x][y].getEmoticonType().equals("EMPTY"))) {
                    Bitmap empty = populator.getEmptyBitmap();
                    tiles[x][y].setEmoticon(new EmptyEmoticon(empty));
                }
            }
        }
    }

    private void shiftIconsDown() {
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                // If empty tile found, mark tile and proceed up column until
                // gets to end of column or finds a non-empty tile, in which
                // case this non-empty tile should be moved to the previously
                // marked tile and then set to empty
                if (tiles[x][y].getEmoticonType().equals("EMPTY")) {
                    int tempY = y;
                    while ((tempY < Y_MAX) && (tiles[x][tempY].getEmoticonType().equals("EMPTY"))) {
                        tempY++;
                    }

                    // If all tiles to the top of the column are empty, then
                    // the value of tempY will be equal to Y_MAX, not less,
                    // which means there are no icons in the column to shift down
                    if (tempY < Y_MAX) {
                        Emoticon shiftedEmoticon = tiles[x][tempY].getEmoticon();
                        tiles[x][y].setEmoticon(shiftedEmoticon);
                        Bitmap empty = populator.getEmptyBitmap();
                        tiles[x][tempY].setEmoticon(new EmptyEmoticon(empty));
                    }
                }
            }
        }
    }

    private void insertNewEmoticons() {
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                if (tiles[x][y].getEmoticonType().equals("EMPTY")) {
                    Emoticon e = populator.generateRandomEmoticon();
                    tiles[x][y].setEmoticon(e);
                }
            }
        }
    }
}
