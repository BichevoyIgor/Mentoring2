package ru.bichevoy;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.bichevoy.service.CardService;

public class Main {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        CardService cardService = context.getBean("cardService", CardService.class);
        new Program(cardService).start();
    }
}