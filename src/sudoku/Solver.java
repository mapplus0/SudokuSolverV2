package sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solver {
    
    private boolean changed;
    private List<Gameplan> stack;

    public Solver () {
        this.changed = false;
        this.stack = new ArrayList<>();
    }

    public void solve() {
        this.changed = false;
        if (Gameplan.getCurrentGameplan().isSolved()) return;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (Gameplan.getCurrentGameplan().getTile(row,col).isFree()) {
                    Set<Integer> taken = new HashSet<>();
                    for (Tile t : Gameplan.getCurrentGameplan().getRow(row).getTileList()) {
                        if (!t.isFree())
                            taken.add(t.getValue());
                    }
                    for (Tile t : Gameplan.getCurrentGameplan().getColumn(col).getTileList()) {
                        if (!t.isFree())
                            taken.add(t.getValue());
                    }
                    for (Tile t : Gameplan.getCurrentGameplan().getSquareFromTile(row, col).getTileList()) {
                        if (!t.isFree())
                            taken.add(t.getValue());
                    }

                    if (taken.size() == 8) {
                        for (int i = 1; i <= 9; i++) {
                            if (!taken.contains(i)) {
                                //System.out.println("row="+row+" | col="+col+" | val="+i);
                                Gameplan.getCurrentGameplan().getTile(row,col).setValue(i);
                                this.changed = true;
                                break;
                            }
                        }
                    } else if (taken.size() == 9 && !this.stack.isEmpty()) {
                        Gameplan.setCurrentGameplan(this.stack.get(stack.size()-1));
                        this.stack.remove(this.stack.size()-1);
                    } else if(!this.changed && taken.size()==7) {
                        Gameplan gcopy = Gameplan.getCurrentGameplan().deepCopy();
                        int a = -1;
                        int b = -1;
                        for (int i = 1; i <= 9; i++) {
                            if (!taken.contains(i)) {
                                if (a == -1) {
                                    a = i;
                                } else {
                                    b = i;
                                }

                            }
                        }
                        //System.out.println(" >>>>>>>>>>>> a="+a+"b="+b);
                        gcopy.getTile(row,col).setValue(a);
                        this.stack.add(gcopy);
                        Gameplan.getCurrentGameplan().getTile(row,col).setValue(b);
                    }

                }

            }
        }

    }

}