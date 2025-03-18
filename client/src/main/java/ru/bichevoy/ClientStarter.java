package ru.bichevoy;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class ClientStarter {

    public static void main(String[] args) {
        String host = getProperties().getProperty("HOST");
        int port = Integer.parseInt(getProperties().getProperty("PORT"));
        new Client(host, port);
    }

    static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get("config/client.config"))) {
            properties.load(input);
        } catch (IOException e) {
            log.error("Ошибка загрузки client.config\n{}", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException("Ошибка загрузки client.config", e);
        }
        return properties;
    }
}