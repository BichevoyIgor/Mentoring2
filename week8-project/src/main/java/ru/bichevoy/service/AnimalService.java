package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bichevoy.entity.Animal;
import ru.bichevoy.repository.AnimalRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimalService {

    private final AnimalRepository animalRepository;

    public List<Animal> findAllAnimal() {
        return animalRepository.findAllAnimal();
    }

    public void addAnimal(Animal animal) {
        animalRepository.addAnimal(animal);
    }

    public void removeAnimal(Animal animal) {
        animalRepository.removeAnimal(animal);
    }

    public Optional<Animal> findAnimalById(int animalId) {
        return animalRepository.findAnimalById(animalId);
    }

}
