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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {
    @Autowired
    RsEventRespository rsEventRespository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/rs/list")
    public ResponseEntity<List<RsEventEntity>> getRsList() {
        List<RsEventEntity> allEvents = rsEventRespository.findAll();
        return ResponseEntity.ok(allEvents);
    }

    @GetMapping("/rs/{index}")
    public ResponseEntity<RsEventEntity> getRsEventByIndex(@PathVariable int index) throws InValidIndexException {
        if (index > rsEventRespository.findAll().size()) {
            throw new InValidIndexException("invalid index");
        }
        RsEventEntity result = rsEventRespository.findById(index).get();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/rs")
    public ResponseEntity<List<RsEventEntity>> getRsEventBetween(@RequestParam(required = false) Integer start,
                                                                 @RequestParam(required = false) Integer end) throws InvalidStartAndEnd {
        List<RsEventEntity> allEvents = rsEventRespository.findAll();
        if (start == null || end == null) {
            return ResponseEntity.ok(allEvents);
        }
        if (start > end || start < 1 || start > allEvents.size() || end < 1 || end > allEvents.size()) {
            throw new InvalidStartAndEnd("invalid request param");
        }
        return ResponseEntity.ok(allEvents.subList(start, end + 1));
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

    @PutMapping("/rs/{rsEventId}")
    public ResponseEntity updateOneRsEvent(@PathVariable int rsEventId,
                                           @RequestBody RsEvent rsEvent) {
        @NotNull int userId = rsEvent.getUserId();
        RsEventEntity rsEventEntity = rsEventRespository.findById(rsEventId).get();
        int id = rsEventEntity.getUserId();
        if (id!=userId) {
            return ResponseEntity.badRequest().build();
        }
        if (rsEvent.getEventName() != null) {
            rsEventEntity.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
            rsEventEntity.setKeyword(rsEvent.getKeyWord());
        }
        rsEventRespository.save(rsEventEntity);
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @DeleteMapping("/rs/{rsEventId}")
    public ResponseEntity deleteOneRsEvent(@PathVariable int rsEventId) {
        rsEventRespository.deleteById(rsEventId);
        return new ResponseEntity(null, HttpStatus.CREATED);
    }
}