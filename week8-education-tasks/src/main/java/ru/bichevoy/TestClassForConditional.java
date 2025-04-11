package ru.bichevoy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * 18. Определите бин, который создается только при выполнении определенного условия.
 */
@Component
@ConditionalOnBean(ProdBean.class)
public class TestClassForConditional {
    public TestClassForConditional() {
        System.out.println("TestClassForConditional создан потому что бин ProdBean создан");
    }
}
