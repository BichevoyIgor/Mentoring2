package ru.bichevoy;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import ru.bichevoy.entity.Chicken;
import ru.bichevoy.entity.Corn;
import ru.bichevoy.entity.Cow;
import ru.bichevoy.service.AnimalService;
import ru.bichevoy.service.PlantService;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        initStartParameters(context);
    }

    private static void initStartParameters(AnnotationConfigApplicationContext context) {
        AnimalService animalService = context.getBean("animalService", AnimalService.class);
        PlantService plantService = context.getBean("plantService", PlantService.class);
        ConfigurableEnvironment environment = context.getEnvironment();

        Integer startChicken = environment.getProperty("START.CHICKEN.COUNT", Integer.class);
        if (startChicken == null) {
            startChicken = 0;
        }
        for (int i = 0; i < startChicken; i++) {
            animalService.addAnimal(context.getBean("chicken", Chicken.class));
        }

        Integer startCow = environment.getProperty("START.COW.COUNT", Integer.class);
        if (startCow == null) {
            startCow = 0;
        }
        for (int i = 0; i < startCow; i++) {
            animalService.addAnimal(context.getBean("cow", Cow.class));
        }

        Integer startCorn = environment.getProperty("START.CORN.COUNT", Integer.class);
        if (startCorn == null) {
            startCorn = 0;
        }
        for (int i = 0; i < startCorn; i++) {
            plantService.addPlant(context.getBean("corn", Corn.class));
        }
    }
}