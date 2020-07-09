package kychin.sudokuwebservice.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class StateTests {

    @Test
    void convertsStateToGridAndBack() {
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

        String[] stateArr = state.split("");
        int size = State.getSize(state);
        int[][] grid = State.toGrid(state);

        for (int j=0; j<grid.length; j++) {
            for (int i=0; i<grid.length; i++) {
                assertEquals(State.SYMBOL_MAP.get(stateArr[i + j * size]), grid[j][i]);
            }
        }

        assertEquals(state, State.fromGrid(grid));
    }

    @Test
    void checksStateIsValid() {
        String state = "";
        assertFalse(State.isValid(state));
        state = "1234";
        assertFalse(State.isValid(state));
        state = "3124421313422431";
        assertTrue(State.isValid(state));
        state = "3124421313322431"; // Repeat at index 9,10
        assertFalse(State.isValid(state));
        state = "001000000230009400009007000000000700050890063096372514300604957574000006968025001";
        assertTrue(State.isValid(state));
        state = "001000000230009400009007000000000700050890063096372514300604957574000006968025201";
        assertFalse(State.isValid(state)); // Repeat at index -3, -5
        state = "" +
                "f01a004d3080000e" +
                "0509001ac0207046" +
                "006bc9ge04010830" +
                "04308b20075g9f00" +
                "a8000g50001fe000" +
                "0020040000b0a7gc" +
                "cb70e0816000f900" +
                "0040a0b020e0100d" +
                "00000dc0f00b0001" +
                "9cb2g003100a40e7" +
                "700100e2000d8000" +
                "00000760g2003c9f" +
                "g9000cd4b1060008" +
                "32056100e0a0g000" +
                "6d00be0050900070" +
                "000f030g78d060a2";
        assertTrue(State.isValid(state.toUpperCase()));
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
