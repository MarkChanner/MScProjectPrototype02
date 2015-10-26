package com.markchanner.mscprojectprototype02;

/**
 * An implementation of the Tile interface.
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class TileImpl implements Tile {

    private int x;
    private int y;
    private Emoticon emoticon;

    public TileImpl(int x, int y, Emoticon e) {
        this.x = x;
        this.y = y;
        emoticon = e;
        emoticon.setCoordinates(this.x, this.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Emoticon getEmoticon() {
        if (emoticon != null) {
            return emoticon.retrieveEmoticon();
        } else {
            throw new NullPointerException("Emoticon is not set");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEmoticon(Emoticon e) {
        emoticon = e;
        emoticon.setCoordinates(x, y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmoticonType() {
        if (emoticon != null) {
            return emoticon.showType();
        } else {
            throw new NullPointerException("Emoticon type is not set");
        }

    }
}
