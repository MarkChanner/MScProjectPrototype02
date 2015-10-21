package com.markchanner.mscprojectprototype02;

import java.util.LinkedList;
import java.util.ArrayList;

/**
 * @author Mark Channer for prototype of Birkbeck MSc Computer Science final project
 */
public class MatchFinder {

    public ArrayList<LinkedList<Emoticon>> findMatchingColumns(GameView gameView) {
        Emoticon[][] emoticonArray = gameView.getEmoticonArray();
        int numRows = gameView.getRows();
        int numCols = gameView.getCols();

        LinkedList<Emoticon> matchingEmoticons = new LinkedList<>();
        ArrayList<LinkedList<Emoticon>> bigList = new ArrayList<>();
        Emoticon emoticon;
        for (int col = 0; col < numCols; col++) {
            matchingEmoticons.add(emoticonArray[numRows - 1][col]);

            for (int row = (numRows - 2); row >= 0; row--) {
                emoticon = emoticonArray[row][col];
                if (!emoticon.getFace().equals(matchingEmoticons.getLast().getFace())) {
                    checkForThreeOrMoreMatches(matchingEmoticons, bigList);
                    matchingEmoticons = new LinkedList<>();
                }
                matchingEmoticons.add(emoticon);
                if (row == 0) {
                    checkForThreeOrMoreMatches(matchingEmoticons, bigList);
                    matchingEmoticons = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    /**
     * Starts scanning the board from the bottom row. Checks columns left to right in
     * that row, then moves up to the row above. This is so that matching emoticons
     * will be removed from the bottom of the board up.
     *
     * @param gameView the View of the game
     * @return An ArrayList containing 1 or more LinkedLists of matching emoticons
     */
    public ArrayList<LinkedList<Emoticon>> findMatchingRows(GameView gameView) {
        Emoticon[][] emoticonArray = gameView.getEmoticonArray();
        int numRows = gameView.getRows();
        int numCols = gameView.getCols();
        LinkedList<Emoticon> consecutiveEmoticons = new LinkedList<>();
        ArrayList<LinkedList<Emoticon>> bigList = new ArrayList<>();
        Emoticon emoticon;

        // Starts at bottom-left of board
        for (int row = (numRows - 1); row >= 0; row--) {
            // Adds first emoticon in the row to consecutiveEmoticons
            // list in preparation to begin the below algorithm
            consecutiveEmoticons.add(emoticonArray[row][0]);
            /*
             * Unless one of the below two conditions are met, adds current tile's
             * emoticon to consecutiveEmoticons list, then proceeds to next tile.
             *
             * Condition 1:
             * If emoticon at current tile does NOT match emoticon most recently added
             * to consecutiveEmoticons list (this will be the majority of the time),
             * calls checkForThreeOrMoreMatches, which checks if consecutiveEmoticons
             * contains 3 or more emoticons, which would mean that at least 3 of the last tiles
             * contained matching emoticons and need to be dealt with. This is handled
             * by adding the consecutiveEmoticons list to bigList, which will be returned
             * to the GameView when the whole view has been scanned to modify the view accordingly.
             *
             * Condition 2:
             * Having checked Condition 1 (and handled if met), add current tile's emoticon
             * to consecutiveEmoticons and call checkForThreeOrMoreMatches again. As this
             * is now the end of the current row, creates a new consecutiveEmoticons list and
             * either moves up a row, or if on the top row, returns bigList and exits
             */
            for (int col = 1; col < numCols; col++) {
                emoticon = emoticonArray[row][col];
                if (!emoticon.getFace().equals(consecutiveEmoticons.getLast().getFace())) {
                    checkForThreeOrMoreMatches(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
                consecutiveEmoticons.add(emoticon);

                if (col == numCols - 1) {
                    checkForThreeOrMoreMatches(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    private void checkForThreeOrMoreMatches(LinkedList<Emoticon> consecutiveEmoticons, ArrayList<LinkedList<Emoticon>> bigList) {
        // If the size of the list is 3 or more, this means
        // we have at least 3 matching emoticons, and these
        // will be added to bigList to deal with after
        if (consecutiveEmoticons.size() >= 3) {
            bigList.add(consecutiveEmoticons);
        }
    }
}
