package com.user.exception;

import com.user.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserAlreadyExist.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExist(UserAlreadyExist ex){
        ExceptionResponse result=new ExceptionResponse(
          ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                "Already user exist!",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(result,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UsernameNotFoundException ex){
        ExceptionResponse result=new ExceptionResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "User not found!",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(result,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex){
        ExceptionResponse result=new ExceptionResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Please enter correct OTP!",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
    }
}
