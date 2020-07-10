package kychin.sudokuwebservice.model;

import java.util.*;

public class ExactCover {
    public static final Set<Integer> SIZES = Set.of(4, 9, 16, 25);
    public static final Map<Integer, boolean[][]> MATRICES = makeMatrices();
    public static final Map<Integer, Map<Integer, Action>> LOOKUPS = makeLookups();

    /**
     * Calculates the row index on the ECM given the parameters
     * @param row Row index in Grid
     * @param col Column index in Grid
     * @param value Value at Grid[Row][Col]
     * @param grid Int matrix representing the Sudoku grid
     * @return Row index on the ECM given the parameters
     */
    public static int getMatrixRowIndex(int row, int col, int value, int[][] grid) {
        int nValues = grid.length;
        // The *grid row* value of each *matrix Row* only increases by 1 after every col and value for that row has
        // been listed
        int rowOffset = row * grid[0].length * nValues;
        // The *grid col* value of each *matrix Row* only increases by 1 after every value for that row has been listed
        int colOffset = col * nValues;
        // Each value changes for every (grid row, grid col) combination in their natural sequence 1 to nValues
        // Because 0 is not included in the EC Matrix, the index of each value is value-1
        int valueIndex = value - 1;
        return rowOffset + colOffset + valueIndex;
    }

    /**
     * Pre-make row index lookups for all Exact Cover Matrices supported sizes and map to their size.
     * @return Map that maps each lookup to their size
     */
    private static Map<Integer, Map<Integer, Action>> makeLookups() {
        Map<Integer, Map<Integer, Action>> lookups = new HashMap<>();
        for (int size: SIZES) {
            lookups.put(size, makeLookup(new int[size][size]));
        }
        return Collections.unmodifiableMap(lookups);
    }

    /**
     * @param grid Int matrix representing the Sudoku grid
     * @return Map that maps each row index on an Exact Cover matrix to an Action
     */
    private static Map<Integer, Action> makeLookup(int[][] grid) {
        Map<Integer, Action> lookup = new HashMap<>();
        for (int i=0; i<grid.length; i++) {
            for (int j=0; j<grid[0].length; j++) {
                for (int v=1; v<grid.length+1; v++) {
                    int row = ExactCover.getMatrixRowIndex(i, j, v, grid);
                    lookup.put(row, new Action(i, j, v));
                }
            }
        }
        return Collections.unmodifiableMap(lookup);
    }

    /**
     * Pre-make Exact Cover Matrices for all supported sizes and map to their size.
     * @return Map that maps each matrix to their size
     */
    private static Map<Integer, boolean[][]> makeMatrices() {
        Map<Integer, boolean[][]> matrices = new HashMap<>();
        for (int size: SIZES) {
            matrices.put(size, makeMatrix(new int[size][size]));
        }
        return Collections.unmodifiableMap(matrices);
    }

    /**
     * Generates an Exact Cover matrix for a Sudoku grid the size of "grid"
     * @param grid Int matrix representing the Sudoku grid
     * @return Exact Cover Matrix
     */
    private static boolean[][] makeMatrix(int[][] grid) {
        int nGridRows = grid.length;
        int nGridCols = grid[0].length;
        int nValues = grid.length;

        // One *matrix row* for every value in every cell
        int nRows = nGridRows * nGridCols * nValues;
        // One value per cell constraint (Row-Column)
        // One matrix column for every cell in the grid
        // This constraint marks the location of each cell
        int nCellConCols = nGridRows * nValues;
        // Row constraint (Row-Value)
        // One matrix column for every row-value pair in the grid
        // This constraint marks whether a value exists in a given row
        int nRowConCols = nGridRows * nValues;
        // Col constraint (Col-Value)
        // One matrix column for every col-value pair in the grid
        // This constraint marks whether a value exists in a given col
        int nColConCols = nGridCols * nValues;
        // Block constraint (Block-Value)
        // One matrix column for every block-value pair in the grid
        // This constraint marks whether a value exists in a given blocks
        // The number of blocks in a grid is equal to its length
        // So this is actually: numBlocksInGrid * symbols.size()
        int nBlockConCols =  nGridRows * nValues;
        // Starting Columns for each constraint section
        int cellConStartCol = 0;
        int rowConStartCol = nCellConCols;
        int colConStartCol = nCellConCols + nRowConCols;
        int blockConStartCol = nCellConCols + nRowConCols + nColConCols;

        boolean[][] matrix = new boolean[nRows][nCellConCols + nRowConCols + nColConCols + nBlockConCols];
        setCellConstraints(matrix, grid, cellConStartCol);
        setRowConstraints(matrix, grid, rowConStartCol);
        setColConstraints(matrix, grid, colConStartCol);
        setBlockConstraints(matrix, grid, blockConStartCol);

        return matrix;
    }

    /**
     * Mutates the matrix with the columns that satisfies the Cell Constraints for each row.
     * @param matrix Exact Cover Matrix that is under construction
     * @param grid Int matrix representing the Sudoku grid
     * @param colInd Column index on the ECM from which this method will start
     */
    private static void setCellConstraints(boolean[][] matrix, int[][] grid, int colInd) {
        // For each cell in the grid
        for (int i=0; i<grid.length; i++) {
            for (int j=0; j<grid[0].length; j++) {
                // And every possible value for each cell
                for (int v=1; v<grid.length+1; v++) {
                    // Mark its location in the matrix
                    int rowInd = getMatrixRowIndex(i, j, v, grid);
                    matrix[rowInd][colInd] = true;
                }
                // Since only every new *grid col* generates a new *matrix col* increment colInd only after symbols
                // are complete
                colInd++;
            }
        }
    }

    /**
     * Mutates the matrix with the columns that satisfies the Row Constraints for each row.
     * @param matrix Exact Cover Matrix that is under construction
     * @param grid Int matrix representing the Sudoku grid
     * @param colInd Column index on the ECM from which this method will start
     */
    private static void setRowConstraints(boolean[][] matrix, int[][] grid, int colInd) {
        // For each row in the grid
        for (int i = 0; i < grid.length; i++) {
            // And every value that can exist in the row
            for (int v=1; v<grid.length+1; v++) {
                // And every column where a given row-value pair can occur
                for (int j = 0; j < grid[0].length; j++) {
                    // Mark its location in the matrix
                    int rowInd = getMatrixRowIndex(i, j, v, grid);
                    matrix[rowInd][colInd] = true;
                }
                // Since only every new row-value pair generates a new *matrix col* increment colInd only after cols
                // are complete
                colInd++;
            }
        }
    }

    /**
     * Mutates the matrix with the columns that satisfies the Column Constraints for each row.
     * @param matrix Exact Cover Matrix that is under construction
     * @param grid Int matrix representing the Sudoku grid
     * @param colInd Column index on the ECM from which this method will start
     */
    private static void setColConstraints(boolean[][] matrix, int[][] grid, int colInd) {
        // For each col in the grid
        for (int j=0; j<grid[0].length; j++) {
            // And every value that can exist in the col
            for (int v=1; v<grid.length+1; v++) {
                // And every row where a given col-value pair can occur
                for (int i=0; i<grid.length; i++) {
                    // Mark its location in the matrix
                    int rowInd = getMatrixRowIndex(i, j, v, grid);
                    matrix[rowInd][colInd] = true;
                }
                // Since only every new col-value pair generates a new *matrix col* increment colInd only after rows
                // are complete
                colInd++;
            }
        }
    }

    /**
     * Mutates the matrix with the columns that satisfies the Block Constraints for each row.
     * @param matrix Exact Cover Matrix that is under construction
     * @param grid Int matrix representing the Sudoku grid
     * @param colInd Column index on the ECM from which this method will start
     */
    private static void setBlockConstraints(boolean[][] matrix, int[][] grid, int colInd) {
        int numBlocks = (int) Math.sqrt(grid.length); // Per row or per col

        // For every block in the grid
        for (int iOffset = 0; iOffset < grid.length; iOffset += numBlocks) {
            for (int jOffset = 0; jOffset < grid[0].length; jOffset += numBlocks) {
                // And every value that can exist in the block
                for (int v=1; v<grid.length+1; v++) {
                    // Apply to every cell in the block
                    for (int i = 0; i < numBlocks; i++) {
                        for (int j = 0; j < numBlocks; j++) {
                            // Mark its location in the matrix
                            int rowInd = getMatrixRowIndex(iOffset+i, jOffset+j, v, grid);
                            matrix[rowInd][colInd] = true;
                        }
                    }
                    // Since only every new block-value pair generates a new *matrix col* increment colInd only after
                    // blocks cells are complete
                    colInd++;
                }
            }
        }
    }

    /**
     * Generates a string representation of the Exact Cover Matrix.
     * @param matrix Exact Cover Matrix
     * @return String representation of ECM
     */
    public static String toString(boolean[][] matrix) {
        StringBuilder strGrid = new StringBuilder();
        for (int i=0; i<matrix.length ; i++) {
            for (int j=0; j<matrix[0].length ; j++) {
                // Values
                if (matrix[i][j]) {
                    strGrid.append("1");
                } else {
                    strGrid.append(" ");
                }
            }
            strGrid.append("\n");
        }
        return strGrid.toString();
    }
}
