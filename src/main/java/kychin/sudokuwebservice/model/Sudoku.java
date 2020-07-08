package kychin.sudokuwebservice.model;

import java.util.*;

public class Sudoku {

    private final UUID id;
    private final int[][] grid;

    public Sudoku(UUID id, String state) {
        this.id = id;
        this.grid = State.parse(state);
    }


}
