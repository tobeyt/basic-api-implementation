package com.thoughtworks.rslist;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldAddUser() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
