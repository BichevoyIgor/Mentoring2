package ru.bichevoy.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Corn extends Plant {

    public Corn(@Value("${CORN.MAX_RESOURCE}") int maxResource,
                @Value("${CORN.GROW_PER_DAY}") int growPerDay) {
        super("Кукуруза", maxResource, growPerDay);
    }

    @Override
    public void satietyDown() {
        setSatiety(Math.max((getSatiety() - 1), 0));
    }
}
