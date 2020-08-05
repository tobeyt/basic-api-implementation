package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.InValidIndexException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RsController {
    public static List<RsEvent> rsList = Stream.of(
            new RsEvent("the first event", "first"),
            new RsEvent("the second event", "second"),
            new RsEvent("the third event", "third")).collect(Collectors.toList());

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEvent>> getRsList() {
        return ResponseEntity.ok(rsList);
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEvent> getOneRsEvent(@PathVariable int index) throws InValidIndexException {
        if (index > rsList.size()) {
            throw new InValidIndexException("invalid index");
        }
        return ResponseEntity.ok(rsList.get(index - 1));
    }

    @GetMapping("/rs")
    public ResponseEntity<List<RsEvent>> getRsEventBetween(@RequestParam(required = false) Integer start,
                                                           @RequestParam(required = false) Integer end) {
        if (start == null && end == null) {
            return ResponseEntity.ok(rsList);
        }
        if (end == null) {
            return ResponseEntity.ok(rsList.subList(start - 1, rsList.size()));
        }
        if (start == null) {
            return ResponseEntity.ok(rsList.subList(0, end));
        }
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        boolean existUser = false;
        for (int i = 0; i < UserController.users.size(); i++) {
            if (rsEvent.getUser().getUserName().equals(UserController.users.get(i).getUserName())) {
                existUser = true;
                break;
            }
        }
        if (!existUser) {
            UserController.users.add(rsEvent.getUser());
        }
        rsList.add(rsEvent);
        HttpHeaders headers = new HttpHeaders();
        headers.add("index", String.valueOf(rsList.size() - 1));
        return new ResponseEntity(null, headers, HttpStatus.CREATED);
    }

    @PutMapping("/rs/event")
    public ResponseEntity updateOneRsEvent(@RequestParam(required = true) Integer number,
                                           @RequestBody RsEvent rsEvent) {
        if (rsEvent.getEventName() == null) {
            rsList.set(number - 1, new RsEvent(rsList.get(number - 1).getEventName(), rsEvent.getKeyWord()));
        } else if (rsEvent.getKeyWord() == null) {
            rsList.set(number - 1, new RsEvent(rsEvent.getEventName(), rsList.get(number - 1).getKeyWord()));
        } else {
            rsList.set(number - 1, rsEvent);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("index", String.valueOf(number - 1));
        return new ResponseEntity(null, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/rs/{index}")
    public ResponseEntity deleteOneRsEvent(@PathVariable int index) {
        rsList.remove(index - 1);

        HttpHeaders headers = new HttpHeaders();
        headers.add("index", String.valueOf(index - 1));

        return new ResponseEntity(null, headers, HttpStatus.CREATED);
    }

    @PostMapping("/rs")
    public ResponseEntity insertOneEvent(@RequestParam Integer index,
                                         @RequestBody RsEvent rsEvent) {
        rsList.add(index - 1, rsEvent);

        HttpHeaders headers = new HttpHeaders();
        headers.add("index", String.valueOf(index - 1));

        return new ResponseEntity(null, headers, HttpStatus.CREATED);
    }

    @ExceptionHandler(InValidIndexException.class)
    public ResponseEntity exceptionHandler(InValidIndexException ex) {
        CommentError commentError = new CommentError();
        commentError.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentError);
    }
}