package ru.bichevoy;


import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 14. Создайте несколько профилей для приложения и продемонстрируйте выбор активного профиля.
 */
@Component
@Profile("prod")
public class ProdBean {
    public ProdBean() {
        System.out.println("Бин ProdBean создан");
    }
}
