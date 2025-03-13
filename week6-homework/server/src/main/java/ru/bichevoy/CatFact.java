package ru.bichevoy;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatFact implements Serializable {
    private String fact;
    private int length;
}


