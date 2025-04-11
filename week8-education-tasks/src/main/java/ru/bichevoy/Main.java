package ru.bichevoy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println(context.getBean("person", Person.class));

        AnnotationConfigApplicationContext javaConfig = new AnnotationConfigApplicationContext(SpringConfig.class);
        House house = javaConfig.getBean("house", House.class);
        System.out.println(house);
        house.getAnimal().makeSound();

        javaConfig.close();
    }
}