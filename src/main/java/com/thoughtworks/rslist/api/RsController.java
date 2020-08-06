package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.InValidIndexException;
import com.thoughtworks.rslist.exception.InvalidStartAndEnd;
import com.thoughtworks.rslist.repository.RsEventRespository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RsController {
    @Autowired
    RsEventRespository rsEventRespository;
    @Autowired
    UserRepository userRepository;

    public static List<RsEvent> rsList;

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
                                                           @RequestParam(required = false) Integer end) throws InvalidStartAndEnd {
        if (start == null || end == null) {
            return ResponseEntity.ok(rsList);
        }
        if (start > end || start < 1 || start > rsList.size() || end < 1 || end > rsList.size()) {
            throw new InvalidStartAndEnd("invalid request param");
        }
        return ResponseEntity.ok(rsList.subList(start - 1, end));
    }

    @PostMapping("/rs/event")
    public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {
        Optional<UserEntity> id = userRepository.findById(rsEvent.getUserId());
        if (!id.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        RsEventEntity rsEventEntity = RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyWord())
                .userId(rsEvent.getUserId())
                .build();
        rsEventRespository.save(rsEventEntity);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/rs/event")
    public ResponseEntity updateOneRsEvent(@RequestParam(required = true) Integer number,
                                           @RequestBody RsEvent rsEvent) {

        return new ResponseEntity(null, HttpStatus.CREATED);
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
}