package com.markchanner.mscprojectprototype02;

import java.util.LinkedList;
import java.util.ArrayList;

/**
 * @author Mark Channer for prototype of Birkbeck MSc Computer Science final project
 */
public class MatchFinder {

    public ArrayList<LinkedList<Emoticon>> findMatchingColumns(GameView gameView) {
        Emoticon[][] emoticonArray = gameView.getEmoticonArray();
        int rowSize = gameView.getRows();
        int colSize = gameView.getCols();

        LinkedList<Emoticon> consecutiveEmoticons = new LinkedList<>();
        ArrayList<LinkedList<Emoticon>> bigList = new ArrayList<>();
        Emoticon emoticon;
        for (int col = 0; col < colSize; col++) {
            consecutiveEmoticons.add(emoticonArray[colSize - 1][col]);

            for (int row = (rowSize - 2); row >= 0; row--) {
                emoticon = emoticonArray[row][col];
                if (!emoticon.getBitmap().sameAs(consecutiveEmoticons.getLast().getBitmap())) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
                consecutiveEmoticons.add(emoticon);
                if (row == 0) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    public ArrayList<LinkedList<Emoticon>> findMatchingRows(GameView gameView) {
        Emoticon[][] emoticonArray = gameView.getEmoticonArray();
        int rowSize = gameView.getRows();
        int colSize = gameView.getCols();
        LinkedList<Emoticon> consecutiveEmoticons = new LinkedList<>();
        ArrayList<LinkedList<Emoticon>> bigList = new ArrayList<>();
        Emoticon emoticon;

        for (int row = (rowSize - 1); row >= 0; row--) {
            consecutiveEmoticons.add(emoticonArray[row][0]);

            for (int col = 1; col < colSize; col++) {
                emoticon = emoticonArray[row][col];
                if (!emoticon.getBitmap().sameAs(consecutiveEmoticons.getLast().getBitmap())) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
                consecutiveEmoticons.add(emoticon);
                if (col == colSize - 1) {
                    examineList(consecutiveEmoticons, bigList);
                    consecutiveEmoticons = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    private void examineList(LinkedList<Emoticon> consecutiveEmoticons, ArrayList<LinkedList<Emoticon>> bigList) {
        if (consecutiveEmoticons.size() >= 3) {
            bigList.add(consecutiveEmoticons);
        }
    }
}
