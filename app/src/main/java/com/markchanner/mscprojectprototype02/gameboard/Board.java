package com.markchanner.mscprojectprototype02.gameboard;

import com.markchanner.mscprojectprototype02.gamepieces.Emoticon;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public interface Board {

    void updateEmoticonMovements();

    void processSelections(GameView view, Selection selections);

    Emoticon[][] getEmoticons();

}
