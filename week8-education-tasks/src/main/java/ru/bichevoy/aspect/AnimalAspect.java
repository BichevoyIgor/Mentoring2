package ru.bichevoy.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.stereotype.Component;

/**
 * 16. Напишите аспект, который перехватывает вызовы методов и выводит информацию о их исполнении.
 * Join point
 * pointcut
 */
@Component
@Aspect
public class AnimalAspect {

    @Before("execution(* ru.bichevoy.Animal+.makeSound())")
    public void beforeAnimalMakeSound(JoinPoint joinPoint) {
        SourceLocation sourceLocation = joinPoint.getSourceLocation();
        System.out.println("Сейчас прозвучит животный звук из класса " + sourceLocation.getWithinType().getSimpleName());
    }
}
