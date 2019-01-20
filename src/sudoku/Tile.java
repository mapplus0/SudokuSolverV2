package sudoku;

public class Tile {

    private int value;

    public Tile() {
        this.value = 0;
    }

    public Tile(int value) {
        this.value = value;
        if (this.value < 0) this.value = 0;
        else if (this.value > 9) this.value = 9;
    }

    public void prevVal() {
        if (this.value == 0) {
            this.value = 9;
        } else {
            this.value--;
        }
    }

    public void nextVal() {
        if (this.value == 9) {
            this.value = 0;
        } else {
            this.value++;
        }
    }

    public boolean isFree() {
        return this.value==0;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
