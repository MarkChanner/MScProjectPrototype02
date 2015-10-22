package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class EmptyEmoticon extends AbstractEmoticon {

    public EmptyEmoticon(Bitmap b) {
        super (b, "EMPTY");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Emoticon retrieveEmoticon() {
        return (this);
    }

}



