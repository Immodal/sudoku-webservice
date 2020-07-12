package kychin.sudokuwebservice.model;

import java.util.*;

public abstract class Solver {

    /**
     * List of solutions as valid states as determined by State.isValid()
     */
    protected final List<String> solutions = new ArrayList<>();

    /**
     * Getter for solutions
     * @return solutions
     */
    public List<String> getSolutions() {
        return solutions;
    }

    /**
     * Checks if the algorithm has searched all possible paths
     * @return true if algorithm has no remaining iterations
     */
    public abstract boolean isComplete();

    /**
     * Searches for valid solutions (up to solutionLimit or stepLimit) to the puzzle.
     * @param solutionLimit Number of additional solutions found that will trigger the search to stop.
     * @param stepLimit Number of steps taken that will trigger the search to stop.
     */
    public void search(int solutionLimit, int stepLimit) {
        int nSteps = 0;
        int nSolutions = 0;
        int stepSolutionsSize;
        while(!isComplete() && nSolutions<solutionLimit && nSteps<stepLimit) {
            stepSolutionsSize = solutions.size();
            step();
            nSteps++;
            if (solutions.size()>stepSolutionsSize) nSolutions++;
        }
    }

    /**
     * Runs a single iteration of the solver algorithm.
     */
    protected abstract void step();

}
