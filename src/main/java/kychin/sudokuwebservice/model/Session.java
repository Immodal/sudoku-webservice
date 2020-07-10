package kychin.sudokuwebservice.model;

import java.util.*;

public class Session {

    private final UUID ID;
    private final String INITIAL_STATE;

    private int[][] grid;
    private Solver solver;

    public Session(UUID id, String state) {
        this.ID = id;
        this.INITIAL_STATE = state;

        this.grid = State.toGrid(state);
        this.solver = new DLX(this.grid);
    }
}
