package com.scrapheap.itineraryplanner.advice;

import com.scrapheap.itineraryplanner.exception.AlreadyExistsException;
import com.scrapheap.itineraryplanner.exception.InvalidAuthenticationException;
import com.scrapheap.itineraryplanner.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public Map<String, String> handleInvalidArguments(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(String.format("%s%s", error.getField(), "ErrorMessage"), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Map<String, String> handleUnauthorizedException(Exception ex) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("errorMessage", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidAuthenticationException.class)
    public Map<String, String> handleInvalidAuthentication(Exception ex) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put("errorMessage", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AlreadyExistsException.class)
    public Map<String, String> handleForbidden(AlreadyExistsException ex) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put(ex.getField() + "ErrorMessage", ex.getMessage());
        return errorMap;
    }



//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public Map<String, String> handleBusinessException(Exception ex) {
//        Map<String, String> errorMap = new HashMap<String, String>();
//        errorMap.put("errorMessage", ex.getMessage());
//        return errorMap;
//    }



}
