package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.repository.RsEventRespository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRespository rsEventRespository;

    public static List<User> users = new ArrayList<>();

    @PostMapping("/user")
    public ResponseEntity register(@RequestBody @Valid User user) {
        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .vote(user.getVote())
                .build();
        userRepository.save(userEntity);
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity deleteById(@PathVariable int userId) {
        userRepository.deleteById(userId);
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        CommentError commentError = new CommentError();
        commentError.setError("invalid user");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentError);
    }
}
