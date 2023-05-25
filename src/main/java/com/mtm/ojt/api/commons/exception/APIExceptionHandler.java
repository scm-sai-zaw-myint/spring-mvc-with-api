package com.mtm.ojt.api.commons.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.mtm.ojt.api.commons.reponse.ErrorResponse;

@ControllerAdvice
@RequestMapping("/api")
public class APIExceptionHandler {

    // Handle 404 - Not Found
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleNoHandlerFoundException(NoHandlerFoundException e) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Page not found!", new Date().getTime(),e.getMessage());
    }

    // Handle other exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleException(Exception e) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", new Date().getTime(),e.getMessage());
    }
    
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<ErrorResponse> handleTokenException(TokenException e) {
        return ResponseEntity.status(450).body(new ErrorResponse(450, "Token exception", new Date().getTime(),e.getMessage()));
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleMethodNotAllowedOnAPI(HttpRequestMethodNotSupportedException e) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), "You don't have enought permission to perform this action.", new Date().getTime(), null);
    }
    
}

