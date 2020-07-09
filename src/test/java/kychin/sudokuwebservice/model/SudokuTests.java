package kychin.sudokuwebservice.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SudokuTests {
    int[][][] grids = {
            State.toGrid("1400004121000012"), // Initial
            State.toGrid("1423324121344312"), // Complete
            State.toGrid("010048720" +
                    "070200506" +
                    "300700400" +
                    "005060078" +
                    "600503009" +
                    "230090100" +
                    "008006002" +
                    "403005010" +
                    "092380050"), //Incomplete
            State.toGrid("519648723" +
                    "874231596" +
                    "326759481" +
                    "945162378" +
                    "681573249" +
                    "237894165" +
                    "758416932" +
                    "463925817" +
                    "192387654"), // Complete
            State.toGrid("" +
                    "1423" +
                    "3241" +
                    "2135" +
                    "4312"), // Will pass all except symbols
            State.toGrid("" +
                    "1212" +
                    "3434" +
                    "1212" +
                    "3434"), // Will pass blocks and symbols check
            State.toGrid("" +
                    "1111" +
                    "2222" +
                    "3333" +
                    "4444"), // Will pass col and symbols check
            State.toGrid("" +
                    "1234" +
                    "1234" +
                    "1234" +
                    "1234"), // Will pass row and symbols check
    };

    @Test
    void checksValuesAreValid() {
        assertFalse(Sudoku.checkValuesAreValid(grids[0]));
        assertTrue(Sudoku.checkValuesAreValid(grids[1]));
        assertFalse(Sudoku.checkValuesAreValid(grids[2]));
        assertTrue(Sudoku.checkValuesAreValid(grids[3]));
        assertFalse(Sudoku.checkValuesAreValid(grids[4]));
        assertTrue(Sudoku.checkValuesAreValid(grids[5]));
        assertTrue(Sudoku.checkValuesAreValid(grids[6]));
        assertTrue(Sudoku.checkValuesAreValid(grids[7]));
    }

    @Test
    void checksRowValuesAreUnique() {
        assertFalse(Sudoku.checkRowValuesAreUnique(grids[0]));
        assertTrue(Sudoku.checkRowValuesAreUnique(grids[1]));
        assertFalse(Sudoku.checkRowValuesAreUnique(grids[2]));
        assertTrue(Sudoku.checkRowValuesAreUnique(grids[3]));
        assertTrue(Sudoku.checkRowValuesAreUnique(grids[4]));
        assertFalse(Sudoku.checkRowValuesAreUnique(grids[5]));
        assertFalse(Sudoku.checkRowValuesAreUnique(grids[6]));
        assertTrue(Sudoku.checkRowValuesAreUnique(grids[7]));
    }

    @Test
    void checksColValuesAreUnique() {
        assertFalse(Sudoku.checkColValuesAreUnique(grids[0]));
        assertTrue(Sudoku.checkColValuesAreUnique(grids[1]));
        assertFalse(Sudoku.checkColValuesAreUnique(grids[2]));
        assertTrue(Sudoku.checkColValuesAreUnique(grids[3]));
        assertTrue(Sudoku.checkColValuesAreUnique(grids[4]));
        assertFalse(Sudoku.checkColValuesAreUnique(grids[5]));
        assertTrue(Sudoku.checkColValuesAreUnique(grids[6]));
        assertFalse(Sudoku.checkColValuesAreUnique(grids[7]));
    }

    @Test
    void checksBlockValuesAreUnique() {
        assertFalse(Sudoku.checkBlockValuesAreUnique(grids[0]));
        assertTrue(Sudoku.checkBlockValuesAreUnique(grids[1]));
        assertFalse(Sudoku.checkBlockValuesAreUnique(grids[2]));
        assertTrue(Sudoku.checkBlockValuesAreUnique(grids[3]));
        assertTrue(Sudoku.checkBlockValuesAreUnique(grids[4]));
        assertTrue(Sudoku.checkBlockValuesAreUnique(grids[5]));
        assertFalse(Sudoku.checkBlockValuesAreUnique(grids[6]));
        assertFalse(Sudoku.checkBlockValuesAreUnique(grids[7]));
    }
}
