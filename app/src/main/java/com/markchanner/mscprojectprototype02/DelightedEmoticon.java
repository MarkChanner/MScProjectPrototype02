package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class DelightedEmoticon extends AbstractEmoticon {

    public DelightedEmoticon(Bitmap b) {
        super (b, "DELIGHTED");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Emoticon retrieveEmoticon() {
        return (this);
    }

}



