package com.weather.exception;

/**
 * @author akif
 * @since 1/15/22
 */
public class InvalidIdException extends RuntimeException {

    private String msgKey;

    public InvalidIdException(String msgKey) {
        super(msgKey);
    }
}
