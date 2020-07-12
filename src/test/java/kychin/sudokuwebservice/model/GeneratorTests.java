package kychin.sudokuwebservice.model;

import static org.junit.jupiter.api.Assertions.*;

import kychin.sudokuwebservice.model.solvers.dlx.DLX;
import org.junit.jupiter.api.Test;
import org.apache.commons.lang3.StringUtils;
import kychin.sudokuwebservice.Utils;

public class GeneratorTests {
    @Test
    void makesSolutions() {
        for(int i=0; i<25; i++) {
            for(int size: State.SIZES) {
                String state = Generator.makeSolution(size);
                assertTrue(State.isValid(state));
            }
        }
    }

    @Test
    void makesPuzzles() {
        repeatPuzzleSizeAndRandomizeParams(50, 4);
        repeatPuzzleSizeAndRandomizeParams(50, 9);
        repeatPuzzleSizeAndRandomizeParams(10, 16);
        makeAndTestPuzzle(25, 1, 25*25);
    }

    private void repeatPuzzleSizeAndRandomizeParams(int n, int size) {
        for(int i=0; i<n; i++) {
            makeAndTestPuzzle(size, Utils.randInt(1, size), Utils.randInt(size, size*size));
        }
    }

    private void makeAndTestPuzzle(int size, int maxSolutions, int maxRemovals) {
        String state = Generator.makePuzzle(size, maxSolutions, maxRemovals);
        assertTrue(State.isValid(state));
        Solver solver = new DLX(state);
        solver.search(maxSolutions+1, state.length()*10);
        assertTrue(solver.getSolutions().size()<=maxSolutions);
        assertTrue(StringUtils.countMatches(state,"0")<=maxRemovals);
    }
}
