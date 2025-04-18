package ru.bichevoy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.bichevoy.entity.Chicken;
import ru.bichevoy.entity.Corn;
import ru.bichevoy.entity.Cow;
import ru.bichevoy.entity.Warehouse;

import java.math.BigDecimal;


@Slf4j
@Component
public class MarketService {

    private final AnnotationConfigApplicationContext context;
    private final Warehouse warehouse;
    private final AnimalService animalService;
    private final BigDecimal chickenMeatCost;
    private final BigDecimal cowMeatCost;
    private final BigDecimal popCornCost;
    private final BigDecimal newChickenCost;
    private final BigDecimal newCowCost;
    private final BigDecimal eggCost;
    private final BigDecimal foodAnimalCost;
    private final BigDecimal waterCost;
    private final BigDecimal newCornCost;
    private final PlantService plantService;

    @Autowired
    public MarketService(Warehouse warehouse,
                         AnnotationConfigApplicationContext context,
                         AnimalService animalService,
                         @Value("${CHICKEN_MEAT_COST}") String chickenMeatCost,
                         @Value("${COW_MEAT_COST}") String cowMeatCost,
                         @Value("${POPCORN_COST}") String popCornCost,
                         @Value("${NEW_CHICKEN_COST}") String newChickenCost,
                         @Value("${NEW_COW_COST}") String newCowCost,
                         @Value("${EGG_COST}") String eggCost,
                         @Value("${FOOD_ANIMAL_COST}") String foodAnimalCost,
                         @Value("${WATER_COST}") String waterCost,
                         @Value("${NEW_CORN_COST}") String newCornCost,
                         PlantService plantService) {
        this.warehouse = warehouse;
        this.context = context;
        this.animalService = animalService;
        this.chickenMeatCost = new BigDecimal(chickenMeatCost);
        this.cowMeatCost = new BigDecimal(cowMeatCost);
        this.popCornCost = new BigDecimal(popCornCost);
        this.newChickenCost = new BigDecimal(newChickenCost);
        this.newCowCost = new BigDecimal(newCowCost);
        this.eggCost = new BigDecimal(eggCost);
        this.foodAnimalCost = new BigDecimal(foodAnimalCost);
        this.waterCost = new BigDecimal(waterCost);
        this.newCornCost = new BigDecimal(newCornCost);
        this.plantService = plantService;
    }

    public BigDecimal getFoodAnimalCost() {
        return new BigDecimal(foodAnimalCost.toString());
    }

    public BigDecimal getWaterCost() {
        return new BigDecimal(waterCost.toString());
    }

    public BigDecimal getNewCornCost() {
        return new BigDecimal(newCornCost.toString());
    }

    public void sellEgg(int count) {
        try {
            warehouse.spendEgg(count);
            BigDecimal gain = eggCost.multiply(BigDecimal.valueOf(count));
            warehouse.addMoney(gain);
            log.info("Продано яйцо на сумму: {}", gain);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void sellChickenMeat(int count) {
        try {
            warehouse.spendChickenMeat(count);
            BigDecimal gain = chickenMeatCost.multiply(BigDecimal.valueOf(count));
            warehouse.addMoney(gain);
            log.info("Продано куриное мясо на сумму: {}", gain);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void sellCowMeat(int count) {
        try {
            warehouse.spendCowMeat(count);
            BigDecimal gain = cowMeatCost.multiply(BigDecimal.valueOf(count));
            warehouse.addMoney(gain);
            log.info("Продана говядина на сумму: {}", gain);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void sellPopCorn(int count) {
        try {
            warehouse.spendPopCorn(count);
            BigDecimal gain = popCornCost.multiply(BigDecimal.valueOf(count));
            warehouse.addMoney(gain);
            log.info("Продан попкорн на сумму: {}", gain);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }

    public void buyChicken() {
        warehouse.spendMoney(newChickenCost);
        Chicken chicken = context.getBean("chicken", Chicken.class);
        animalService.addAnimal(chicken);
        log.info("Купили цыпленка");
    }

    public void buyCow() {
        warehouse.spendMoney(newCowCost);
        Cow cow = context.getBean("cow", Cow.class);
        animalService.addAnimal(cow);
        log.info("Купили корову");
    }

    public void buyAnimalFood(int countFoodForBuy) {
        warehouse.spendMoney(foodAnimalCost.multiply(new BigDecimal(countFoodForBuy)));
        warehouse.addFood(countFoodForBuy);
        log.info("Купили {} еды", countFoodForBuy);
    }

    public void buyWater(int countWater) {
        warehouse.spendMoney(waterCost.multiply(new BigDecimal(countWater)));
        warehouse.addWater(countWater);
        log.info("Купили {} воды", countWater);
    }

    public void buyNewCorn(int countCorn) {
        warehouse.spendMoney(newCornCost.multiply(new BigDecimal(countCorn)));
        for (int i = 0; i < countCorn; i++) {
            Corn corn = context.getBean("corn", Corn.class);
            plantService.addPlant(corn);
            log.info("Купили и посадили кукурузу");
        }
    }
}
