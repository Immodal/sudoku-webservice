package kychin.sudokuwebservice.model;

import java.util.*;

public class Sudoku {

    private final UUID ID;
    private final String INITIAL_STATE;

    private int[][] grid;

    public Sudoku(UUID id, String state) {
        this.ID = id;
        this.INITIAL_STATE = state;

        this.grid = State.toGrid(state);
    }

    /**
     * Check if the current state of grid is a valid solution
     * @param grid Int Matrix representation of the state
     * @return true if grid contains a valid solution
     */
    public static boolean validate(int[][] grid) {
        return checkValuesAreValid(grid) && checkRowValuesAreUnique(grid) &&
                checkColValuesAreUnique(grid) && checkBlockValuesAreUnique(grid);
    }

    /**
     * Check if all values in grid correspond to a valid symbol in State.SYMBOLS
     * @param grid Int Matrix representation of the state
     * @return true if all values are valid
     */
    protected static boolean checkValuesAreValid(int[][] grid) {
        for(int j=0; j<grid.length; j++) {
            for(int i=0; i<grid.length; i++) {
                if (grid[j][i]<=0 || grid[j][i]>grid.length) return false;
            }
        }
        return true;
    }

    /**
     * Check if all values in each row are unique within that row
     * @param grid Int Matrix representation of the state
     * @return true if all values in each row are unique within that row for the whole grid
     */
    protected static boolean checkRowValuesAreUnique(int[][] grid) {
        for(int j=0; j<grid.length; j++) {
            Set<Integer> values = new HashSet<>();
            for(int i=0; i<grid.length; i++) {
                int v = grid[j][i];
                if (values.contains(v)) return false;
                else values.add(v);
            }
        }
        return true;
    }

    /**
     * Check if all values in each col are unique within that col
     * @param grid Int Matrix representation of the state
     * @return true if all values in each col are unique within that col for the whole grid
     */
    protected static boolean checkColValuesAreUnique(int[][] grid) {
        for(int i=0; i<grid.length; i++) {
            Set<Integer> values = new HashSet<>();
            for(int j=0; j<grid.length; j++) {
                int v = grid[j][i];
                if (values.contains(v)) return false;
                else values.add(v);
            }
        }
        return true;
    }

    /**
     * Check if all values in each block are unique within that block
     * @param grid Int Matrix representation of the state
     * @return true if all values in each block are unique within that block for the whole grid
     */
    protected static boolean checkBlockValuesAreUnique(int[][] grid) {
        int blockSize = (int) Math.sqrt(grid.length);
        for(int j=0; j<grid.length; j+=blockSize) {
            for(int i=0; i<grid.length; i+=blockSize) {
                if(!checkBlockValuesAreUnique(grid, blockSize, i, j)) return false;
            }
        }
        return true;
    }

    /**
     * Helper to check that all values are unique within a block where its top left cell is grid[iOffset][jOffset]
     * @param grid Int Matrix representation of the state
     * @param blockSize Size of each block
     * @param iOffset Column index on grid for the top left cell of the block
     * @param jOffset Row index on grid for the top left cell of the block
     * @return true if all values in each block are unique within that block
     */
    private static boolean checkBlockValuesAreUnique(int[][] grid, int blockSize, int iOffset, int jOffset) {
        Set<Integer> values = new HashSet<>();
        for(int j=0; j<blockSize; j++) {
            for(int i=0; i<blockSize; i++) {
                int v = grid[j+jOffset][i+iOffset];
                if (values.contains(v)) return false;
                else values.add(v);
            }
        }
        return true;
    }


}
