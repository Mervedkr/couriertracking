package com.tracking.courier.Exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String error) {
        super(String.format(error));
    }
}
