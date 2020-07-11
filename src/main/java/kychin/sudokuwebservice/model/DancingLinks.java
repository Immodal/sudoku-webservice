package kychin.sudokuwebservice.model;

import java.util.*;

/**
 * Dancing Links data structure for representing sparse matrices
 * Here it is used to represent Exact Cover matrices
 */
public class DancingLinks {
    public final String INITIAL_STATE;
    private final Column root;
    private final Map<Integer, Action> lookup;

    /**
     * Constructor
     * @param state String representation of puzzle state
     */
    public DancingLinks(String state) {
        this.INITIAL_STATE = state;

        int[][] grid = State.toGrid(INITIAL_STATE);
        this.root = fromExactCover(ExactCover.MATRICES.get(grid.length));
        this.lookup = ExactCover.LOOKUPS.get(grid.length);
        initialize(this, grid);
    }

    /**
     * Getter for root
     * @return root
     */
    public Column get() {
        return root;
    }

    /**
     * Get INITIAL_STATE as an Int Matrix
     * @return Int Matrix representation of INITIAL_STATE
     */
    public int[][] getGrid() {
        return State.toGrid(INITIAL_STATE);
    }

    /**
     * Get the Action object associated with the given Node's ID
     * @param node
     * @return
     */
    public Action getAction(Node node) {
        return lookup.get(node.getId());
    }

    /**
     * Get the string representation of this DLM
     * @return String representation of DLM
     */
    public String toString() {
        return toString(root);
    }

    /**
     * Converts the DLM to its string representation
     * @param root Root Column of the DLM
     * @return String representation of DLM
     */
    protected static String toString(Column root) {
        StringBuilder s = new StringBuilder();
        for (Column c=root.getRight(); c!=root; c=c.getRight()) {
            s.append("Column ").append(c.getId()).append(": ");
            for (Node r=c.down; r!=c; r=r.down)
                s.append(r.getId()).append(",");
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Goes through DLM and covers columns that grid satisfies
     * @param dlm Dancing Links Matrix
     * @param grid Int Matrix representation of state
     */
    protected static void initialize(DancingLinks dlm, int[][] grid) {
        boolean[][] ecm = ExactCover.MATRICES.get(grid.length);
        Column root = dlm.get();

        // Cover columns that have already been satisfied by grid.
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
                    for (Column c=root.getRight(); c!=root; c=c.getRight()) {
                        if (columns.contains(c.getId())) c.cover();
                    }
                }
            }
        }
    }

    /**
     * Converts an Exact Cover Matrix from a boolean[][] to a Dancing Links representation.
     * @param ecm Exact Cover Matrix
     * @return Dancing Links Exact Cover Matrix
     */
    protected static Column fromExactCover(boolean[][] ecm) {
        List<Column> columns = new ArrayList<>();
        for (int i=0; i<ecm[0].length; i++) {
            columns.add(new Column(i));
            // Insert new columns to the right of their predecessors
            if (i>0) columns.get(i-1).insertRight(columns.get(i));
        }

        // For each row in the ecMatrix,
        for (int i=0; i<ecm.length; i++) {
            Node prevNode = null;
            // And each column in the ecMatrix,
            for (int j=0; j<ecm[0].length; j++) {
                // If row i satisfies column j, create a node here
                if (ecm[i][j]) {
                    Column c = columns.get(j);
                    Node node = new Node(i, c);
                    // Add node to column and insert it to the right of prevNode
                    c.add(node, prevNode);
                    prevNode = node;
                }
            }
        }

        // Insert root object
        Column root = new Column(-1);
        columns.get(columns.size() - 1).insertRight(root);
        return root;
    }

    /**
     * Base Class for Objects within the Dancing Links Matrix
     */
    protected static class Node {
        protected Node up = this;
        protected Node down = this;
        protected Node left = this;
        protected Node right = this;

        private final int id;
        private Column column;

        public Node(int id) {
            this.id = id;
        }

        public Node(int id, Column column) {
            this(id);
            this.column = column;
        }

        public int getId() {
            return id;
        }

        public Column getColumn() {
            return column;
        }

        /**
         * Inserts the Node n to the right of this Node.
         * @param n Node to be inserted
         */
        public void insertRight(Node n) {
            // Update pointers in n
            n.left = this;
            n.right = this.right;
            // Update pointers in new n.right, originally points to this, now n
            n.right.left = n;
            // Update pointers in this
            this.right = n;
        }

        /**
         * Inserts the Node n below this Node.
         * @param n Node to be inserted
         */
        public void insertDown(Node n) {
            // Update pointers in n
            n.up = this;
            n.down = this.down;
            // Update pointers in new n.down, originally points to this, now n
            n.down.up = n;
            // Update pointers in this
            this.down = n;
        }
    }

    /**
     * Column Node in the Dancing Links Matrix.
     */
    protected static class Column extends Node {
        private int size = 0;

        public Column(int id) {
            super(id);
        }

        public int getSize() {
            return size;
        }

        public Column getLeft() {
            return (Column) this.left;
        }

        public Column getRight() {
            return (Column) this.right;
        }

        /**
         * Adds Node n to this Column and inserts it to the right of Node left.
         * Only use in construction.
         * @param n Node to be added
         * @param left Node to insert n to the right of
         */
        protected void add(Node n, Node left) {
            // Because iteration is going top to bottom, insertion will always
            // be at the bottom of the column (which is always above the header)
            // Vertical Link
            this.up.insertDown(n);
            this.size++;
            // Because iteration is going left to right, insertion will always
            // to the right of the previous node
            // Horizontal Link
            if (left!=null) left.insertRight(n);
        }

        /**
         * Covers this column to remove it from the Dancing Links Matrix.
         */
        public void cover() {
            // Point column header's left right neighbors at each other
            this.right.left = this.left;
            this.left.right = this.right;
            // Going top to bottom, i being a row in the column
            Node i = this.down;
            while (i != this) {
                // Left to right, j a node in row i satisfies
                Node j = i.right;
                while (j != i) {
                    // Point j's up down neighbors at each other
                    j.down.up = j.up;
                    j.up.down = j.down;
                    // Because j has been covered, update the size of the column it belongs to
                    j.column.size--;
                    // Continue to the right
                    j = j.right;
                }
                // Continue to the bottom
                i = i.down;
            }
        }

        /**
         * Uncovers this column to reintroduce it to the Dancing Links Matrix. Works in reverse of cover().
         */
        public void uncover() {
            // Going bottom to top, i being a row in the column
            Node i = this.up;
            while (i != this) {
                // Right to left, j being columns that row i satisfies
                Node j = i.left;
                while (j != i) {
                    // Because j has been uncovered, update the size of the column it belongs to
                    j.column.size++;
                    // Point j's up down neighbors to j again
                    j.down.up = j;
                    j.up.down = j;
                    // Continue to the left
                    j = j.left;
                }
                // Continue to the top
                i = i.up;
            }
            // Point column header's left right neighbors at column again
            this.right.left = this;
            this.left.right = this;
        }
    }
}
