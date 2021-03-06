package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRespository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRespository rsEventRespository;

    @BeforeEach
    void setUp() {
        UserController.users.clear();
    }

    @Test
    void nameShouldNotNull() throws Exception {
        User user = new User(null, 20, "female", "bitsqiu@gmail.com", "13886585124");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void genderShouldNotNull() throws Exception {
        User user = new User("Alibaba", 20, null, "bitsqiu@gmail.com", "13886585124");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldNotLessThan18() throws Exception {
        User user = new User("Alibaba", 17, "female", "bitsqiu@gmail.com", "13886585124");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ageShouldNotMoreThan100() throws Exception {
        User user = new User("Alibaba", 101, "female", "bitsqiu@gmail.com", "13886585124");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emailShouldValid() throws Exception {
        User user = new User("Alibaba", 20, "female", "gmail.com", "13886585124");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void phoneShouldValid() throws Exception {
        User user = new User("Alibaba", 20, "female", "bitsqiu@gmail.com", "13124");
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnExpectedUserList() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$[0].user_name", is("qindi")))
                .andExpect(jsonPath("$[0].user_age", is(22)))
                .andExpect(jsonPath("$[0].user_gender", is("male")))
                .andExpect(jsonPath("$[0].user_email", is("bitsqiu@gmail.com")))
                .andExpect(jsonPath("$[0].user_phone", is("13886585124")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnExceptionWhenRegisterWithInvalidUser() throws Exception {
        String requestJson = "{\"userName\":\"qindi\",\"age\":12,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid user")));
    }

    @Test
    void shouldAddUser() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserEntity> userEntities = userRepository.findAll();
        assertEquals(1,userEntities.size());
        assertEquals("qindi",userEntities.get(0).getUserName());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, userRepository.findAll().size());

        String requestJson = "{\"eventName\":\"事件1\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, rsEventRespository.findAll().size());
        String requestJson1 = "{\"eventName\":\"事件2\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(2, rsEventRespository.findAll().size());

        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isCreated());
        assertEquals(0, userRepository.findAll().size());
        assertEquals(0, rsEventRespository.findAll().size());
    }
}