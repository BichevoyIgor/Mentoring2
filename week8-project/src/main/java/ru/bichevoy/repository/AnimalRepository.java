package ru.bichevoy.repository;

import ru.bichevoy.entity.Animal;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository {

    List<Animal> findAllAnimal();

    void addAnimal(Animal animal);

    void removeAnimal(Animal animal);

    Optional<Animal> findAnimalById(int id);

}
