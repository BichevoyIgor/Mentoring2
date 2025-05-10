package ru.bichevoy;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class ProgramProperties {

    public static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream input = ProgramProperties.class.getClassLoader().getResourceAsStream("program.properties")) {
            if (input == null) {
                log.error("program.properties не найден");
                throw new RuntimeException("program.properties не найден");
            }
            properties.load(input);
        } catch (IOException e) {
            log.error("Ошибка загрузки program.properties", e);
            throw new RuntimeException("Ошибка загрузки program.properties", e);
        }
        return properties;
    }
}
