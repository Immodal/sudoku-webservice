package kychin.sudokuwebservice.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {GeneratorController.class})
@WebMvcTest
public class GeneratorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void canGETSize() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.put("size", Collections.singletonList("2"));
        ApiTestUtils.sendGet(mockMvc, "/generate", params, true);

        params.put("size", Collections.singletonList("25"));
        ApiTestUtils.sendGet(mockMvc, "/generate", params, true);

        params.put("size", Collections.singletonList("36"));
        ApiTestUtils.sendGet(mockMvc, "/generate", params, true);

        for(int size : GeneratorController.SIZES) {
            params.put("size", Collections.singletonList(String.valueOf(size)));
            String result = ApiTestUtils.sendGet(mockMvc, "/generate", params).getResponse().getContentAsString();
            assertTrue(size*size<result.length());
        }
    }

    @Test
    void canGETState() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.put("state", Collections.singletonList("756389241493621875812547396268795134579134682134862759987416523341258967605973418"));
        ApiTestUtils.sendGet(mockMvc, "/generate", params, true);

        params.put("state", Collections.singletonList("756389241493621875812547396268795134579134682134862759987416523341258967625973418"));
        String result = ApiTestUtils.sendGet(mockMvc, "/generate", params).getResponse().getContentAsString();
        assertTrue(81<result.length());
    }
}
