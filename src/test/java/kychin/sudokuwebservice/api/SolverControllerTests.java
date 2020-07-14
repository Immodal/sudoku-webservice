package kychin.sudokuwebservice.api;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {SolverController.class})
@WebMvcTest
public class SolverControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canGETSolution() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.put("state", Collections.singletonList("700300001400001800000000006008700000009100002034002000900400000000050067625077000"));
        ApiTestUtils.sendGet(mockMvc, "/solve", params, true);

        params.put("state", Collections.singletonList("700300001400001800000000006008700000009100002034002000900400000000050067625070000"));
        assertEquals("{\"solutions\":[\"756389241493621875812547396268795134579134682134862759987416523341258967625973418\"],\"isComplete\":false}",
                ApiTestUtils.sendGet(mockMvc, "/solve", params).getResponse().getContentAsString());

        params.put("n", Collections.singletonList("0"));
        ApiTestUtils.sendGet(mockMvc, "/solve", params, true);

        params.put("n", Collections.singletonList("2"));
        assertEquals("{\"solutions\":[\"756389241493621875812547396268795134579134682134862759987416523341258967625973418\"],\"isComplete\":true}",
                ApiTestUtils.sendGet(mockMvc, "/solve", params).getResponse().getContentAsString());
    }

    @Test
    void canGETInfo() throws Exception {
        assertNotEquals("{}",
                ApiTestUtils.sendGet(mockMvc, "/solve/info", new LinkedMultiValueMap<>()).getResponse().getContentAsString());
    }
}
