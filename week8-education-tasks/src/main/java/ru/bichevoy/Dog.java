package ru.bichevoy;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 9. Добавьте аннотацию @Primary к одному из бинов и продемонстрируйте его инъекцию как предпочтительного.
 */

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class Dog extends Animal {

    @Override
    public void makeSound() {
        System.out.println("Гав");
    }
}
