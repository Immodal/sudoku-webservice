package kychin.sudokuwebservice.model;

import java.util.*;

public class DancingLinks {
    private final Column root;

    public DancingLinks(boolean[][] ecm) {
        this.root = fromExactCover(ecm);
    }

    public Column get() {
        return root;
    }

    public String toString() {
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

        private int id;
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
