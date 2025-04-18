package ru.bichevoy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.bichevoy.entity.Animal;
import ru.bichevoy.entity.Plant;
import ru.bichevoy.entity.Warehouse;
import ru.bichevoy.service.AnimalService;
import ru.bichevoy.service.MarketService;
import ru.bichevoy.service.PlantService;
import ru.bichevoy.entity.Production;

import java.util.Optional;
import java.util.Scanner;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeyboardListener {

    private final AnimalService animalService;
    private final PlantService plantService;
    private final Warehouse warehouse;
    private final Production production;
    private final MarketService marketService;
    private final AnnotationConfigApplicationContext context;
    private final Scanner scanner = new Scanner(System.in);

    @PostConstruct
    public void init() {
        Thread thread = new Thread(() -> {

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String command = scanner.nextLine();
                    if (Commands.SHOW_ANIMALS.getCommand().equals(command)) {
                        animalService.findAllAnimal().forEach(System.out::println);
                    } else if (Commands.SHOW_PLANTS.getCommand().equals(command)) {
                        plantService.findAllPlant().forEach(System.out::println);
                    } else if (Commands.SHOW_RESOURCES.getCommand().equals(command)) {
                        log.info(String.valueOf(warehouse));
                    } else if (Commands.MAKE_MEAT.getCommand().equals(command)) {
                        makeMeat();
                    } else if (Commands.COLLECT_PLANT.getCommand().equals(command)) {
                        collectPlant();
                    } else if (Commands.SELL_CHICKEN_MEAT.getCommand().equals(command)) {
                        log.info("Укажите количество куриного мяса:");
                        marketService.sellChickenMeat(Integer.parseInt(scanner.nextLine()));
                    } else if (Commands.SELL_COW_MEAT.getCommand().equals(command)) {
                        log.info("Укажите количество говядины:");
                        marketService.sellCowMeat(Integer.parseInt(scanner.nextLine()));
                    } else if (Commands.SELL_POPCORN.getCommand().equals(command)) {
                        log.info("Укажите количество попкорна:");
                        marketService.sellPopCorn(Integer.parseInt(scanner.nextLine()));
                    } else if (Commands.SELL_EGG.getCommand().equals(command)) {
                        log.info("Укажите количество яиц для продажи(доступно {}):", warehouse.getEgg());
                        marketService.sellEgg(Integer.parseInt(scanner.nextLine()));
                    } else if (Commands.EXIT.getCommand().equals(command)) {
                        context.close();
                    } else if (Commands.BUY_CHICKEN.getCommand().equals(command)) {
                        marketService.buyChicken();
                    } else if (Commands.BUY_COW.getCommand().equals(command)) {
                        marketService.buyCow();
                    } else if (Commands.FEED_ANIMAL.getCommand().equals(command)) {
                        feedAnimal();
                    } else if (Commands.BUY_ANIMAL_FOOD.getCommand().equals(command)) {
                        log.info("Укажите количество еды для покупки (стоимость {}, баланс {}):",
                                marketService.getFoodAnimalCost(), warehouse.getBalance());
                        marketService.buyAnimalFood(Integer.parseInt(scanner.nextLine()));
                    } else if (Commands.BUY_WATER.getCommand().equals(command)) {
                        log.info("Укажите количество воды для покупки (стоимость {}, баланс {}):",
                                marketService.getWaterCost(), warehouse.getBalance());
                        marketService.buyWater(Integer.parseInt(scanner.nextLine()));
                    } else if (Commands.WATER_PLANT.getCommand().equals(command)) {
                        waterPlant();
                    } else if (Commands.BUY_CORN.getCommand().equals(command)) {
                        log.info("Укажите количество грядок кукуруз для покупки (стоимость {}, баланс {}):",
                                marketService.getNewCornCost(), warehouse.getBalance());
                        marketService.buyNewCorn(Integer.parseInt(scanner.nextLine()));
                    } else {
                        log.info("command unknown");
                    }
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Полить растения
     */
    private void waterPlant() {
        log.info("Укажите id грядки для полива");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Plant> plantById = plantService.findPlantById(id);
        if (plantById.isPresent()) {
            Plant foundedPlant = plantById.get();
            log.info("Укажите количество воды полива (баланс {}, растение {})",
                    warehouse.getWater(),
                    foundedPlant.getSatiety());
            int countWater = Integer.parseInt(scanner.nextLine());
            warehouse.spendWater(countWater);
            foundedPlant.satietyUp(countWater);
            log.info("Полили грядку {}", foundedPlant);
        } else {
            log.info("Грядка не найдена");
        }
    }

    /**
     * Покормить животных
     */
    private void feedAnimal() {
        log.info("Укажите id животного:");
        int id = Integer.parseInt(scanner.nextLine());
        Optional<Animal> animalById = animalService.findAnimalById(id);
        if (animalById.isPresent()) {
            Animal foundedAnimal = animalById.get();
            log.info("Укажите количество еды для животного (еды на складе {}, сытость {} {})",
                    warehouse.getFood(),
                    foundedAnimal.getTitle(),
                    foundedAnimal.getSatiety());
            int countFood = Integer.parseInt(scanner.nextLine());
            warehouse.spendFood(countFood);
            foundedAnimal.satietyUp(countFood);
            log.info("Покормили животное {}", foundedAnimal);
        } else {
            log.info("Животное не найдена");
        }
    }

    /**
     * Собрать растения
     */
    private void collectPlant() {
        log.info("Укажите номер грядки");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Optional<Plant> foundedPlantOpt = plantService.findPlantById(id);
            if (foundedPlantOpt.isPresent()) {
                Plant foundedPlant = foundedPlantOpt.get();
                if (foundedPlant.getCurrentResource() == foundedPlant.getMAX_RESOURCE()) {
                    production.collectGrow(foundedPlant);
                    log.info("Растение {} собрано", foundedPlant.getTitle());
                } else {
                    log.info("Растение еще не созрело");
                }
            } else {
                log.info("Не верно указан номер грядки");
            }
        } catch (NumberFormatException e) {
            log.error(e.toString());
        }
    }

    /**
     * Разделать животное
     */
    private void makeMeat() {
        log.info("Укажите номер животного");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (production.makeMeat(id)) {
                log.info("Сделано");
            } else {
                log.info("Не верно указан номер");
            }
        } catch (NumberFormatException e) {
            log.error(e.toString());
        }
    }
}
