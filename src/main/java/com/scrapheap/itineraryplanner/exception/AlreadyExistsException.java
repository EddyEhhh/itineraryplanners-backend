package com.scrapheap.itineraryplanner.exception;


public class AlreadyExistsException extends RuntimeException {

    String field;

    public AlreadyExistsException(String message) {
        super(message);
    }
    public AlreadyExistsException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return this.field;
    }
}
