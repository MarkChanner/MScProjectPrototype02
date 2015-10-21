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
import java.util.LinkedList;
import java.util.List;
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
    private Bitmap emptyBitmap;
    private int emoticonWidth;
    private int emoticonHeight;
    private final int NUM_ROWS = 8;
    private final int NUM_COLS = 8;
    private Emoticon[][] emoticonArray;
    private MatchFinder matchFinder;
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
        emoticonArray = new Emoticon[NUM_ROWS][NUM_COLS];
        matchFinder = new MatchFinder(); /** Consider initializing in GameActivity and passing to GameViewConstructor, like Java version */
        resetUserSelections();
    }

    public int getRows() {
        return NUM_ROWS;
    }

    public int getCols() {
        return NUM_COLS;
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
        createEmoticons();
    }
    /* Split into scaleBitmaps() and createEmoticons() */
    protected void createEmoticons() {
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

        // Populates array with random emoticon varieties
        Emoticon newEmoticon;
        for (int rowID = 0; rowID < NUM_ROWS; rowID++) {
            for (int columnID = 0; columnID < NUM_COLS; columnID++) {
                // If the randomly generated emoticon would mean 3 of the same
                // type in a row keep randomly generating a different emoticon
                do {
                    newEmoticon = generateRandomEmoticon(rowID, columnID);
                } while ((rowID >= 2 && (newEmoticon.getFace()
                        .equals(emoticonArray[rowID - 1][columnID].getFace()) &&
                        newEmoticon.getFace().equals(emoticonArray[rowID - 2][columnID].getFace()))) ||
                        (columnID >= 2 &&
                                (newEmoticon.getFace().equals(emoticonArray[rowID][columnID - 1].getFace()) &&
                                        newEmoticon.getFace().equals(emoticonArray[rowID][columnID - 2].getFace()))));

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
                emoticon = new AngryEmoticon(angryBitmap, rowID, columnID);
                break;
            case 1:
                emoticon = new DelightedEmoticon(delightedBitmap, rowID, columnID);
                break;
            case 2:
                emoticon = new EmbarrassedEmoticon(embarrassedBitmap, rowID, columnID);
                break;
            case 3:
                emoticon = new SurprisedEmoticon(surprisedBitmap, rowID, columnID);
                break;
            case 4:
                emoticon = new UpsetEmoticon(upsetBitmap, rowID, columnID);
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
        for (int i = 0; i < NUM_ROWS; i++) {
            canvas.drawLine(0, i * emoticonHeight, getWidth(), i * emoticonHeight, gridLineColour);
            canvas.drawLine(i * emoticonWidth, 0, i * emoticonWidth, getHeight(), gridLineColour);
        }
        // Draws emoticonList on canvas
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
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
            /* Both selections are of the same Tile. Reset selections */
        }
        resetUserSelections();
    }

    private boolean differentPieceTypes() {
        return (!(emoticonArray[userSelection01[ZERO]][userSelection01[ONE]].getFace()
                .equals(emoticonArray[userSelection02[ZERO]][userSelection02[ONE]].getFace())));
    }

    private void swapPieces() {
        Emoticon tempEmoticon = emoticonArray[userSelection01[ZERO]][userSelection01[ONE]];
        emoticonArray[userSelection01[ZERO]][userSelection01[ONE]] = emoticonArray[userSelection02[ZERO]][userSelection02[ONE]];
        emoticonArray[userSelection02[ZERO]][userSelection02[ONE]] = tempEmoticon;
    }

    private void findMatches() {
        ArrayList<LinkedList<Emoticon>> matchingColumns = matchFinder.findMatchingColumns(this);
        ArrayList<LinkedList<Emoticon>> matchingRows = matchFinder.findMatchingRows(this);
        if (matchesFound(matchingColumns, matchingRows)) {
            //updateBoard(matchingColumns, matchingRows);
        } else {
            // The swap did not yield a match, so pieces swapped back to previous position
            swapPieces();
        }
    }

    private boolean matchesFound(ArrayList<LinkedList<Emoticon>> matchingColumns, ArrayList<LinkedList<Emoticon>> matchingRows) {
        return (!(matchingColumns.isEmpty() && matchingRows.isEmpty()));
    }

    private void updateBoard(ArrayList<LinkedList<Emoticon>> matchingColumns, ArrayList<LinkedList<Emoticon>> matchingRows) {
        do {
            //giveReward(matchingColumns, matchingRows);
            removeFromBoard(matchingColumns);
            removeFromBoard(matchingRows);
            shiftIconsDown();
            insertNewIcons();
            matchingColumns = matchFinder.findMatchingColumns(this);
            matchingRows = matchFinder.findMatchingRows(this);
        } while (matchesFound(matchingColumns, matchingRows));
    }

    private void giveReward(ArrayList<LinkedList<Emoticon>> matchingColumns, ArrayList<LinkedList<Emoticon>> matchingRows) {
        for (LinkedList<Emoticon> matchingColumn : matchingColumns) {
            //System.out.println(matchingColumn.getFirst().getFace());
        }
        for (LinkedList<Emoticon> matchingRow : matchingRows) {
            //System.out.println(matchingRow.getFirst().getFace());
        }
        //printList("Matching columns:", matchingColumns);
        //printList("Matching rows:", matchingRows);
    }

    private void removeFromBoard(ArrayList<LinkedList<Emoticon>> matches) {
        for (List<Emoticon> rowList : matches) {
            for (Emoticon e : rowList) {
                int theRow = e.getRow();
                int theCol = e.getColumn();
                if (!(emoticonArray[theRow][theCol].getFace().equals("EMPTY"))) {
                    emoticonArray[theRow][theCol] = new EmptyEmoticon(emptyBitmap,theRow, theCol);
                }
            }
        }
    }

    private void shiftIconsDown() {
        for (int col = 0; col < NUM_COLS; col++) {
            for (int row = (NUM_ROWS - 1); row >= 0; row--) {
                if (emoticonArray[row][col].getFace().equals("EMPTY")) {
                    // get any pieces higher up the column and, if found, plug hole with it
                    int tempRow = row;
                    while ((tempRow >= 0) && (emoticonArray[tempRow][col].getFace().equals("EMPTY"))) {
                        tempRow--;
                    }
                    if (tempRow >= 0) {
                        Emoticon e = emoticonArray[tempRow][col];
                        emoticonArray[row][col] = e;
                        /* sets previous tile to be empty */
                        emoticonArray[tempRow][col] = new EmptyEmoticon(emptyBitmap, row, col);
                    }
                }
            }
        }
    }

    private void insertNewIcons() {
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                if (emoticonArray[row][col].getFace().equals("EMPTY")) {
                    emoticonArray[row][col] = generateRandomEmoticon(row, col);
                }
            }
        }
    }
}
