package kychin.sudokuwebservice.model;

import java.util.*;

public class DLX extends Solver {
    private int level = 0;
    private final int COL_IND = 0;
    private final int NODE_IND = 1;

    private final List<DancingLinks.Node[]> stack; // "Recursion" stack
    private final List<DancingLinks.Node> solution;
    private final Map<Integer, Action> lookup;
    private final DancingLinks dlm;

    /**
     * Sudoku Solver that uses the DLX Algorithm by Donald Knuth
     * @param grid Int matrix representation of puzzle state
     */
    public DLX(int[][] grid) {
        // Initialize stack with first level
        stack = new ArrayList<>();
        stack.add(pair(null, null));
        // Initilize linkedlist used for interim solutions
        solution = new LinkedList<>();

        // Variables needed for constructing Dancing Links Matrix
        lookup = ExactCover.LOOKUPS.get(grid.length);
        boolean[][] ecm = ExactCover.MATRICES.get(grid.length);
        dlm = new DancingLinks(ecm);
        DancingLinks.Column root = dlm.get();

        // Make a new Dancing Links Matrix and then cover columns that have already been satisfied by grid.
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
    }

    /**
     * Get String representation of the Dancing Links Matrix
     * @return String representation of the Dancing Links Matrix
     */
    public String getDLMString() {
        return this.dlm.toString();
    }

    /**
     * Checks if the algorithm has searched all possible paths
     * @return true if algorithm has no remaining iterations
     */
    @Override
    public boolean searchIsComplete() {
        return level<0;
    }

    /**
     * Runs a single iteration of the DLX algorithm. This is a non-recursive version of DLX.
     * Basically Donald Knuth's implementation, but instead of actual recursion,
     * a "stack" keeps track of the state at each level of "recursion".
     */
    @Override
    protected void step() {
        // Each level stack is equivalent to the recursion depth
        DancingLinks.Column root = dlm.get();
        DancingLinks.Column c = (DancingLinks.Column) stack.get(level)[COL_IND];
        DancingLinks.Node r = stack.get(level)[NODE_IND];

        // If this stack level was just initialized
        if (c==null) {
            if (root.right==root) {
                // Check if its complete
                // Store the solution
                List<Action> actions = new ArrayList<>();
                for (DancingLinks.Node node : solution) {
                    actions.add(lookup.get(node.getId()));
                }
                solutions.add(actions);
                // Backtrack
                level--;
                return; // Exit Early
            } else {
                // Otherwise Choose a column to satisfy
                // S heuristic
                for (DancingLinks.Column j=root.getRight(); j!=root; j=j.getRight()) {
                    if (c==null || j.getSize() < c.getSize()) c = j;
                }

                if (c!=null) {
                    // Cover the chosen column and iterate through the rows
                    c.cover();
                    stack.set(level, pair(c ,c));
                    r = c;
                }
            }
        }

        // If r is not a header, then it was processed in last call. We must undo r before we continue
        if (r != c) {
            solution.remove(solution.size()-1);
            for(DancingLinks.Node j = r.left; j!=r; j=j.left) {
                j.getColumn().uncover();
            }
        }

        // If there is a non-header below r
        if (r.down != c) {
            // add r.down to the solution
            r = r.down;
            solution.add(r);
            for(DancingLinks.Node j = r.right; j!=r; j=j.right) {
                j.getColumn().cover();
            }
            // Record the state of the current level
            stack.set(level, pair(c ,r));
            // and go deeper into the stack
            level++;
            // Make sure the next level in the stack is clear
            // Grow stack if needed
            if(stack.size()==level) {
                stack.add(pair(null ,null));
            } else {
                stack.set(level, pair(null ,null));
            }
        } else {
            // If we've reached the end if the column or there are no rows
            // uncover the column and move up a stack level
            c.uncover();
            level--;
        }
    }

    /**
     * Helper function to pair up two Nodes to add to the "stack"
     * @param a Column Node
     * @param b Node
     * @return Array [a, b]
     */
    private DancingLinks.Node[] pair(DancingLinks.Column a, DancingLinks.Node b) {
        DancingLinks.Node[] p = new DancingLinks.Node[2];
        p[COL_IND] = a;
        p[NODE_IND] = b;
        return p;
    }
}
