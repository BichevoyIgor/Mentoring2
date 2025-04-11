package ru.bichevoy;

import ru.bichevoy.entity.Card;
import ru.bichevoy.entity.Command;
import ru.bichevoy.service.CardService;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Program {

    private final CardService cardService;
    private final Scanner scanner;
    private int counter;

    public Program(CardService cardService) {
        this.cardService = cardService;
        this.scanner = new Scanner(System.in);
    }

    private void showCommands() {
        for (Command value : Command.values()) {
            System.out.println(value);
        }
        System.out.println();
    }

    public void start() {
        showCommands();
        String answer;
        while (true) {
            System.out.printf("Ваша команда [%s]: ", Arrays.stream(Command.values())
                    .map(Command::getVal)
                    .collect(Collectors.joining(", ")));

            answer = scanner.nextLine().trim().toLowerCase();
            if (Command.EXIT_GAME.getVal().equals(answer)) {
                return;
            }
            if (answer.startsWith(Command.ADD_CARD.getVal())) {
                addCard();
                continue;
            }
            if (answer.startsWith(Command.REMOVE_CARD.getVal())) {
                removeCard();
                continue;
            }
            if (answer.startsWith(Command.SHOW_ALL_CARD.getVal())) {
                if (cardService.getRepositorySize() == 0) {
                    System.out.println("Карточек нет");
                    continue;
                }
                cardService.getAllCards()
                        .forEach(System.out::println);
                continue;
            }
            if (answer.startsWith(Command.GET_WORD.getVal())) {
                getWord();
            }
        }
    }

    /**
     * Удаление карточки по id
     */
    private void removeCard() {
        while (true) {
            System.out.println("Укажите id карточки для удаления");
            String answer = scanner.nextLine().trim();
            if (!answer.isEmpty()) {
                try {
                    cardService.remove(Integer.parseInt(answer));
                    return;
                } catch (NumberFormatException e) {
                    System.out.println("Вы указали не id");
                }
            }
        }
    }

    /**
     * Загадать рандомное слово
     */
    private void getWord() {
        Optional<Card> randomCard = cardService.getRandomCard();
        if (randomCard.isEmpty()) {
            System.out.println("Карточек нет");
            return;
        }
        while (true) {
            Card card = randomCard.get();
            System.out.printf("%s - укажите перевод:\n", card.getWord());
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.isEmpty()) {
                Optional<String> result = card.getTranslate().stream()
                        .filter(w -> w.equals(answer))
                        .findFirst();
                if (result.isPresent()) {
                    counter++;
                    System.out.printf("Верно, вы правильно ответили %d раз\n", counter);
                    return;
                } else {
                    System.out.println("Не верно");
                }
            }
        }
    }

    /**
     * Добавление новой карточки
     */
    private void addCard() {
        String word;
        Set<String> translateWords;

        while (true) {
            System.out.println("Введите слово:");
            word = scanner.nextLine().trim().toLowerCase();
            if (word.isEmpty()) {
                System.out.println("Вы не указали слово");
            } else {
                break;
            }
        }

        while (true) {
            System.out.println("Введите перевод через запятую, если слов несколько:");
            translateWords = Arrays.stream(scanner.nextLine().split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());
            if (translateWords.isEmpty()) {
                System.out.println("Вы не указали перевод");
            } else {
                break;
            }
        }

        Card card = new Card(word, translateWords);
        cardService.add(card);
    }
}

