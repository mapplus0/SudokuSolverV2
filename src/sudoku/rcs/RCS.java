package sudoku.rcs;

import sudoku.Tile;

import java.util.ArrayList;
import java.util.List;

public abstract class RCS {

    protected List<Tile> tileList;

    public RCS() {
        this.tileList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            this.tileList.add(null);
        }
    }

    public void setTile(int i, Tile tile) {
        this.tileList.set(i,tile);
    }

    public void setTileVal(int i, int val) {
        if (this.tileList.get(i).isFree()) this.tileList.get(i).setValue(val);
    }

    public Tile getTile(int i) {
        return this.tileList.get(i);
    }

    public int getTileVal(int i) {
        return this.tileList.get(i).getValue();
    }

    public List<Tile> getTileList() {
        return tileList;
    }
}
