package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RsController {
    private List<RsEvent> rsList = Stream.of(
            new RsEvent("the first event", "first"),
            new RsEvent("the second event", "second"),
            new RsEvent("the third event", "third")).collect(Collectors.toList());

    @GetMapping("/rs/list")
    public List<RsEvent> getRsList() {
        return rsList;
    }

    @GetMapping("/rs/{index}")
    public RsEvent getOneRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }
}