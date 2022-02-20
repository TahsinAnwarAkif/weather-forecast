package com.weather.controller.exception;

import com.weather.exception.InvalidIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author akif
 * @since 1/13/22
 */
@ControllerAdvice
public class GlobalExceptionResolver {

    @Autowired
    private MessageSourceAccessor msa;

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<Object> handleNoDataFoundException(InvalidIdException e) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("status", "error");
        json.put("error", msa.getMessage(msa.getMessage(e.getMessage())));

        return new ResponseEntity<>(json, NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<Object> handleMissingServletReqParamException(MissingServletRequestParameterException e) {

        Map<String, Object> json = new LinkedHashMap<>();
        json.put("status", "error");
        json.put("error", e.getMessage());

        return new ResponseEntity<>(json, BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleException(HttpMessageNotReadableException e) {
        Map<String, Object> json = new LinkedHashMap<>();
        json.put("status", "error");
        json.put("error", e.getMessage());

        return new ResponseEntity<>(json, BAD_REQUEST);
    }
}
