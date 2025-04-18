package ru.bichevoy.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Chicken extends Animal {
    private int levelBirthToEgg;

    public Chicken(@Value("${CHICKEN.MAX_RESOURCE}") int maxResource,
                   @Value("${CHICKEN.GROW_PER_DAY}") int growPerDay) {
        super("Курица", maxResource, growPerDay);
        this.levelBirthToEgg = 0;
    }

    public Optional<Egg> birthEgg() {
        if (getCurrentResource() == getMAX_RESOURCE()) {
            if (levelBirthToEgg < 3) {
                levelBirthToEgg++;
            } else {
                levelBirthToEgg = 0;
                return Optional.of(new Egg());
            }
        }
        return Optional.empty();
    }

    @Override
    public void satietyDown() {
        setSatiety(Math.max((getSatiety() - 1), 0));
    }
}
