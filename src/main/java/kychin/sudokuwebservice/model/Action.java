package kychin.sudokuwebservice.model;

/**
 * Data object that represents the action of placing a value v into position grid[i,j].
 */
public class Action {
    public final int i;
    public final int j;
    public final int v;

    public Action(int i, int j, int v) {
        this.i = i;
        this.j = j;
        this.v = v;
    }
}
