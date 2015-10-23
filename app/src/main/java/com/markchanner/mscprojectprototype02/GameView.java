package com.markchanner.mscprojectprototype02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * LIST OF CHANGES REGARDING PROBLEM WITH GRID:
 * 1. SET COLUMN AND ROW TO DIFFERENT SIZE SO CAN TELL IF SOMETHING WRONG
 * 2. onSizeChanged() method: emoticonWidth = screenWidth / X_MAX; - This makes more sense
 * 3. onDraw() method: Check all these values very carefully
 * 4. DECIDED TO CHANGE EVERYTHING TO X, Y - IT WILL AVOID CONFUSION
 *
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class GameView extends View {

    private Context context;
    private Paint backgroundColour;
    private Paint gridLineColour;
    private int screenWidth;
    private int screenHeight;
    private int emoticonWidth;
    private int emoticonHeight;


    private static final int X_VAL = 0;
    private static final int Y_VAL = 1;

    private final int X_MAX = 2;
    private final int Y_MAX = 2;
    private MatchFinder matchFinder;
    private BoardPopulator populator;
    private Tile[][] tiles;
    private int[] userSelection01 = new int[2];
    private int[] userSelection02 = new int[2];
    private boolean firstSelectionMade;

    public GameView(Context theContext, BoardPopulator bp, MatchFinderImpl mf) {
        super(theContext);
        context = theContext;
        context.getResources();
        populator = bp;
        matchFinder = mf;
        tiles = new TileImpl[X_MAX][Y_MAX];
        backgroundColour = new Paint();
        gridLineColour = new Paint();
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

/** Android screen coordinate referencing is in the opposite order (col,row)!!! */
    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        screenWidth = w;
        screenHeight = h;
        emoticonWidth = screenWidth / X_MAX;
        emoticonHeight = screenHeight / Y_MAX;
        populator.populate(this, context, emoticonWidth, emoticonHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        final int ZERO = 0;
        // Draws a rectangle with sky blue centre
        backgroundColour.setColor(Color.parseColor("#7EC0EE"));
        canvas.drawRect(ZERO, ZERO, getWidth(), getHeight(), backgroundColour);

        // Draws grid lines within rectangle
        gridLineColour.setStrokeWidth(2f);
        gridLineColour.setColor(Color.BLACK);
        /** !!!!!!!!!!!!!!!!!!!!! change this loop to x after and alter drawLine accordingly !!!!*/
        for (int y = 0; y < Y_MAX; y++) {
            // Draw  grid lines
            /** check if horizontal or vertical */
            canvas.drawLine(ZERO, y * emoticonHeight, getWidth(), y * emoticonHeight, gridLineColour);
            //Draw  grid lines
            canvas.drawLine(y * emoticonWidth, ZERO, y * emoticonWidth, getHeight(), gridLineColour);
        }
        // Draws emoticonList on canvas
        /** !!!!!!!!!!!!!!!!!!!!!!!confirm if horizontal or vertical */
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
        invalidate();
        return true;
    }

    public void selectTile(int x, int y) {
        if (!(firstSelectionMade)) {
            firstSelectionMade = true;
            userSelection01[X_VAL] = x;
            userSelection01[Y_VAL] = y;
        } else {
            if (!sameTileSelectedTwice()) {
                userSelection02[X_VAL] = x;
                userSelection02[Y_VAL] = y;
                checkValidSelections();
            }
        }
    }

    private void checkValidSelections() {
        if (!sameTileSelectedTwice()) {
            if (selectedTilesAreAdjacent()) {
                compareTileContents();
            } else {
                /*  Selections not adjacent. Last selection becomes first selection */
                userSelection01[X_VAL] = userSelection02[X_VAL];
                userSelection01[Y_VAL] = userSelection02[Y_VAL];
                userSelection02[X_VAL] = -1;
                userSelection02[Y_VAL] = -1;
            }
        } else {
            /* Same selection made twice. Reset selections */
            resetUserSelections();
        }
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

    private void compareTileContents() {
        if (differentPieceTypes()) {
            swapPieces();
            findMatches();
        } else {
            /* Both selections are of the same Emoticon. Reset selections */
        }
        resetUserSelections();
    }

    private boolean differentPieceTypes() {
        return (!(tiles[userSelection01[X_VAL]][userSelection01[Y_VAL]].getEmoticonType()
                .equals(tiles[userSelection02[X_VAL]][userSelection02[Y_VAL]].getEmoticonType())));
    }

    private void swapPieces() {
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
            // The swap did not yield a match, so pieces swapped back to previous position
            swapPieces();
        }
    }

    private boolean matchesFound(ArrayList<LinkedList<Tile>> matchingColumns, ArrayList<LinkedList<Tile>> matchingRows) {
        return (!(matchingColumns.isEmpty() && matchingRows.isEmpty()));
    }

    /** update variable names and probably method logic! */
    private void updateBoard(ArrayList<LinkedList<Tile>> matchingColumns, ArrayList<LinkedList<Tile>> matchingRows) {
        removeFromBoard(matchingColumns);
        removeFromBoard(matchingRows);
       /* do {
            //giveReward(matchingColumns, matchingRows);
            /*removeFromBoard(matchingColumns);
            removeFromBoard(matchingRows);
            shiftIconsDown();
            insertNewIcons();
            matchingColumns = matchFinder.findMatchingColumns(this);
            matchingRows = matchFinder.findMatchingRows(this);
        } while (matchesFound(matchingColumns, matchingRows));*/
    }

    private void giveReward(ArrayList<LinkedList<Tile>> matchingColumns, ArrayList<LinkedList<Tile>> matchingRows) {
        for (LinkedList<Tile> matchingColumn : matchingColumns) {
            //System.out.println(matchingColumn.getFirst().showType());
        }
        for (LinkedList<Tile> matchingRow : matchingRows) {
            //System.out.println(matchingRow.getFirst().showType());
        }
        //printList("Matching columns:", matchingColumns);
        //printList("Matching rows:", matchingRows);
    }

    private void removeFromBoard(ArrayList<LinkedList<Tile>> matches) {
        for (List<Tile> killList : matches) {
            for (Tile t : killList) {
                int x = t.getX();
                int y = t.getY();

                if (!(tiles[x][y].getEmoticonType().equals("EMPTY"))) {
                    Bitmap empty = populator.getEmptyBitmap();
                    tiles[x][y].setEmoticon(new EmptyEmoticon(empty));
                }
            }
        }
    }

    /** This will need changing when animation is involved */
    private void shiftIconsDown() {
        for (int x = 0; x < X_MAX; x++) {
            for (int y = 0; y < Y_MAX; y++) {
                if (tiles[x][y].getEmoticonType().equals("EMPTY")) {
                    // get any pieces higher up the column and, if found, plug hole with it
                    int tempY = y;
                    while ((tempY < Y_MAX) && (tiles[x][tempY].getEmoticonType().equals("EMPTY"))) {
                        tempY++;
                    }
                    if (tempY < Y_MAX) {
                        Emoticon e = tiles[x][tempY].getEmoticon();
                        tiles[x][y].setEmoticon(e);
                        /* sets previous tile to be empty */
                        Bitmap empty = populator.getEmptyBitmap();
                        tiles[x][tempY].setEmoticon(new EmptyEmoticon(empty));
                    }
                }
            }
        }
    }
    /** This will need changing when animation is involved */
    private void insertNewIcons() {
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
