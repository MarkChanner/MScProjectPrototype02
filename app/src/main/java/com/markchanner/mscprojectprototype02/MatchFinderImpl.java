package com.markchanner.mscprojectprototype02;

import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Finds and returns an ArrayList that contains a List/Lists of rows or columns that has a
 * succession of tiles that contain a game piece of the same type.
 *
 * @author Mark Channer for Birkbeck MSc Computer Science project
 */
public class MatchFinderImpl implements MatchFinder {

    /**
     * Starts at column 0, where a tile is added to the List, then proceeds up the column looking
     * for matches, comparing each tile's game piece with the one on the tile last visited. Whilst
     * the tile visited has the same type of game piece as the one on the previous tile, this tile
     * is added to the list. When a tile is reached that has a different type game piece, or when
     * the end of each column is reached, the List is checked. If the List contains 3 or more game pieces,
     * this List is added to the ArrayList, which is returned after the entire board has been checked.
     *
     * @param gameView the board to be checked for matching columns
     * @return An ArrayList that holds either a List/Lists of matching columns, or an empty list
     * if no matching rows are located on the board
     */
    @Override
    public ArrayList<LinkedList<Tile>> findMatchingColumns(GameView gameView) {
        Tile[][] tiles = gameView.getTiles();
        int xMax = gameView.getX_MAX();
        int yMax = gameView.getY_MAX();

        LinkedList<Tile> consecutivePieces = new LinkedList<>();
        ArrayList<LinkedList<Tile>> bigList = new ArrayList<>();
        Tile tile;
        for (int x = 0; x < xMax; x++) {
            consecutivePieces.add(tiles[x][0]);

            for (int y = 1; y < yMax; y++) {
                tile = tiles[x][y];
                if (!tile.getEmoticonType().equals(consecutivePieces.getLast().getEmoticonType())) {
                    examineList(consecutivePieces, bigList);
                    consecutivePieces = new LinkedList<>();
                }
                consecutivePieces.add(tile);
                if (y == yMax - 1) {
                    examineList(consecutivePieces, bigList);
                    consecutivePieces = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    /**
     * Starts at the bottom of the board (rowSize - 1), where a tile is added to the List. Then travels across
     * the row looking for matches, comparing each tile's game piece with the one on the tile last visited.
     * Whilst the tile visited has the same type of game piece as the one on the previous tile, this tile
     * is added to the list. When a tile is reached that has a different type game piece, or when
     * the end of each row is reached, the List is checked. If the List contains 3 or more game pieces,
     * this List is added to the ArrayList, which is returned after the entire board has been checked.
     *
     * @param gameView the board to be checked for matching rows
     * @return An ArrayList that holds either a List/Lists of matching rows, or an empty list
     * if no matching rows are located on the board
     */
    @Override
    public ArrayList<LinkedList<Tile>> findMatchingRows(GameView gameView) {
        Tile[][] tiles = gameView.getTiles();
        int xMax = gameView.getX_MAX();
        int yMax = gameView.getY_MAX();
        LinkedList<Tile> consecutiveTiles = new LinkedList<>();
        ArrayList<LinkedList<Tile>> bigList = new ArrayList<>();
        Tile tile;

        for (int y = 0; y < yMax; y++) {
            consecutiveTiles.add(tiles[0][y]);

            for (int x = 1; x < xMax; x++) {
                tile = tiles[x][y];
                if (!tile.getEmoticonType().equals(consecutiveTiles.getLast().getEmoticonType())) {
                    examineList(consecutiveTiles, bigList);
                    consecutiveTiles = new LinkedList<>();
                }
                consecutiveTiles.add(tile);
                if (x == xMax - 1) {
                    examineList(consecutiveTiles, bigList);
                    consecutiveTiles = new LinkedList<>();
                }
            }
        }
        return bigList;
    }

    private void examineList(LinkedList<Tile> consecutivePieces, ArrayList<LinkedList<Tile>> bigList) {
        if (consecutivePieces.size() >= 3) {
            bigList.add(consecutivePieces);
        }
    }
}