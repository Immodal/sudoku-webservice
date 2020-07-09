package kychin.sudokuwebservice.model;

import java.util.HashSet;
import java.util.Set;

public class DLX {
    private final DancingLinks dlm;

    public DLX(int[][] grid) {
        this.dlm = initDancingLinks(grid);
    }

    public String getDLMString() {
        return this.dlm.toString();
    }

    /**
     * Make a new Dancing Links Matrix and then cover columns that have already been satisfied by grid.
     * @param grid Int Matrix representation of the state
     * @return Dancing Links Matrix representation of the Exact Cover Matrix for grid
     */
    protected static DancingLinks initDancingLinks(int[][] grid) {
        boolean[][] ecm = ExactCover.MATRICES.get(grid.length);
        DancingLinks dlm = new DancingLinks(ecm);
        DancingLinks.Column root = dlm.get();

        for (int i=0; i<grid.length; i++) {
            for (int j=0; j<grid[0].length; j++) {
                int v = grid[i][j];
                // If it already has a value, cover the columns it satisfies
                if (v>0) {
                    int rowInd = ExactCover.getMatrixRowIndex(i, j, v, grid);
                    // Get the columns that row satisfies
                    boolean[] row = ecm[rowInd];
                    Set<Integer> columns = new HashSet<>();
                    for (int k=0; k<row.length; k++) {
                        if (row[k]) columns.add(k);
                    }

                    // Find the column object in dlx and cover
                    for (DancingLinks.Column c=root.getRight(); c!=root; c=c.getRight()) {
                        if (columns.contains(c.getId())) c.cover();
                    }
                }
            }
        }

        return dlm;
    }
}
