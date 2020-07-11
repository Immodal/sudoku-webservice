package kychin.sudokuwebservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<String>> solveState(
            @RequestParam String state,
            @RequestParam(required=false, defaultValue="1") int n) {
        if (State.isValid(state)) {
            Solver solver = new DLX(state);
            solver.search(n, 5000);
            return new ResponseEntity<>(solver.getSolutions(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    
}
