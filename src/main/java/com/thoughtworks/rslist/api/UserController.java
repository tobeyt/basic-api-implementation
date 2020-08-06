package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.component.GlobalExceptionHander;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.CommentError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    public static List<User> users = new ArrayList<>();
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user")
    public ResponseEntity register(@RequestBody @Valid User user) {
        users.add(user);
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        logger.info("MethodArgumentNotValidException Invalid User~~~");
        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentError);
    }
}
