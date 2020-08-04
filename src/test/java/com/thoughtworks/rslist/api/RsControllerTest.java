package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class RsControllerTest {
    @Autowired
    MockMvc mockMvc;

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
    void shouldGetRsEventListBetweenHaveStartNotHaveEnd() throws Exception {
        mockMvc.perform(get("/rs?start=2"))
                .andExpect(jsonPath("$[0].eventName", is("the second event")))
                .andExpect(jsonPath("$[0].keyWord", is("second")))
                .andExpect(jsonPath("$[1].eventName", is("the third event")))
                .andExpect(jsonPath("$[1].keyWord", is("third")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsEventListBetweenNotHaveStartHaveEnd() throws Exception {
        mockMvc.perform(get("/rs?end=2"))
                .andExpect(jsonPath("$[0].eventName", is("the first event")))
                .andExpect(jsonPath("$[0].keyWord", is("first")))
                .andExpect(jsonPath("$[1].eventName", is("the second event")))
                .andExpect(jsonPath("$[1].keyWord", is("second")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetRsEventListBetweenNotHaveStartNotHaveEnd() throws Exception {
        mockMvc.perform(get("/rs"))
                .andExpect(jsonPath("$[0].eventName", is("the first event")))
                .andExpect(jsonPath("$[0].keyWord", is("first")))
                .andExpect(jsonPath("$[1].eventName", is("the second event")))
                .andExpect(jsonPath("$[1].keyWord", is("second")))
                .andExpect(jsonPath("$[2].eventName", is("the third event")))
                .andExpect(jsonPath("$[2].keyWord", is("third")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetFourEventsWhenAddOneEvent() throws Exception {
        RsEvent newRsEvent = new RsEvent("the forth event", "forth");
        String requestJson = new ObjectMapper().writeValueAsString(newRsEvent);

        mockMvc.perform(post("/rs/event").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("the first event")))
                .andExpect(jsonPath("$[0].keyWord", is("first")))
                .andExpect(jsonPath("$[1].eventName", is("the second event")))
                .andExpect(jsonPath("$[1].keyWord", is("second")))
                .andExpect(jsonPath("$[2].eventName", is("the third event")))
                .andExpect(jsonPath("$[2].keyWord", is("third")))
                .andExpect(jsonPath("$[3].eventName", is("the forth event")))
                .andExpect(jsonPath("$[3].keyWord", is("forth")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateEventAndGetUpdatedEventHaveEventNameAndKeyword() throws Exception {

        RsEvent newRsEvent = new RsEvent("the second new event", "new second");
        String requestJson = new ObjectMapper().writeValueAsString(newRsEvent);

        mockMvc.perform(put("/rs/event?number=2").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("the second new event")))
                .andExpect(jsonPath("$.keyWord", is("new second")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateEventAndGetUpdatedEventHaveEventNameAndNotHaveKeyword() throws Exception {

        RsEvent newRsEvent = new RsEvent("the second new event", null);
        String requestJson = new ObjectMapper().writeValueAsString(newRsEvent);

        mockMvc.perform(put("/rs/event?number=2").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("the second new event")))
                .andExpect(jsonPath("$.keyWord", is("second")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateEventAndGetUpdatedEventNotHaveEventNameAndHaveKeyword() throws Exception {

        RsEvent newRsEvent = new RsEvent(null, "new second");
        String requestJson = new ObjectMapper().writeValueAsString(newRsEvent);

        mockMvc.perform(put("/rs/event?number=2").content(requestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName", is("the second event")))
                .andExpect(jsonPath("$.keyWord", is("new second")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteEventAndGetListNotHaveDeletedEvent() throws Exception{
        mockMvc.perform(delete("/rs/2"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$[0].eventName", is("the first event")))
                .andExpect(jsonPath("$[0].keyWord", is("first")))
                .andExpect(jsonPath("$[1].eventName", is("the third event")))
                .andExpect(jsonPath("$[1].keyWord", is("third")))
                .andExpect(status().isOk());
    }
}
