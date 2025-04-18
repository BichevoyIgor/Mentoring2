package ru.bichevoy.repository;

import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AnimalRepositoryImpl implements AnimalRepository {

    private final List<Animal> animalList;
    private final AtomicInteger idCounter;

    public AnimalRepositoryImpl() {
        animalList = new ArrayList<>();
        idCounter = new AtomicInteger();
    }

    @Override
    public List<Animal> findAllAnimal() {
        return List.copyOf(animalList);
    }

    @Override
    public void addAnimal(Animal animal) {
        animalList.add(animal);
        animal.setId(idCounter.incrementAndGet());
    }

    @Override
    public void removeAnimal(Animal animal) {
        animalList.remove(animal);
    }

    @Override
    public Optional<Animal> findAnimalById(int id) {
        return animalList.stream()
                .filter(animal -> animal.getId() == id)
                .findFirst();
    }
}
