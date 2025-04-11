package ru.bichevoy;

import lombok.Data;

@Data
public abstract class Animal {
    private String name;

    public abstract void makeSound();
}
