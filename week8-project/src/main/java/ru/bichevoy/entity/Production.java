package ru.bichevoy.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bichevoy.service.AnimalService;
import ru.bichevoy.service.PlantService;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Production {
    private final AnimalService animalService;
    private final PlantService plantService;
    private final Warehouse warehouse;

    public boolean makeMeat(int animalId) {
        Optional<Animal> foundedAnimal = animalService.findAnimalById(animalId);
        if (foundedAnimal.isEmpty()) {
            return false;
        }

        if (foundedAnimal.get() instanceof Chicken) {
            warehouse.addChickenMeat(foundedAnimal.get().getCurrentResource());
        } else if (foundedAnimal.get() instanceof Cow) {
            warehouse.addCowMeat(foundedAnimal.get().getCurrentResource());
        }
        animalService.removeAnimal(foundedAnimal.get());
        return true;
    }

    public void collectGrow(Plant plant) {
        if (plant instanceof Corn) {
            warehouse.addPopCorn(plant.getCurrentResource());
        }
        plantService.removePlant(plant);
    }
}
