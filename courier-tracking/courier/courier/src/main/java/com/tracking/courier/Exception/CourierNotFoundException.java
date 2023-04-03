package com.tracking.courier.Exception;
public class CourierNotFoundException extends RuntimeException {

    public CourierNotFoundException(Long courierId) {
        super("Courier with ID " + courierId + " not found");
    }

    public CourierNotFoundException(String error) {
        super(error);
    }
}
