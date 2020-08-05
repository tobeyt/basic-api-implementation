package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.InValidIndexException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHander {

    @ExceptionHandler({InValidIndexException.class, MethodArgumentNotValidException.class})
    public ResponseEntity exceptionHandler(Exception ex) {
        String errorMessage;
        CommentError commentError = new CommentError();

        if (ex instanceof MethodArgumentNotValidException) {
            errorMessage = "invalid param";
        } else {
            errorMessage = ex.getMessage();
        }
        commentError.setError(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commentError);
    }
}
