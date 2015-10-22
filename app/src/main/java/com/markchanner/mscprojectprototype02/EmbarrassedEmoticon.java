package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class EmbarrassedEmoticon extends AbstractEmoticon {

    public EmbarrassedEmoticon(Bitmap b) {
        super (b, "EMBARRASSED");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Emoticon retrieveEmoticon() {
        return (this);
    }

}



