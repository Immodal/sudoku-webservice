package kychin.sudokuwebservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import kychin.sudokuwebservice.model.*;

import java.util.*;

// TODO Add Tests for Generator API
@RestController
@RequestMapping("/generate")
public class GeneratorController {
    private static final Set<Integer> SIZES = initSizes();
    private static Set<Integer> initSizes() {
        Set<Integer> s = new HashSet<>(State.SIZES);
        s.remove(25);
        return Collections.unmodifiableSet(s);
    }

    /**
     * Generate a puzzle with a given size
     * @param size Size of puzzle to generate
     * @param maxSolutions Maximum number of solutions allowed for the puzzle
     * @param maxEmptyCells Maximum number of empty cells allowed for the puzzle
     * @return String representation of the puzzle
     */
    @GetMapping(value="", params = {"size"})
    public ResponseEntity<Map<String, Object>> generatePuzzle(
            @RequestParam int size,
            @RequestParam(required=false, defaultValue="1") int maxSolutions,
            @RequestParam(required=false, defaultValue="0") int maxEmptyCells) {
        if (SIZES.contains(size) && maxSolutions>0 && maxEmptyCells>=0) {
            if (maxEmptyCells==0) maxEmptyCells = size * size;
            Map<String, Object> body = new HashMap<>();
            body.put("puzzle", Generator.makePuzzle(size, maxSolutions, maxEmptyCells));
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * Generate a puzzle with a given size
     * @param state Size of puzzle to generate
     * @param maxSolutions Maximum number of solutions allowed for the puzzle
     * @param maxEmptyCells Maximum number of empty cells allowed for the puzzle
     * @return String representation of the puzzle
     */
    @GetMapping(value="", params = {"state"})
    public ResponseEntity<Map<String, Object>> generatePuzzleFromSolution(
            @RequestParam String state,
            @RequestParam(required=false, defaultValue="1") int maxSolutions,
            @RequestParam(required=false, defaultValue="0") int maxEmptyCells) {
        if (State.isValidAndComplete(state) &&
                SIZES.contains((int)Math.sqrt(state.length())) &&
                maxSolutions>0 && maxEmptyCells>=0) {
            if (maxEmptyCells==0) maxEmptyCells = state.length();
            Map<String, Object> body = new HashMap<>();
            body.put("puzzle", Generator.makePuzzle(state, maxSolutions, maxEmptyCells));
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
