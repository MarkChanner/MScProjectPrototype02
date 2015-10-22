package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class SurprisedEmoticon extends AbstractEmoticon {

    public SurprisedEmoticon(Bitmap b) {
        super(b, "SURPRISED");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Emoticon retrieveEmoticon() {
        return (this);
    }

}



