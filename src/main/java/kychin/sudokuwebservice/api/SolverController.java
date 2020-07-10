package kychin.sudokuwebservice.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import kychin.sudokuwebservice.model.*;
import java.util.*;

@RestController
public class SolverController {

    /**
     * Solve a given puzzle state
     * @param state String representation of Sudoku puzzle
     * @return Session object
     */
    @GetMapping("/solve")
    public List<String> solveState(
            @RequestParam String state,
            @RequestParam(required=false, defaultValue="1") int n) {
        Solver solver = new DLX(State.toGrid(state));
        solver.search(n, 5000);
        return solver.getSolutions();
    }
}
