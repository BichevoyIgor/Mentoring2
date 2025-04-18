package ru.bichevoy.entity;

import lombok.Data;

@Data
public abstract class Animal implements Growable, Satietyable {
    private int id;
    private final String title;
    private final int MAX_RESOURCE;
    private final int GROW_PER_DAY;
    private int currentResource;
    private int health;
    private int satiety;

    public Animal(String title, int MAX_RESOURCE, int GROW_PER_DAY) {
        this.title = title;
        this.MAX_RESOURCE = MAX_RESOURCE;
        this.GROW_PER_DAY = GROW_PER_DAY;
        this.health = 100;
        this.satiety = 100;
    }

    @Override
    public void satietyUp(int countFood) {
        if (countFood >= 0) {
            satiety = Math.min(satiety + countFood, 100);
        }
    }

    @Override
    public void grow() {
        if (getCurrentResource() < getMAX_RESOURCE()) {
            int newResource = getCurrentResource() + getGROW_PER_DAY();
            setCurrentResource(Math.min(newResource, getMAX_RESOURCE()));
        }
    }

    @Override
    public String toString() {
        return String.format("%s #%s: зрелость [%d/%d], cытость [%d], здоровье [%d]",
                title,
                id,
                currentResource,
                MAX_RESOURCE,
                satiety,
                health);
    }
}
