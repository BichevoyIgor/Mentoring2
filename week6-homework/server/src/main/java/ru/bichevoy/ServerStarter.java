package ru.bichevoy;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class ServerStarter {
    public static void main(String[] args) {
        int port = Integer.parseInt(getProperties().getProperty("PORT"));
        int clients = Integer.parseInt(getProperties().getProperty("AVAILABLE_COUNT_CLIENTS"));
        new Server(port, clients);
    }

    static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get("config/server.config"))) {
            properties.load(input);
        } catch (IOException e) {
            log.error("Ошибка загрузки server.config\n{}", String.valueOf(e));
            throw new RuntimeException("Ошибка загрузки server.config", e);
        }
        return properties;
    }
}
