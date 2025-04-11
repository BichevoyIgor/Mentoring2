package ru.bichevoy;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 4. Определите бин с областью prototype и продемонстрируйте создание нескольких экземпляров этого бина.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Data
public class Person {
    private String name;

    public Person(@Value("${person.name}") String name) {
        this.name = name;
    }
}
