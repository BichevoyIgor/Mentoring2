package ru.bichevoy.entity.car;

import lombok.Data;

@Data
public abstract class Car {
    private String regNomer;

    public Car(String regNomer) {
        this.regNomer = regNomer;
    }

    @Override
    public String toString() {
        return String.format("%s{regNomer=%s}", this.getClass().getSimpleName(), regNomer);
    }
}
