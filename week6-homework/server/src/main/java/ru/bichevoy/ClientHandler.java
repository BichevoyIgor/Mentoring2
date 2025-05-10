package ru.bichevoy;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.List;

@Data
@Slf4j
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private DataOutputStream out;
    private DataInputStream in;
    private CatFactService catFactService;

    private String login;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            catFactService = new CatFactService();
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод обрабатывающий входящие сообщения на сервер от конкретного сокета
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String message = in.readUTF();
                if (message.startsWith("LOGIN=")) {
                    String enteredLogin = message.split("=")[1];
                    if (server.loginIsPresent(enteredLogin)) {
                        sendMessage("Логин занят, придумайте другой:");
                        continue;
                    } else if (this.login != null) {
                        server.broadcastMessage(String.format("[%s]: сменил логин на %s", this.login, enteredLogin));
                        log.info("{}: сменил логин на {}", this.login, enteredLogin);
                        this.login = enteredLogin;
                        sendMessage("LOGIN=" + this.login);
                        continue;
                    } else {
                        this.login = enteredLogin;
                        sendMessage("LOGIN=" + this.login);
                        log.info("Установлен логин {} для {} ", this.login, socket);
                        List<String> messageHistory = server.getMessageRepositoryService().getMessage(10);
                        for (String mess : messageHistory) {
                            sendMessage(mess);
                        }
                        server.broadcastMessage(String.format("[%s]: присоединился к чату", this.login));
                        continue;
                    }
                }
                if ("CATFACT".equals(message)) {
                    try {
                        message = catFactService.getCatFact();
                        log.info("Generate catFact for user {}", login);
                    } catch (IOException e) {
                        sendMessage("Сервис информации о котах не доступен");
                        log.error(String.valueOf(e));
                        continue;
                    }
                }
                if ("EXIT".equals(message)) {
                    server.removeClient(this);
                    Thread.currentThread().interrupt();
                    continue;
                }
                server.broadcastMessage(String.format("[%s]: %s", this.login, message));
            } catch (IOException e) {
                disconnect(e);
            }
        }
    }

    /**
     * Отправка сообщения в сокет
     *
     * @param message
     */
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            disconnect(e);
        }
    }

    /**
     * Метод завершения сессии на сервере при потере соединения с клиентом
     *
     * @param e
     */
    private void disconnect(IOException e) {
        log.error("Сессия клиента {} отключена, причина: {}", this.login, e.getMessage());
        server.removeClient(this);
        Thread.currentThread().interrupt();
    }
}
