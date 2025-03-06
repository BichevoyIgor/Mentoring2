package ru.bichevoy.entity.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParkingUnitException extends RuntimeException {

    public ParkingUnitException(String message) {
        super(message);
        log.error(message);
    }
}
