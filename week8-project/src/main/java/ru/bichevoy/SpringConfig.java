package ru.bichevoy;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.bichevoy")
@PropertySource("classpath:application.properties")
public class SpringConfig {
}
