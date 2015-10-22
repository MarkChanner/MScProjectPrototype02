package com.markchanner.mscprojectprototype02;

import android.graphics.Bitmap;

/**
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class UpsetEmoticon extends AbstractEmoticon {

    public UpsetEmoticon(Bitmap b) {
        super (b, "UPSET");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Emoticon retrieveEmoticon() {
        return (this);
    }

}



