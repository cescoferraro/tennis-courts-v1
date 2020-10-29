package com.tenniscourts.reservations;

import com.tenniscourts.tenniscourts.TennisCourtController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TenisCourtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TennisCourtController controller;

    @Test
    public void testEndpoint() throws Exception {
        mockMvc.perform(get("/test")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Greetings from Spring Boot!")));

    }
}