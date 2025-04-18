package ru.bichevoy.entity;

import lombok.Data;

@Data
public abstract class Plant implements Growable, Satietyable {

    private int id;
    private final String title;
    private final int MAX_RESOURCE;
    private final int GROW_PER_DAY;
    private int currentResource;
    private int health;
    private int satiety;

    public Plant(String title, int MAX_RESOURCE, int GROW_PER_DAY) {
        this.MAX_RESOURCE = MAX_RESOURCE;
        this.GROW_PER_DAY = GROW_PER_DAY;
        this.health = 100;
        this.satiety = 100;
        this.title = title;
    }

    @Override
    public void satietyUp(int countWater) {
        if (countWater >= 0) {
            satiety = Math.min(satiety + countWater, 100);
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
        return String.format("%s #%s: зрелость [%d/%d], насыщенность водой [%d], здоровье [%d]",
                title,
                id,
                currentResource,
                MAX_RESOURCE,
                satiety,
                health);
    }
}
