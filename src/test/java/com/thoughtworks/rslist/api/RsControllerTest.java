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
    void shouldGetAllRsList() throws Exception {
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("the first event")))
                .andExpect(jsonPath("$[0].keyWord", is("first")))
                .andExpect(jsonPath("$[1].eventName", is("the second event")))
                .andExpect(jsonPath("$[1].keyWord", is("second")))
                .andExpect(jsonPath("$[2].eventName", is("the third event")))
                .andExpect(jsonPath("$[2].keyWord", is("third")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetOneRsEvent() throws Exception {
        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("the second event")))
                .andExpect(jsonPath("$.keyWord", is("second")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsEventListBetween() throws Exception {
        mockMvc.perform(get("/rs?start=2&end=3"))
                .andExpect(jsonPath("$[0].eventName", is("the second event")))
                .andExpect(jsonPath("$[0].keyWord", is("second")))
                .andExpect(jsonPath("$[1].eventName", is("the third event")))
                .andExpect(jsonPath("$[1].keyWord", is("third")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateEventAndGetUpdatedEventHaveEventNameAndKeyword() throws Exception {


    }

    @Test
    void shouldUpdateEventAndGetUpdatedEventHaveEventNameAndNotHaveKeyword() throws Exception {


    }

    @Test
    void shouldUpdateEventAndGetUpdatedEventNotHaveEventNameAndHaveKeyword() throws Exception {


    }

    @Test
    void shouldDeleteEventAndGetListNotHaveDeletedEvent() throws Exception {

    }

    @Test
    void addRsEventWithUser() throws Exception {
        String requestJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"user\":{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}}";

        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(4, RsController.rsList.size());
        assertEquals(1, UserController.users.size());

        mockMvc.perform(delete("/rs/4"))
                .andExpect(status().isCreated());
    }

    @Test
    void addRsEventWithoutUserInList() throws Exception {
        String requestJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"user\":{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}}";

        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, UserController.users.size());
        assertEquals(4, RsController.rsList.size());

        mockMvc.perform(delete("/rs/4"))
                .andExpect(status().isCreated());
    }

    @Test
    void eventNameShouldNotNull() throws Exception {

    }

    @Test
    void keyWordShouldNotNull() throws Exception {

    }

    @Test
    void userShouldNotNull() throws Exception {

    }

    @Test
    void userNameShouldNotNull() throws Exception {

    }

    @Test
    void shouldAddORsEvent() throws Exception {
        String userJson = "{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}";
        mockMvc.perform(post("/user").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, userRepository.findAll().size());

        String requestJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"userId\":1}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertEquals(1, rsEventRespository.findAll().size());

        String requestJson2 = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"userId\":111}";
        mockMvc.perform(post("/rs/event").content(requestJson2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void getRsListReturnWithoutUserFields() throws Exception {
        String requestJson = "{\"eventName\":\"第四条事件\",\"keyWord\":\"无分类\",\"user\":{\"userName\":\"qindi\",\"age\":22,\"gender\":\"male\",\"email\":\"bitsqiu@gmail.com\",\"phone\":\"13886585124\"}}";
        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[3].eventName", is("第四条事件")))
                .andExpect(jsonPath("$[3].keyWord", is("无分类")))
                .andExpect(jsonPath("$[3]", not(hasKey("user"))))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/rs/4"))
                .andExpect(status().isCreated());
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
