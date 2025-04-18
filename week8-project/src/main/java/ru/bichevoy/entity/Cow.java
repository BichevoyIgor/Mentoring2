package ru.bichevoy.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Cow extends Animal {

    public Cow(@Value("${COW.MAX_RESOURCE}") int maxResource,
               @Value("${COW.GROW_PER_DAY}") int growPerDay) {
        super("Корова", maxResource, growPerDay);
    }

    @Override
    public void satietyDown() {
        setSatiety(Math.max((getSatiety() - 2), 0));
    }
}
