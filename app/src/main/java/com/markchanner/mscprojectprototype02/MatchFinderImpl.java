package com.markchanner.mscprojectprototype02;

import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Finds and returns an ArrayList that contains a List/Lists of rows or
 * columns that have a succession emoticons of the same type.
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class MatchFinderImpl implements MatchFinder {

    /**
     * Starts at bottom of column, where an emoticon is added to the List, then proceeds up the
     * column looking for matches, comparing each emoticon with the one last visited. Whilst
     * the emoticon is of the same type as the previous one, this emoticon
     * is added to the list. When an emoticon is reached that is of a different type, or when
     * the end of each column is reached, the List is checked. If the List contains 3 or more emoticons,
     * this List is added to the ArrayList, which is returned after the entire board has been checked.
     *
     * @param gameView the board to be checked for matching columns
     * @return An ArrayList that holds either a List/Lists of matching columns, or an empty list
     * if no matching rows are located on the board
     */
    @Override
    public ArrayList<LinkedList<Emoticon>> findMatchingColumns(GameView gameView) {
        Emoticon[][] emoticonArray = gameView.getEmoticonArray();
        int xMax = gameView.getX_MAX();
        int yMax = gameView.getY_MAX();

        LinkedList<Emoticon> consecutiveEmoticons = new LinkedList<>();
        ArrayList<LinkedList<Emoticon>> bigList = new ArrayList<>();
        Emoticon emoticon;

        for (int x = 0; x < xMax; x++) {
            consecutiveEmoticons.add(emoticonArray[x][yMax - 1]);

            for (int y = (yMax - 2); y >= 0; y--) {
                emoticon = emoticonArray[x][y];
                if (!emoticon.getType().equals(consecutiveEmoticons.getLast().getType())) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
                consecutiveEmoticons.add(emoticon);
                if (y == 0) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    /**
     * Starts at the bottom of the board, where an emoticon is added to the List. Then travels across
     * the row looking for matches, comparing each emoticon with the one last visited.
     * Whilst the emoticon visited is of the same type as the one on the previous one, this emoticon
     * is added to the list. When an emoticon is reached that is of a different type, or when
     * the end of each row is reached, the List is checked. If the List contains 3 or more emoticons,
     * this List is added to the ArrayList, which is returned after the entire board has been checked.
     *
     * @param gameView the board to be checked for matching rows
     * @return An ArrayList that holds either a List/Lists of matching rows, or an empty list
     * if no matching rows are located on the board
     */
    @Override
    public ArrayList<LinkedList<Emoticon>> findMatchingRows(GameView gameView) {
        Emoticon[][] emoticonArray = gameView.getEmoticonArray();
        int xMax = gameView.getX_MAX();
        int yMax = gameView.getY_MAX();
        LinkedList<Emoticon> consecutiveEmoticons = new LinkedList<>();
        ArrayList<LinkedList<Emoticon>> bigList = new ArrayList<>();
        Emoticon emoticon;

        for (int y = yMax - 1; y >= 0; y--) {
            consecutiveEmoticons.add(emoticonArray[0][y]);
            for (int x = 1; x < xMax; x++) {
                emoticon = emoticonArray[x][y];
                if (!(emoticon.getType().equals(consecutiveEmoticons.getLast().getType()))) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
                consecutiveEmoticons.add(emoticon);
                if (x == xMax - 1) {
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
            if (!(nextEmoticon.equals(previousEmoticon))) {
                return false;
            } else {
                previousEmoticon = nextEmoticon;
            }
        }
        return true;
    }
}