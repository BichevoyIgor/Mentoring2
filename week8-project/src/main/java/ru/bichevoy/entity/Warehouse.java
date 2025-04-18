package ru.bichevoy.entity;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class Warehouse {
    private int cowMeat;
    private int chickenMeat;
    @Getter
    private int egg;
    private int popCorn;
    @Getter
    private int water;
    @Getter
    private int food;
    private BigDecimal money;

    public Warehouse(@Value("${START.MONEY.COUNT}") String money) {
        this.money = new BigDecimal(money);
    }

    public void addCowMeat(int count) {
        if (count >= 0) {
            cowMeat += count;
        } else {
            throw new IllegalArgumentException("Значение не может быть отрицательным");
        }
    }

    public void addWater(int count) {
        checkCount(count);
        water += count;
    }

    public void addChickenMeat(int count) {
        checkCount(count);
        chickenMeat += count;
    }

    public void addEgg(int count) {
        checkCount(count);
        egg += count;
    }

    public void addFood(int count) {
        checkCount(count);
        food += count;
    }

    public void addPopCorn(int count) {
        checkCount(count);
        popCorn += count;
    }

    public void addMoney(BigDecimal money) {
        if (money.doubleValue() >= 0) {
            this.money = this.money.add(money);
        } else {
            throw new IllegalArgumentException("Значение не может быть отрицательным");
        }
    }

    public void spendCowMeat(int count) {
        if (count > 0 && count <= cowMeat) {
            cowMeat -= count;
        } else {
            throw new IllegalArgumentException("Не достаточно CowMeat");
        }
    }

    public void spendChickenMeat(int count) {
        if (count > 0 && count <= chickenMeat) {
            chickenMeat -= count;
        } else {
            throw new IllegalArgumentException("Не достаточно ChickenMeat");
        }
    }

    public void spendEgg(int count) {
        if (count > 0 && count <= egg) {
            egg -= count;
        } else {
            throw new IllegalArgumentException("Не достаточно Egg");
        }
    }

    public void spendPopCorn(int count) {
        if (count > 0 && count <= popCorn) {
            popCorn -= count;
        } else {
            throw new IllegalArgumentException("Не достаточно PopCorn");
        }
    }

    public void spendMoney(BigDecimal money) {
        if (this.money.compareTo(money) >= 0) {
            this.money = this.money.subtract(money);
        } else {
            throw new IllegalArgumentException("Не достаточно денег");
        }
    }

    public void spendFood(int countFood) {
        if (food >= countFood) {
            food -= countFood;
        } else {
            throw new IllegalArgumentException("Не достаточно food на складе");
        }
    }

    public void spendWater(int countWater) {
        if (water >= countWater){
            water -= countWater;
        } else {
            throw new IllegalArgumentException("Не достаточно water на складе");
        }
    }

    public BigDecimal getBalance() {
        return new BigDecimal(money.toString());
    }

    private void checkCount(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Значение не может быть отрицательным");
        }
    }

    @Override
    public String toString() {
        return "Ресурсы:" +
                "\n    Говядина: " + cowMeat +
                "\n    Куриное мясо: " + chickenMeat +
                "\n    Яйца: " + egg +
                "\n    Поп корн: " + popCorn +
                "\n    Корм для животных: " + food +
                "\n    Вода: " + water +
                "\n    Деньги: " + money;
    }
}
