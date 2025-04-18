package ru.bichevoy.repository;

import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Plant;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class PlantRepositoryImpl implements PlantRepository {

    private final List<Plant> plantList;
    private final AtomicInteger idCounter;

    public PlantRepositoryImpl() {
        plantList = new ArrayList<>();
        idCounter = new AtomicInteger();
    }

    @Override
    public List<Plant> findAllPlant() {
        return List.copyOf(plantList);
    }

    @Override
    public void addPlant(Plant plant) {
        plantList.add(plant);
        plant.setId(idCounter.incrementAndGet());
    }

    @Override
    public void removePlant(Plant plant) {
        plantList.remove(plant);
    }

    @Override
    public Optional<Plant> findPlantById(int id) {
        return plantList.stream()
                .filter(plant -> plant.getId() == id)
                .findFirst();
    }
}
