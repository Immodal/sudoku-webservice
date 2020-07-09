package kychin.sudokuwebservice.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

public class DLXTests {
    @Test
    void initializesAsExpected() {
        String state = "1400004121000012";
        String dlmExp = "Column 2: 9,10,\n" +
                "Column 3: 14,\n" +
                "Column 4: 18,\n" +
                "Column 5: 21,22,\n" +
                "Column 10: 42,\n" +
                "Column 11: 46,47,\n" +
                "Column 12: 50,51,\n" +
                "Column 13: 54,\n" +
                "Column 17: 9,\n" +
                "Column 18: 10,14,\n" +
                "Column 21: 21,\n" +
                "Column 22: 18,22,\n" +
                "Column 26: 42,46,\n" +
                "Column 27: 47,\n" +
                "Column 30: 50,54,\n" +
                "Column 31: 51,\n" +
                "Column 34: 18,50,\n" +
                "Column 35: 51,\n" +
                "Column 37: 21,\n" +
                "Column 38: 22,54,\n" +
                "Column 41: 9,\n" +
                "Column 42: 10,42,\n" +
                "Column 46: 14,46,\n" +
                "Column 47: 47,\n" +
                "Column 49: 21,\n" +
                "Column 50: 18,22,\n" +
                "Column 53: 9,\n" +
                "Column 54: 10,14,\n" +
                "Column 58: 50,54,\n" +
                "Column 59: 51,\n" +
                "Column 62: 42,46,\n" +
                "Column 63: 47,\n";

        assertTrue(State.isValid(state));
        DLX dlx = new DLX(State.toGrid(state));
        assertEquals(dlx.getDLMString(), dlmExp);
    }

    @Test
    void findsSolutions() {
        String state = "295743861431865900876192543387459216612387495549216738763524189928671354154938600";
        assertTrue(State.isValid(state));
        DLX dlx = new DLX(State.toGrid(state));
        dlx.search(1000, 10000);
        checkSolutions(dlx.getSolutions(), 2, 4);

        state = "0000000000000000";
        assertTrue(State.isValid(state));
        dlx = new DLX(State.toGrid(state));
        dlx.search(1000, 10000);
        checkSolutions(dlx.getSolutions(), 288, 16);
    }

    // Helper for findsSolutions()
    private void checkSolutions(List<List<DancingLinks.Node>> solutions, int nSols, int solSize) {
        assertEquals(nSols, solutions.size());
        for (int i=0; i<solutions.size(); i++) {
            assertEquals(solSize, solutions.get(0).size());
        }
    }
}
