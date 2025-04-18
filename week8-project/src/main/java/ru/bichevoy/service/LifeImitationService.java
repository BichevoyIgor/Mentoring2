package ru.bichevoy.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.bichevoy.entity.Animal;
import ru.bichevoy.entity.Chicken;
import ru.bichevoy.entity.Egg;
import ru.bichevoy.entity.Growable;
import ru.bichevoy.entity.Plant;
import ru.bichevoy.entity.Satietyable;
import ru.bichevoy.entity.Warehouse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class LifeImitationService {
    private final ScheduledExecutorService executorService;
    private final AnimalService animalService;
    private final PlantService plantService;
    private final int DELAY;
    private final AtomicInteger dayCounter;
    private final Warehouse warehouse;

    @Autowired
    public LifeImitationService(AnimalService animalService,
                                PlantService plantService,
                                @Value("${LIFE.DELAY}") int delay, Warehouse warehouse) {
        this.DELAY = delay;
        this.animalService = animalService;
        this.plantService = plantService;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.dayCounter = new AtomicInteger();
        this.warehouse = warehouse;
    }

    @PostConstruct
    private void lifeStart() {
        executorService.scheduleWithFixedDelay(() -> {

            List<Animal> matureAnimals = animalService.findAllAnimal().stream()
                    .peek(Satietyable::satietyDown)
                    .peek(this::livedAnimalDay)
                    .filter(animal -> animal.getHealth() >= 0)
                    .peek(Growable::grow)
                    .filter(el -> el.getCurrentResource() == el.getMAX_RESOURCE())
                    .toList();

            List<Plant> maturePlants = plantService.findAllPlant().stream()
                    .peek(Satietyable::satietyDown)
                    .peek(this::livedPlantDay)
                    .filter(plant -> plant.getHealth() >= 0)
                    .peek(Growable::grow)
                    .filter(el -> el.getCurrentResource() == el.getMAX_RESOURCE())
                    .toList();
            log.info("Новый день №{}", dayCounter.incrementAndGet());

            if (!matureAnimals.isEmpty()) {
                log.info("Взрослые животные:");
                matureAnimals.forEach(el -> log.info(el.toString()));
                tryBirthChickenEgg(matureAnimals);
            }
            if (!maturePlants.isEmpty()) {
                log.info("Зрелые растения:");
                maturePlants.forEach(el -> log.info(el.toString()));
            }

        }, 0, DELAY, TimeUnit.SECONDS);
    }

    /**
     * Попытка снести яйцо
     * @param matureAnimals
     */
    private void tryBirthChickenEgg(List<Animal> matureAnimals) {
        for (Animal matureAnimal : matureAnimals) {
            if (matureAnimal instanceof Chicken) {
                Optional<Egg> egg = ((Chicken) matureAnimal).birthEgg();
                if (egg.isPresent()) {
                    warehouse.addEgg(1);
                    log.info("{} #{} снесла яйцо", matureAnimal.getTitle(), matureAnimal.getId());
                }
            }
        }
    }

    /**
     * Один день жизни животного
     *
     * @param animal
     */
    private void livedAnimalDay(Animal animal) {
        if (animal.getSatiety() >= 0 && animal.getHealth() < 100) {
            animal.setHealth(animal.getHealth() + 1);
        }
        if (animal.getSatiety() <= 0) {
            animal.setHealth(animal.getHealth() - 20);
        }
        if (animal.getHealth() < 0) {
            log.info("{} #{} погибла от голода", animal.getTitle(), animal.getId());
            animalService.removeAnimal(animal);
        }
    }

    /**
     * Один день жизни растения
     *
     * @param plant
     */
    private void livedPlantDay(Plant plant) {
        if (plant.getSatiety() >= 0 && plant.getHealth() < 100) {
            plant.setHealth(plant.getHealth() + 1);
        }
        if (plant.getSatiety() <= 0) {
            plant.setHealth(plant.getHealth() - 5);
        }
        if (plant.getHealth() < 0) {
            log.info("{} #{} погибло от засухи", plant.getTitle(), plant.getId());
            plantService.removePlant(plant);
        }
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }
}
