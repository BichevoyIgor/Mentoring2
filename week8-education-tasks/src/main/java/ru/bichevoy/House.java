package ru.bichevoy;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 2. Напишите Java-класс, который использует @Component для определения бина и @Autowired для внедрения его зависимости.
 * 5. Создайте бин с методами, аннотированными @PostConstruct и @PreDestroy, и выведите сообщения до и после его использования.
 * 6. Определите класс с зависимостями, внедренными через конструктор, и продемонстрируйте его использование.
 * 8. Создайте несколько бинов одного интерфейса и используйте @Qualifier для выбора конкретного бина в инъекции.
 */
@Data
@Component
public class House {
    private final String address;
    private Person person;
    private Animal animal;

    @Autowired
    public House(@Value("${house.adress}") String address, Person person, @Qualifier("cat") Animal animal) {
        this.address = address;
        this.person = person;
        this.animal = animal;
    }

    @PostConstruct
    private void init(){
        System.out.println("Бин House создан");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("Бин House уничтожен");
    }
}
