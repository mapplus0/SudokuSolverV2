package sudoku.rcs;

import sudoku.Tile;

public class Square extends RCS {

    public void setTile(int row, int column, Tile tile) {
        if (row < 0 || row > 2 || column < 0 || column > 2) return;
        this.tileList.set(row*3+column, tile);
    }

    public void setTileVal(int row, int column, int val) {
        if (row < 0 || row > 2 || column < 0 || column > 2) return;
        if (this.tileList.get(row*3+column).isFree()) this.tileList.get(row*3+column).setValue(val);
    }

    public Tile getTile(int row, int column) {
        if (row < 0 || row > 2 || column < 0 || column > 2) return null;
        return this.tileList.get(row*3+column);
    }

    public int getTileVal(int row, int column) {
        if (row < 0 || row > 2 || column < 0 || column > 2) return -1;
        return this.tileList.get(row*3+column).getValue();
    }

}
