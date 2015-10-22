package com.markchanner.mscprojectprototype02;

/**
 * An implementation of the AbstractEmoticon interface. Implements all interface
 * methods. A GamePiece object
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class TileImpl implements Tile {

    private int row;
    private int column;
    private Emoticon emoticon;

    public TileImpl(int r, int c, Emoticon e) {
        row = r;
        column = c;
        emoticon = e;
        emoticon.setCoordinates(row, column);
    }

    public TileImpl(int r, int c) {
        row = r;
        column = c;
        emoticon = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRow() {
        return row;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumn() {
        return column;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Emoticon getEmoticon() {
        if (emoticon != null) {
            return emoticon.retrieveEmoticon();
        } else {
            throw new NullPointerException("GamePiece is not set");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEmoticon(Emoticon e) {
        emoticon = e;
        emoticon.setCoordinates(row, column);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmoticonType() {
        if (emoticon != null) {
            return emoticon.showType();
        } else {
            throw new NullPointerException("GameInitializer Piece is not set");
        }

    }
}
