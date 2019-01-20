package sudoku;

import sudoku.rcs.Column;
import sudoku.rcs.Row;
import sudoku.rcs.Square;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Gameplan {

    private static Gameplan currentGameplan;

    private List<Row> rows;
    private List<Column> columns;
    private List<Square> squares;

    public Gameplan(String fileName) {
        try {
            this.init(fileName);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public static Gameplan getCurrentGameplan() {
        return currentGameplan;
    }

    public static void setCurrentGameplan(Gameplan currentGameplan) {
        Gameplan.currentGameplan = currentGameplan;
    }

    public void init(String name) throws Exception {
        this.rows = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.squares = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            this.rows.add(new Row());
            this.columns.add(new Column());
            this.squares.add(new Square());
        }

        Tile[][] plan = new Tile[9][9];

        if (name != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(Gameplan.class.getResourceAsStream(name)));
            int row = 0;
            String line;
            while ((line = br.readLine()) != null && row < 9) {
                for (int col = 0; col < line.length(); col++) {
                    plan[row][col] = new Tile(Integer.parseInt(String.valueOf(line.charAt(col))));
                }
                row++;
            }
        } else {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    plan[row][col] = new Tile();
                }
            }
        }


        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                this.rows.get(row).setTile(col, plan[row][col]);
                this.columns.get(col).setTile(row, plan[row][col]);
                this.squares.get((row/3)*3+col/3).setTile(row%3,col%3, plan[row][col]);
            }
        }
    }

    public boolean isSolved() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (this.getRow(row).getTile(col).isFree()) return false;
            }
        }
        return true;
    }

    public Gameplan deepCopy() {
        Gameplan gameplan = new Gameplan(null);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                gameplan.getTile(row,col).setValue(this.getTile(row,col).getValue());
            }
        }

        return gameplan;
    }

    public Tile getTile(int row, int column) {
        return this.rows.get(row).getTile(column);
    }

    public Row getRow(int row) {
        return this.rows.get(row);
    }

    public Column getColumn(int column) {
        return this.columns.get(column);
    }

    public Square getSquareFromTile(int tileRow, int tileCol) {
        return this.squares.get((tileRow/3)*3+tileCol/3);
    }

    public void print() {
        for (int row = 0; row < 9; row++) {
            printRow(row);
        }
    }

    public void printRow(int row) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            builder.append(this.rows.get(row).getTile(i).getValue());
        }
        System.out.println(builder.toString());
    }

    public void printCol(int col) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            builder.append(this.rows.get(col).getTile(i).getValue());
        }
        System.out.println(builder.toString());
    }

    public void printSquare(int square) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            builder.append(this.rows.get(square).getTile(i).getValue());
        }
        System.out.println(builder.toString());
    }

}
