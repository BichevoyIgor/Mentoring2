package ru.bichevoy.repository;

import ru.bichevoy.entity.Plant;

import java.util.List;
import java.util.Optional;

public interface PlantRepository {

    List<Plant> findAllPlant();

    void addPlant(Plant plant);

    void removePlant(Plant plant);

    Optional<Plant> findPlantById(int id);
}
