package kychin.sudokuwebservice.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import kychin.sudokuwebservice.model.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/solve")
public class SolverController {

    // Preload information object
    private static final Map<String, Object> info = makeInfo();
    private static Map<String, Object> makeInfo() {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<>() {};
        try {
            File from = new ClassPathResource("static/solverInfo.json").getFile();
            HashMap<String, Object> info = mapper.readValue(from, typeRef);
            info.put("sizesSupported", ExactCover.SIZES);
            return info;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }


    /**
     * Solve a given puzzle state
     * @param state String representation of Sudoku puzzle
     * @return solutions found
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> solveState(
            @RequestParam String state,
            @RequestParam(required=false, defaultValue="1") int n) {
        state = state.toUpperCase();
        if (State.isValid(state) && n>0) {
            Solver solver = new DLX(state);
            solver.search(n, 5000);

            Map<String, Object> body = new HashMap<>();
            body.put("solutions", solver.getSolutions());
            body.put("isComplete", solver.isComplete());
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * Information about the solver API
     * @return Solver API information
     */
    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        return info;
    }
}
