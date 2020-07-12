package kychin.sudokuwebservice.model;

import kychin.sudokuwebservice.model.solvers.dlx.DLX;
import kychin.sudokuwebservice.Utils;
import java.util.*;


public class Generator {

    /**
     * Generates a random ungraded puzzle that adheres to the given parameters from a solution.
     * @param state Solved String representation of puzzle state
     * @param maxSolutions Maximum number of solutions that is acceptable in the puzzle.
     * @param maxEmptyCells Maximum number of empty cells that is acceptable in the puzzle.
     * @return String representation of puzzle state
     */
    public static String makePuzzle(String state, int maxSolutions, int maxEmptyCells) {
        String best = null;
        int nEmptyCells = 0;
        StringBuilder stateSB = new StringBuilder(state);
        List<Integer> is = Utils.range(0, stateSB.length());
        Collections.shuffle(is);

        for (int i : is) {
            String temp = stateSB.substring(i, i+1);
            stateSB.replace(i, i+1, "0");

            Solver solver = new DLX(stateSB.toString());
            solver.search(maxSolutions+1, state.length()*10);
            if (solver.getSolutions().size() == 0 || solver.getSolutions().size() > maxSolutions) {
                stateSB.replace(i, i+1, temp);
            } else {
                best = stateSB.toString();
                nEmptyCells++;
            }

            if (nEmptyCells>=maxEmptyCells) return best;
        }

        return best==null ? state : best;
    }

    /**
     * Generates a random ungraded puzzle that adheres to the given parameters from a solution.
     * @param size Size of solved String representation of puzzle state to generate and use.
     * @param maxSolutions Maximum number of solutions that is acceptable in the puzzle.
     * @param maxRemovals Maximum number of empty cells that is acceptable in the puzzle.
     * @return String representation of puzzle state
     */
    public static String makePuzzle(int size, int maxSolutions, int maxRemovals) {
        return makePuzzle(makeSolution(size), maxSolutions, maxRemovals);
    }

    /**
     * Generates a random solution which can be used to generate a puzzle.
     * @param size Size of the puzzle
     * @return Solved String representation of puzzle state
     */
    public static String makeSolution(int size) {
        // Long chains of no solutions are very rare, so this should be safe
        while (true) {
            String state = makeSeedState(size);
            Solver solver = new DLX(state);
            solver.search(10, size*size*10);

            List<String> solutions = solver.getSolutions();
            if (solutions.size()>0) {
                return solutions.get(Utils.randInt(0, solutions.size()-1));
            }
        }
    }

    /**
     * Generates a random state to function as a seed for generating a random solution.
     * Samples valid values for a puzzle of the given size without replacement and places them randomly in the state.
     * @param size Size of the puzzle
     * @return String representation of puzzle state with "size" number of cells filled in
     */
    private static String makeSeedState(int size) {
        List<Integer> indices = Utils.range(0, size*size);
        Collections.shuffle(indices);
        List<Integer> values = Utils.range(1, size+1);
        Collections.shuffle(values);

        // Initialize empty state
        StringBuilder state = new StringBuilder();
        state.append("0".repeat(indices.size()));

        // Fill with values
        for(int i=0; i<size; i++) {
            state.replace(indices.get(i), indices.get(i)+1, String.valueOf(State.SYMBOLS.charAt(i)));
        }

        return state.toString();
    }
}
