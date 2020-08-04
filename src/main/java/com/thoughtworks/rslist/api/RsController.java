package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/rs")
    public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start,
                                           @RequestParam(required = false) Integer end) {
        if (start == null && end == null) {
            return rsList;
        }
        if (end == null) {
            return rsList.subList(start - 1, rsList.size());
        }
        if (start == null) {
            return rsList.subList(0, end);
        }
        return rsList.subList(start - 1, end);
    }

    @PostMapping("/rs/event")
    public void addOneRsEvent(@RequestBody RsEvent rsEvent) {
        rsList.add(rsEvent);
    }

    @PutMapping("/rs/event")
    public void updateOneRsEvent(@RequestParam(required = true) Integer number,
                                 @RequestBody RsEvent rsEvent) {
        if (rsEvent.getEventName() == null) {
            rsList.set(number - 1, new RsEvent(rsList.get(number - 1).getEventName(), rsEvent.getKeyWord()));
        } else if (rsEvent.getKeyWord() == null) {
            rsList.set(number - 1, new RsEvent(rsEvent.getEventName(), rsList.get(number - 1).getKeyWord()));
        } else {
            rsList.set(number - 1, rsEvent);
        }
    }

    @DeleteMapping("/rs/{index}")
    public void deleteOneRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);
    }

    @PostMapping("/rs")
    public void insertOneEvent(@RequestParam Integer index,
                               @RequestBody RsEvent rsEvent) {
        rsList.add(index - 1, rsEvent);
    }
}