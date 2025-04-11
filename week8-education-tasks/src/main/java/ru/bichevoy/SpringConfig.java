package ru.bichevoy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * 7. Создайте бин, для которого зависимости внедряются через методы-сеттеры, и объясните их применение.
 * 10. Настройте бин с использованием @Value для инъекции значений из файла свойств.
 * 11. Напишите класс конфигурации с методами @Bean, создающими и возвращающими бины.
 * 12. Настройте сканирование компонентов в проекте и зарегистрируйте бин с помощью @Component.
 */
@Configuration
@ComponentScan("ru.bichevoy")
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class SpringConfig {

    @Bean
    public Animal getDog(@Value("${dog.name}") String dogName) {
        Dog dog = new Dog();
        dog.setName(dogName);
        return dog;
    }
}
