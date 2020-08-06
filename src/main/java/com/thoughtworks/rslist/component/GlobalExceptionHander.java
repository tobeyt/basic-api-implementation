package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.InValidIndexException;
import com.thoughtworks.rslist.exception.InvalidStartAndEnd;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHander {
    @ExceptionHandler(InValidIndexException.class)
    public ResponseEntity InValidIndexExceptionHandler(InValidIndexException ex) {
        CommentError commentError = new CommentError();
        commentError.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        CommentError commentError = new CommentError();
        commentError.setError("invalid param");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentError);
    }

    @ExceptionHandler({InvalidStartAndEnd.class})
    public ResponseEntity InvalidStartAndEndHandler(InvalidStartAndEnd ex) {
        CommentError commentError = new CommentError();
        commentError.setError(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentError);
    }
}
