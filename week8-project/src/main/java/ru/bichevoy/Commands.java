package ru.bichevoy;

import lombok.Getter;

@Getter
public enum Commands {
    SHOW_RESOURCES("/show_resources"),
    SHOW_ANIMALS("/show_animals"),
    SHOW_PLANTS("/show_plants"),
    MAKE_MEAT("/make_meat"),
    COLLECT_PLANT("/collect_plant"),
    SELL_CHICKEN_MEAT("/sell_chicken_meat"),
    SELL_POPCORN("/sell_popcorn"),
    SELL_COW_MEAT("/sell_cow_meat"),
    SELL_EGG("/sell_egg"),
    BUY_CHICKEN("/buy_chicken"),
    BUY_CORN("/buy_corn"),
    BUY_COW("/buy_cow"),
    BUY_ANIMAL_FOOD("/buy_animal_food"),
    BUY_WATER("/buy_water"),
    FEED_ANIMAL("/feed_animal"),
    WATER_PLANT("/water_plant"),
    EXIT("/exit");

    private final String command;

    Commands(String command) {
        this.command = command;
    }

}
