package com.markchanner.mscprojectprototype02;


import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class BoardImpl implements Board {

    public static final int X = 0;
    public static final int Y = 1;
    public static final int X_MAX = 8;
    public static final int Y_MAX = 7;
    public static final int ROW_START = 0;
    public static final int COLUMN_TOP = 0;
    public static final int COLUMN_BOTTOM = (Y_MAX - 1);

    public static final String EMPTY = "EMPTY";
    public static final String INVALID_MOVE = "INVALID_MOVE";
    public static final String MATCH_FOUND = "MATCH_FOUND";
    public static final int ONE_SECOND = 1000;

    private SoundManager soundManager;
    private int emoticonWidth;
    private int emoticonHeight;
    private Emoticon[][] emoticons;
    private BoardPopulator populator;


    public BoardImpl(Context context, int emoticonWidth, int emoticonHeight) {
        soundManager = new SoundManager();
        soundManager.loadSound(context);
        this.emoticonWidth = emoticonWidth;
        this.emoticonHeight = emoticonHeight;
        emoticons = new AbstractEmoticon[X_MAX][Y_MAX];
        populator = new BoardPopulatorImpl();
        populator.populateBoard(context, this, emoticonWidth, emoticonHeight);
    }

    @Override
    public void updateEmoticonMovements() {
        for (int y = COLUMN_BOTTOM; y >= COLUMN_TOP; y--) {
            for (int x = ROW_START; x < X_MAX; x++) {
                emoticons[x][y].update();
            }
        }
    }

    @Override
    public void processSelections(GameView view, Selection selections) {
        int[] sel1 = selections.getSelection01();
        int[] sel2 = selections.getSelection02();
        swapSelectedEmoticons(sel1, sel2);

        ArrayList<LinkedList<Emoticon>> matchingX = findMatchingColumns();
        ArrayList<LinkedList<Emoticon>> matchingY = findMatchingRows();

        if (matchesFound(matchingX, matchingY)) {
            modifyBoard(view, matchingX, matchingY);
        } else {
            soundManager.playSound(INVALID_MOVE);
            swapBack(sel1, sel2);
        }
        selections.resetUserSelections();
    }

    public void swapSelectedEmoticons(int[] sel1, int[] sel2) {

        Emoticon tempEmoticon1 = emoticons[sel1[X]][sel1[Y]];
        emoticons[sel1[X]][sel1[Y]] = emoticons[sel2[X]][sel2[Y]];
        emoticons[sel2[X]][sel2[Y]] = tempEmoticon1;

        updateEmoticonValues();

        Emoticon e1 = emoticons[sel1[X]][sel1[Y]];
        Emoticon e2 = emoticons[sel2[X]][sel2[Y]];
        int emo01X = e1.getArrayX();
        int emo01Y = e1.getArrayY();
        int emo02X = e2.getArrayX();
        int emo02Y = e2.getArrayY();

        if (emo01X == emo02X) {
            if (emo01Y < emo02Y) {
                e1.setSwapUp(true);
                e2.setSwapDown(true);
            } else if (emo01Y > emo02Y) {
                e1.setSwapDown(true);
                e2.setSwapUp(true);
            }
        } else if (emo01Y == emo02Y) {
            if (emo01X < emo02X) {
                e1.setSwapLeft(true);
                e2.setSwapRight(true);
            } else if (emo01X > emo02X) {
                e1.setSwapRight(true);
                e2.setSwapLeft(true);
            }
        }

        while ((e1.swapUpActivated() || e1.swapDownActivated() || e1.swapLeftActivated() || e1.swapRightActivated()) ||
                (e2.swapUpActivated() || e2.swapDownActivated() || e2.swapLeftActivated() || e2.swapRightActivated())) {
            // wait
        }
    }

    public void swapBack(int[] sel1, int[] sel2) {
        swapSelectedEmoticons(sel1, sel2);
    }

    private void updateEmoticonValues() {
        for (int y = COLUMN_BOTTOM; y >= COLUMN_TOP; y--) {
            for (int x = ROW_START; x < X_MAX; x++) {
                emoticons[x][y].setArrayX(x);
                emoticons[x][y].setArrayY(y);
            }
        }
    }

    public ArrayList<LinkedList<Emoticon>> findMatchingColumns() {
        LinkedList<Emoticon> consecutiveEmoticons = new LinkedList<>();
        ArrayList<LinkedList<Emoticon>> bigList = new ArrayList<>();
        Emoticon emoticon;
        for (int x = ROW_START; x < X_MAX; x++) {
            consecutiveEmoticons.add(emoticons[x][COLUMN_BOTTOM]);

            for (int y = (COLUMN_BOTTOM - 1); y >= COLUMN_TOP; y--) {
                emoticon = emoticons[x][y];
                if (!emoticon.getType().equals(consecutiveEmoticons.getLast().getType())) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
                consecutiveEmoticons.add(emoticon);
                if (y == COLUMN_TOP) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    public ArrayList<LinkedList<Emoticon>> findMatchingRows() {
        LinkedList<Emoticon> consecutiveEmoticons = new LinkedList<>();
        ArrayList<LinkedList<Emoticon>> bigList = new ArrayList<>();
        Emoticon emoticon;
        for (int y = COLUMN_BOTTOM; y >= COLUMN_TOP; y--) {
            consecutiveEmoticons.add(emoticons[ROW_START][y]);

            for (int x = (ROW_START + 1); x < X_MAX; x++) {
                emoticon = emoticons[x][y];
                if (!(emoticon.getType().equals(consecutiveEmoticons.getLast().getType()))) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
                consecutiveEmoticons.add(emoticon);
                if (x == (X_MAX - 1)) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    private void examineList(LinkedList<Emoticon> consecutiveEmotions, ArrayList<LinkedList<Emoticon>> bigList) {
        if ((consecutiveEmotions.size() >= 3) && (allSameType(consecutiveEmotions))) {
            bigList.add(consecutiveEmotions);
        }
    }

    private boolean allSameType(LinkedList<Emoticon> consecutiveEmoticons) {
        String previousEmoticon = consecutiveEmoticons.getFirst().getType();
        String nextEmoticon;
        for (int i = 1; i < consecutiveEmoticons.size(); i++) {
            nextEmoticon = consecutiveEmoticons.get(i).getType();
            if (nextEmoticon.equals(EMPTY) || (!(nextEmoticon.equals(previousEmoticon)))) {
                return false;
            } else {
                previousEmoticon = nextEmoticon;
            }
        }
        return true;
    }

    private boolean matchesFound(ArrayList<LinkedList<Emoticon>> matchingX, ArrayList<LinkedList<Emoticon>> matchingY) {
        return (!(matchingX.isEmpty() && matchingY.isEmpty()));
    }

    public void modifyBoard(GameView view, ArrayList<LinkedList<Emoticon>> matchingX, ArrayList<LinkedList<Emoticon>> matchingY) {
        do {
            giveReward(view, matchingX, matchingY);
            removeFromBoard(matchingX);
            removeFromBoard(matchingY);
            lowerEmoticons();
            matchingX = findMatchingColumns();
            matchingY = findMatchingRows();
        } while (matchesFound(matchingX, matchingY));
    }

    public void giveReward(GameView view, ArrayList<LinkedList<Emoticon>> matchingX, ArrayList<LinkedList<Emoticon>> matchingY) {
        if (!(matchingX.isEmpty())) {
            String matchingTypeX = matchingX.get(0).getFirst().getType();
            soundManager.playSound(matchingTypeX);
        } else if (!(matchingY.isEmpty())) {
            String matchingTypeY = matchingY.get(0).getFirst().getType();
            soundManager.playSound(matchingTypeY);
        }
        highlightMatches(matchingX);
        highlightMatches(matchingY);
        view.control(ONE_SECOND);
    }

    private void highlightMatches(ArrayList<LinkedList<Emoticon>> matches) {
        for (List<Emoticon> removeList : matches) {
            for (Emoticon e : removeList) {
                e.setIsPartOfMatch(true);
            }
        }
    }

    public void removeFromBoard(ArrayList<LinkedList<Emoticon>> matches) {
        for (List<Emoticon> removeList : matches) {
            for (Emoticon e : removeList) {
                int x = e.getArrayX();
                int y = e.getArrayY();
                if (!(emoticons[x][y].getType().equals(EMPTY))) {
                    emoticons[x][y] = populator.getEmptyEmoticon(x, y, emoticonWidth, emoticonHeight);
                }
            }
        }
    }

    public void lowerEmoticons() {
        int offScreenStartPosition;
        int runnerY;

        for (int x = ROW_START; x < X_MAX; x++) {

            offScreenStartPosition = 0;

            for (int y = COLUMN_BOTTOM; y >= COLUMN_TOP; y--) {

                if (emoticons[x][y].getType().equals(EMPTY)) {
                    runnerY = y;
                    while ((runnerY >= COLUMN_TOP) && (emoticons[x][runnerY].getType().equals(EMPTY))) {
                        runnerY--;
                    }
                    if (runnerY >= COLUMN_TOP) {
                        emoticons[x][y] = emoticons[x][runnerY];
                        emoticons[x][y].setArrayY(emoticons[x][runnerY].getArrayY());
                        emoticons[x][runnerY] = populator.getEmptyEmoticon(x, runnerY, emoticonWidth, emoticonHeight);
                    } else {
                        offScreenStartPosition--;
                        emoticons[x][y] = populator.generateEmoticon(x, y, emoticonWidth, emoticonHeight, offScreenStartPosition);
                    }

                    emoticons[x][y].setLowerEmoticon(true);
                }
            }
        }
        updateEmoticonValues();
        waitForAnimationToFinish();
    }

    private void waitForAnimationToFinish() {
        boolean waiting = true;
        while (waiting) {
            waiting = false;
            for (int y = COLUMN_BOTTOM; y >= COLUMN_TOP; y--) {
                for (int x = ROW_START; x < X_MAX; x++) {
                    if (emoticons[x][y].lowerEmoticonActivated()) {
                        waiting = true;
                    }
                }
            }
        }
    }

    @Override
    public Emoticon[][] getEmoticons() {
        if (emoticons == null) {
            throw new NullPointerException();
        } else {
            return emoticons;
        }
    }
}
