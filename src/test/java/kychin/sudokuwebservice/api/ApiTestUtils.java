package kychin.sudokuwebservice.api;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiTestUtils {
    public static MvcResult sendGet(MockMvc mockMvc, String url, MultiValueMap<String, String> params) throws Exception {
        return sendGet(mockMvc, url, params, false);
    }

    public static MvcResult sendGet(MockMvc mockMvc, String url, MultiValueMap<String, String> params, Boolean isBad) throws Exception {
        return mockMvc.perform(
                MockMvcRequestBuilders
                        .get(url)
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(isBad? status().isBadRequest() : status().isOk())
                .andReturn();
    }
}
