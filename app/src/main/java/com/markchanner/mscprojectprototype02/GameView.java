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


    private static final int ZERO = 0;
    private static final int ONE = 1;

    private final int NUM_ROWS = 8;
    private final int NUM_COLS = 8;
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
        tiles = new TileImpl[NUM_ROWS][NUM_COLS];
        backgroundColour = new Paint();
        gridLineColour = new Paint();
        resetUserSelections();
    }

    public int getRows() {
        return NUM_ROWS;
    }

    public int getCols() {
        return NUM_COLS;
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
        userSelection01[ZERO] = -1;
        userSelection01[ONE] = -1;
        userSelection02[ZERO] = -1;
        userSelection02[ONE] = -1;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        screenWidth = w;
        screenHeight = h;
        emoticonWidth = screenWidth / NUM_ROWS;
        emoticonHeight = screenHeight / NUM_COLS;
        populator.populate(this, context, emoticonWidth, emoticonHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Draws a rectangle with sky blue centre
        backgroundColour.setColor(Color.parseColor("#7EC0EE"));
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundColour);

        // Draws grid lines within rectangle
        gridLineColour.setStrokeWidth(2f);
        gridLineColour.setColor(Color.BLACK);
        for (int i = 0; i < NUM_ROWS; i++) {
            canvas.drawLine(0, i * emoticonHeight, getWidth(), i * emoticonHeight, gridLineColour);
            canvas.drawLine(i * emoticonWidth, 0, i * emoticonWidth, getHeight(), gridLineColour);
        }
        // Draws emoticonList on canvas
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                Emoticon e = tiles[row][col].getEmoticon();
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
            if (!sameTileSelectedTwice()) {
                userSelection02[ZERO] = row;
                userSelection02[ONE] = column;
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
                userSelection01[ZERO] = userSelection02[ZERO];
                userSelection01[ONE] = userSelection02[ONE];
                userSelection02[ZERO] = -1;
                userSelection02[ONE] = -1;
            }
        } else {
            /* Same selection made twice. Reset selections */
            resetUserSelections();
        }
    }

    private boolean sameTileSelectedTwice() {
        return ((userSelection01[ZERO] == userSelection02[ZERO]) && (userSelection01[ONE] == userSelection02[ONE]));
    }

    private boolean selectedTilesAreAdjacent() {
        if ((userSelection01[ZERO] == userSelection02[ZERO]) &&
                (userSelection01[ONE] == (userSelection02[ONE] + 1) || userSelection01[ONE] == (userSelection02[ONE] - 1))) {
            return true;
        } else if ((userSelection01[ONE] == userSelection02[ONE]) &&
                (userSelection01[ZERO] == (userSelection02[ZERO] + 1) || userSelection01[ZERO] == (userSelection02[ZERO] - 1))) {
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
        return (!(tiles[userSelection01[ZERO]][userSelection01[ONE]].getEmoticonType()
                .equals(tiles[userSelection02[ZERO]][userSelection02[ONE]].getEmoticonType())));
    }

    private void swapPieces() {
        Emoticon tempEmoticon = tiles[userSelection01[ZERO]][userSelection01[ONE]].getEmoticon();
        tiles[userSelection01[ZERO]][userSelection01[ONE]].setEmoticon(tiles[userSelection02[ZERO]][userSelection02[ONE]].getEmoticon());
        tiles[userSelection02[ZERO]][userSelection02[ONE]].setEmoticon(tempEmoticon);
    }

    private void findMatches() {
        ArrayList<LinkedList<Tile>> matchingColumns = matchFinder.findMatchingColumns(this);
        ArrayList<LinkedList<Tile>> matchingRows = matchFinder.findMatchingRows(this);
        if (matchesFound(matchingColumns, matchingRows)) {
            updateBoard(matchingColumns, matchingRows);
        } else {
            // The swap did not yield a match, so pieces swapped back to previous position
            swapPieces();
        }
    }

    private void checkForThreeOrMoreMatches(LinkedList<Tile> consecutiveEmoticons, ArrayList<LinkedList<Tile>> bigList) {
        if (consecutiveEmoticons.size() >= 3) {
            bigList.add(consecutiveEmoticons);
        }
    }

    private boolean matchesFound(ArrayList<LinkedList<Tile>> matchingColumns, ArrayList<LinkedList<Tile>> matchingRows) {
        return (!(matchingColumns.isEmpty() && matchingRows.isEmpty()));
    }

    private void updateBoard(ArrayList<LinkedList<Tile>> matchingColumns, ArrayList<LinkedList<Tile>> matchingRows) {
        removeFromBoard(matchingColumns);
        removeFromBoard(matchingRows);
        /*do {
            //giveReward(matchingColumns, matchingRows);
            removeFromBoard(matchingColumns);
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
        for (List<Tile> rowList : matches) {
            for (Tile t : rowList) {
                int theRow = t.getRow();
                int theCol = t.getColumn();
                if (!(tiles[theRow][theCol].getEmoticonType().equals("EMPTY"))) {
                    Bitmap empty = populator.getEmptyBitmap();
                    tiles[theRow][theCol].setEmoticon(new EmptyEmoticon(empty));
                }
            }
        }
    }

    private void shiftIconsDown() {
        for (int col = 0; col < NUM_COLS; col++) {
            for (int row = (NUM_ROWS - 1); row >= 0; row--) {
                if (tiles[row][col].getEmoticonType().equals("EMPTY")) {
                    // get any pieces higher up the column and, if found, plug hole with it
                    int tempRow = row;
                    while ((tempRow >= 0) && (tiles[tempRow][col].getEmoticonType().equals("EMPTY"))) {
                        tempRow--;
                    }
                    if (tempRow >= 0) {
                        Emoticon e = tiles[tempRow][col].getEmoticon();
                        tiles[row][col].setEmoticon(e);
                        /* sets previous tile to be empty */
                        Bitmap empty = populator.getEmptyBitmap();
                        tiles[tempRow][col].setEmoticon(new EmptyEmoticon(empty));
                    }
                }
            }
        }
    }

    private void insertNewIcons() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (tiles[row][col].getEmoticonType().equals("EMPTY")) {
                    Emoticon e = populator.generateRandomEmoticon();
                    tiles[row][col].setEmoticon(e);
                }
            }
        }
    }
}
