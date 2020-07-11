package kychin.sudokuwebservice.model.solvers.dlx;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DancingLinksTests {
    @Test
    void nodesInitializeWithCircularReferences() {
        DancingLinks.Node node = new DancingLinks.Node(0);
        assertEquals(node.left, node);
        assertEquals(node.right, node);
        assertEquals(node.up, node);
        assertEquals(node.down, node);
    }

    @Test
    void nodesInsertRight() {
        DancingLinks.Node node1 = new DancingLinks.Node(0);
        DancingLinks.Node node2 = new DancingLinks.Node(1);
        DancingLinks.Node node3 = new DancingLinks.Node(2);

        node1.insertRight(node2);
        assertEquals(node1.left, node2);
        assertEquals(node1.right, node2);
        assertEquals(node2.left, node1);
        assertEquals(node2.right, node1);
        node2.insertRight(node3);
        assertEquals(node2.left, node1);
        assertEquals(node2.right, node3);
        assertEquals(node3.left, node2);
        assertEquals(node3.right, node1);
        assertEquals(node1.left, node3);
        assertEquals(node1.right, node2);
    }

    @Test
    void nodesInsertDown() {
        DancingLinks.Node node1 = new DancingLinks.Node(0);
        DancingLinks.Node node2 = new DancingLinks.Node(1);
        DancingLinks.Node node3 = new DancingLinks.Node(2);

        node1.insertDown(node2);
        assertEquals(node1.up, node2);
        assertEquals(node1.down, node2);
        assertEquals(node2.up, node1);
        assertEquals(node2.down, node1);
        node2.insertDown(node3);
        assertEquals(node2.up, node1);
        assertEquals(node2.down, node3);
        assertEquals(node3.up, node2);
        assertEquals(node3.down, node1);
        assertEquals(node1.up, node3);
        assertEquals(node1.down, node2);
    }

    @Test
    void importsECMatrix() {
        boolean[][] ecm = {
                {true, false, false, true, true},
                {true, false, true, false, true},
                {false, true, false, false, false},
                {false, true, false, false, true},
        };

        DancingLinks.Column root = DancingLinks.fromExactCover(ecm);
        assertEquals(root, root.right.right.right.right.right.right);
        assertEquals(root, root.left.left.left.left.left.left);
        checkColumn(root.right, 0, 2);
        checkColumn(root.right.right, 1, 2);
        checkColumn(root.right.right.right, 2, 1);
        checkColumn(root.right.right.right.right, 3, 1);
        checkColumn(root.right.right.right.right.right, 4, 3);

        assertEquals(root.right.down.right.down, root.left.left);
        assertEquals(root.right.right.down, root.right.right.down.left);
        assertEquals(root.left.up.left, root.right.right.down.down);
        assertNotSame(root.right.down, root.right.down.left);
        assertNotSame(root.right.down, root.right.down.right);
    }

    /*
    Checks if the column was created correctly
     */
    private void checkColumn(DancingLinks.Node col, int id, int size) {
        assertEquals(col.getId(), id);
        assertEquals(((DancingLinks.Column) col).getSize(), size);
        DancingLinks.Node current = col;
        for (int i=0; i<size+1; i++) {
            current = current.down;
            if(i<size) assertNotSame(col, current);
            else assertEquals(col, current);
        }
    }

    @Test
    void convertsToString() {
        boolean[][] ecm = {
                {true, false, false, true, true},
                {true, false, true, false, true},
                {false, true, false, false, false},
                {false, true, false, false, true},
        };
        String exp = "Column 0: 0,1,\nColumn 1: 2,3,\nColumn 2: 1,\nColumn 3: 0,\nColumn 4: 0,1,3,\n";

        DancingLinks.Column root = DancingLinks.fromExactCover(ecm);
        assertEquals(exp, DancingLinks.toString(root));
    }

    @Test
    void columnsCoverAndUncover() {
        boolean[][] ecm = {
                {true, false, false, true, true},
                {true, false, true, false, true},
                {false, true, false, false, false},
                {false, true, false, false, true},
        };

        DancingLinks.Column root = DancingLinks.fromExactCover(ecm);
        DancingLinks.Column c2 = root.getRight().getRight().getRight();
        c2.cover();
        assertEquals(root.right, root.right.right.right.right.right.right); // Column gone
        assertEquals(root.right, root.right.down.down); // row gone
        assertEquals(((DancingLinks.Column)root.right).getSize(), 1);
        c2.uncover();
        assertEquals(root.right, root.right.right.right.right.right.right.right); // Column back
        assertEquals(root.right.down.down.left, root.right.down.down.right.right); // row back
        assertEquals(((DancingLinks.Column)root.right).getSize(), 2);

        DancingLinks.Column c1 = root.getRight().getRight();
        c1.cover();
        assertEquals(root.right, root.right.right.right.right.right.right); // Column gone
        assertEquals(root.left.up, root.right.down.right.right.down); // rows gone
        assertEquals(root.getLeft().getSize(), 2);
        c1.uncover();
        assertEquals(root.right, root.right.right.right.right.right.right.right); // Column back
        assertEquals(root.left.up, root.right.down.right.right.down.down); // rows back
        assertEquals(root.getLeft().getSize(), 3);
    }
}
