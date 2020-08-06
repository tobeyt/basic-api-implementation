package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.repository.RsEventRespository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RsEventRespository rsEventRespository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserController.users.clear();
    }

    @Test
    void shouldGetAllRsEvents() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, userRepository.findAll().size());

        String requestJson = "{\"eventName\":\"事件\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, rsEventRespository.findAll().size());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("事件")))
                .andExpect(status().isOk());

    }

    @Test
    void shouldGetIndexRsEvent() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, userRepository.findAll().size());

        String requestJson = "{\"eventName\":\"事件\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, rsEventRespository.findAll().size());
        mockMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName", is("事件")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetBetween() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, userRepository.findAll().size());
        String requestJson = "{\"eventName\":\"事件\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs?start=1&end=3"))
                .andExpect(jsonPath("$[0].eventName", is("事件")))
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(status().isOk());

    }

    @Test
    void shouldAddORsEvent() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, userRepository.findAll().size());

        String requestJson = "{\"eventName\":\"事件\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, rsEventRespository.findAll().size());

        String requestJson2 = "{\"eventName\":\"事件\",\"keyWord\":\"无分类\",\"userId\":111}";
        mockMvc.perform(post("/rs/event").content(requestJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldGetOneRsEvent() throws Exception {
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("the second event")))
                .andExpect(jsonPath("$.keyWord", is("second")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, userRepository.findAll().size());
        String requestJson = "{\"eventName\":\"事件\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String requestJson1 = "{\"keyWord\":\"分类\",\"userId\":1}";
        mockMvc.perform(put("/rs/1").content(requestJson1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals("分类", rsEventRespository.findById(1).get().getKeyword());
        assertEquals("事件", rsEventRespository.findById(1).get().getEventName());

        String requestJson2 = "{\"eventName\":\"新事件2\",\"userId\":1}";
        mockMvc.perform(put("/rs/1").content(requestJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals("新事件2", rsEventRespository.findById(1).get().getEventName());
        assertEquals("分类", rsEventRespository.findById(1).get().getKeyword());
    }

    @Test
    void shouldDeleteRsEvent() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, userRepository.findAll().size());
        String requestJson = "{\"eventName\":\"事件\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/rs/1"))
                .andExpect(status().isCreated());
        assertEquals(1, rsEventRespository.findAll().size());
    }

    @Test
    void shouldReturnBadRequestWhenIndexOutOfBound() throws Exception {
        mockMvc.perform(get("/rs/10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    void shouldReturnExceptionWhenAddOneRsEventInvalidUser() throws Exception {
        String requestJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"user\":{\"userName\":\"qindi\",\"age\":12,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid param")));
    }

    @Test
    void shouldReturnExcepthonWhenNotBetween() throws Exception {
        mockMvc.perform(get("/rs?start=-1&end=100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("invalid request param")));

    }
}
