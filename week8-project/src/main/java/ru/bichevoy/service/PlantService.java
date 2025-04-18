package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.entity.Plant;
import ru.bichevoy.repository.PlantRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final PlantRepository plantRepository;

    public List<Plant> findAllPlant() {
        return plantRepository.findAllPlant();
    }

    public void addPlant(Plant plant) {
        plantRepository.addPlant(plant);
    }

    public void removePlant(Plant plant) {
        plantRepository.removePlant(plant);
    }

    public Optional<Plant> findPlantById(int id) {
        return plantRepository.findPlantById(id);
    }
}
