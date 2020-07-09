package kychin.sudokuwebservice.model;

import org.apache.commons.lang3.StringUtils;
import java.util.*;

public class State {
    // Sizes are equal to the number of cells in each row/column.
    public static final Set<Integer> SIZES = ExactCover.SIZES;
    public static final String SYMBOLS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final Map<String, Integer> SYMBOL_MAP = initSymbolMap();

    /**
     * Converts the given state into an int matrix representation.
     * @param state String representation of game state
     * @return Int matrix representation of state
     */
    public static int[][] toGrid(String state) {
        String[] stateArr = state.split("");
        int size = getSize(state);
        int[][] grid = new int[size][size];

        for (int j=0; j<grid.length; j++) {
            for (int i=0; i<grid.length; i++) {
                grid[i][j] = SYMBOL_MAP.get(stateArr[i+j*size]);
            }
        }

        return grid;
    }

    /**
     * Converts the given int matrix into its String representation.
     * @param grid Int matrix representation of state
     * @return String representation of game state
     */
    public static String fromGrid(int[][] grid) {
        String[] symbols = SYMBOLS.split("");
        StringBuilder state = new StringBuilder();
        for (int j=0; j<grid.length; j++) {
            for (int i=0; i<grid.length; i++) {
                state.append(symbols[grid[i][j]]);
            }
        }
        return state.toString();
    }

    /**
     * Checks the given state to determine if it is valid for conversion to a grid.
     * @param state String representation of game state
     * @return true if state is valid
     */
    public static boolean isValid(String state) {
        return sizeIsValid(state) && symbolsAreValid(state) &&
                rowsAreUnique(state) && colsAreUnique(state) &&
                blocksAreUnique(state);
    }

    /**
     * @param state String representation of game state
     * @return size Size of state
     */
    protected static int getSize(String state) {
        return (int) Math.sqrt(state.length());
    }

    /**
     * @param state String representation of game state
     * @return size Size of state
     */
    protected static int getBlockSize(String state) {
        return (int) Math.sqrt(getSize(state));
    }

    /**
     * @param state String representation of game state
     * @return true if state will result in a grid of size found in SIZES
     */
    protected static boolean sizeIsValid(String state) {
        double size = Math.sqrt(state.length());
        return size==((int)size) && SIZES.contains((int) size);
    }

    /**
     * Checks if the symbols in the given state are valid for its size.
     * Assumes that state passes checkSize().
     * @param state String representation of game state
     * @return true if state only contains symbols found in SYMBOLS
     */
    protected static boolean symbolsAreValid(String state) {
        String validSymbols = StringUtils.substring(SYMBOLS, 0, getSize(state)+1);
        return StringUtils.containsOnly(state, validSymbols);
    }

    /**
     * Checks if the symbols in the given row (except 0) are unique.
     * Assumes that state passes checkSize()
     * @param state String representation of game state
     * @return true if every row in the state passes symbolsAreUnique()
     */
    protected static boolean rowsAreUnique(String state) {
        int size = getSize(state);
        for (int i=0; i<size; i++) {
            String row = StringUtils.substring(state, i*size, i*size+size);
            if (!symbolsAreUnique(row)) return false;
        }
        return true;
    }

    /**
     * Checks if the symbols in the given column (except 0) are unique.
     * Assumes that state passes checkSize()
     * @param state String representation of game state
     * @return true if every row in the state passes symbolsAreUnique()
     */
    protected static boolean colsAreUnique(String state) {
        int size = getSize(state);
        for (int i=0; i<size; i++) {
            StringBuilder col = new StringBuilder();
            for (int j=0; j<size; j++) {
                col.append(state.charAt(j*size+i));
            }
            if (!symbolsAreUnique(col.toString())) return false;
        }
        return true;
    }

    /**
     * Checks if the symbols in the given column (except 0) are unique.
     * Assumes that state passes checkSize()
     * @param state String representation of game state
     * @return true if every row in the state passes symbolsAreUnique()
     */
    protected static boolean blocksAreUnique(String state) {
        int size = getSize(state);
        int blockSize = getBlockSize(state);
        for (int j=0; j<blockSize; j++) {
            for (int i=0; i<size; i+=blockSize) {
                String block = getBlockValues(state, size, blockSize, j*blockSize*size+i);
                if (!symbolsAreUnique(block)) return false;
            }
        }

        return true;
    }

    /**
     *
     * @param state String representation of game state
     * @param size Size of state
     * @param blockSize BlockSize of state
     * @param n Position of top left of block in terms of its index in the state string
     * @return String containing values that reside within the block
     */
    private static String getBlockValues(String state, int size, int blockSize, int n) {
        StringBuilder block = new StringBuilder();
        for (int j=0; j<blockSize; j++) {
            for (int i=0; i<blockSize; i++) {
                block.append(state.charAt(n+j*size+i));
            }
        }
        return block.toString();
    }

    /**
     * Checks if the symbols in the given row (except 0) are unique
     * @param s String representation of game state
     * @return true if the symbols in the row are unique
     */
    private static boolean symbolsAreUnique(String s) {
        Set<String> symbols = new HashSet<>(Arrays.asList(s.split("")));
        int nZeroes = StringUtils.countMatches(s, "0");
        int zeroesAdjustment = Math.max(nZeroes - 1, 0);
        return s.length() - zeroesAdjustment == symbols.size();
    }

    /**
     * Generates a Map that maps SYMBOLS indices to their values.
     * @return Map that maps SYMBOLS indices to their values
     */
    private static Map<String, Integer> initSymbolMap () {
        Map<String, Integer> symbolMap = new HashMap<>();

        final String[] symbols = SYMBOLS.split("");
        for (int i = 0; i < symbols.length; i++) {
            symbolMap.put(symbols[i], i);
        }
        return symbolMap;
    }
}
