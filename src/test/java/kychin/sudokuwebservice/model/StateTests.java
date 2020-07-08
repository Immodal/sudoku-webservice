package kychin.sudokuwebservice.model;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import java.util.*;

public class StateTests {
    @Test
    void parsesStates() {
        String state = "" +
                "123456789" +
                "456123456" +
                "789789123" +
                "123456789" +
                "456123456" +
                "789789128" +
                "123456789" +
                "456123456" +
                "789789123";

        int[][] grid = State.parse(state);
        for (int j=0; j<grid.length; j++) {
            for (int i=0; i<grid.length; i++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    @Test
    void checksSizeIsValid() {
        for (int i=1; i<Collections.max(State.SIZES)+10; i++){
            if (State.SIZES.contains(i)) {
                assertTrue(State.sizeIsValid("0".repeat(i*i)));
            } else {
                assertFalse(State.sizeIsValid("0".repeat(i*i)));
            }
        }
    }

    @Test
    void checksSymbolsAreValid() {
        String state = "123443!112344321";
        assertFalse(State.symbolsAreValid(state));
        state = "1234432112344321";
        assertTrue(State.symbolsAreValid(state));
        state = "1234432112354321";
        assertFalse(State.symbolsAreValid(state));
        state = "1234431112344321";
        assertTrue(State.symbolsAreValid(state));
        state = "1234432A12344321";
        assertFalse(State.symbolsAreValid(state));
        state = "1234431110004321";
        assertTrue(State.symbolsAreValid(state));
    }

    @Test
    void checksRowAreValid() {
        String state = "1234432112344321";
        assertTrue(State.rowsAreUnique(state));
        state = "1234432112344311";
        assertFalse(State.rowsAreUnique(state));
        state = "1234432002344311";
        assertFalse(State.rowsAreUnique(state));
        state = "1234430002344321";
        assertTrue(State.rowsAreUnique(state));
    }

    @Test
    void checksColsAreValid() {
        String state = "1144223333224411";
        assertTrue(State.colsAreUnique(state));
        state = "1144" +
                "2233" +
                "3321" +
                "4411";
        assertFalse(State.colsAreUnique(state));
        state = "1144" +
                "2233" +
                "3322" +
                "4441";
        assertFalse(State.colsAreUnique(state));
        state = "4123" +
                "3412" +
                "2341" +
                "1234";
        assertTrue(State.colsAreUnique(state));
        state = "0000000000000000";
        assertTrue(State.colsAreUnique(state));
        state = "1144000033224411";
        assertTrue(State.colsAreUnique(state));
    }

    @Test
    void checksBlocksAreValid() {
        String state = "" +
                "1212" +
                "3434" +
                "1212" +
                "3434";
        assertTrue(State.blocksAreUnique(state));
        state = "1212" +
                "3432" +
                "1212" +
                "3434";
        assertFalse(State.blocksAreUnique(state));
        state = "123456789" +
                "456123456" +
                "789789123" +
                "123456789" +
                "456123456" +
                "789789123" +
                "123456789" +
                "456123456" +
                "789789123";
        assertTrue(State.blocksAreUnique(state));
        state = "123456789" +
                "456123456" +
                "789789123" +
                "123456789" +
                "456123456" +
                "789789128" +
                "123456789" +
                "456123456" +
                "789789123";
        assertFalse(State.blocksAreUnique(state));
    }
}
