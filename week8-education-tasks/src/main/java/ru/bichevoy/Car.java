package ru.bichevoy;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 13. Определите ленивый бин и продемонстрируйте, что он не создается до обращения.
 */

@Component
@Lazy
@Data
public class Car {
    public Car() {
        System.out.println("Бин Car создан");
    }
}
