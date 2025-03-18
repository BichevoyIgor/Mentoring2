package ru.bichevoy;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Data
@Slf4j
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Server server;
    private final DataOutputStream out;
    private final DataInputStream in;
    private GameRoom gameRoom;
    private GameService gameService;
    private String login;

    public ClientHandler(Socket socket, Server server, GameRoom gameRoom) {
        this.socket = socket;
        this.server = server;
        this.gameRoom = gameRoom;
        this.gameService = new GameService(server);
        try {
            this.out = new DataOutputStream(socket.getOutputStream());
            this.in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод обрабатывающий входящие сообщения на сокет
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String message = in.readUTF();
                if (message.equals(Commands.EXIT.getVal())) {
                    exitAction();
                    continue;
                }
                if (message.startsWith(Commands.LOGIN.getVal())) {
                    String[] enteredLogin = message.split("=", 2);
                    if (enteredLogin[1].isEmpty()) {
                        sendMessage("Вы не указали логин");
                        continue;
                    }
                    loginAction(enteredLogin[1]);
                }
                if (message.startsWith(Commands.CHAT.getVal())) {
                    String[] msgArray = message.split("=", 2);
                    String enteredMessage = msgArray.length >= 2 ? msgArray[1] : "";
                    chatAction(enteredMessage);
                }
                if (message.startsWith(Commands.GAME.getVal())) {
                    String[] msgArray = message.split("=", 2);
                    if (!gameService.canStart()) {
                        gameService.placeShipStep(msgArray[1], gameService, this, gameRoom);
                    } else {
                        gameService.shot(msgArray[1], gameRoom, this);
                    }
                }
            } catch (IOException e) {
                disconnect(e);
            }
        }
    }

    private void chatAction(String enteredMessage) {
        server.broadcastMessage(gameRoom, String.format("[%s]: %s", this.login, enteredMessage));
    }

    /**
     * Установка/смена логина
     */
    private void loginAction(String enteredLogin) {
        if (gameRoom.loginIsPresent(enteredLogin)) {
            sendMessage("Логин занят, придумайте другой:");
        } else if (this.login != null) {
            server.broadcastMessage(gameRoom, String.format("[%s]: сменил логин на %s", this.login, enteredLogin));
            log.info("{}: сменил логин на {}", this.login, enteredLogin);
            this.login = enteredLogin;
            sendMessage(Commands.LOGIN.getVal() + this.login);
        } else {
            this.login = enteredLogin;
            sendMessage(Commands.LOGIN.getVal() + this.login);
            log.info("Установлен логин {} для {} ", this.login, socket);
            gameRoom.setGamer(this);
            server.broadcastMessage(gameRoom, String.format("[%s]: присоединился к игре", this.login));
            log.info("Client {} added in game room {}", socket.getPort(), gameRoom);
            if (!gameRoom.haveFreeSpace()) {
                server.broadcastMessage(gameRoom, gameService.getConditions());
                server.broadcastMessage(gameRoom, gameService.getStringViewBattleField(gameService.getBattleField()));
                server.broadcastMessage(gameRoom, String.format("Укажите координаты %d-палубного корабля, через запятую (пример: А1,А2,А3,А4)", gameService.getSizeNextShip()));
            }
        }
    }

    /**
     * Завершение сессии
     */
    private void exitAction() {
        server.removeClient(this);
        Thread.currentThread().interrupt();
    }

    /**
     * Отправка сообщения в сокет
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
     */
    private void disconnect(IOException e) {
        log.error("Сессия клиента {} отключена, причина: {}", this.login, e.getMessage());
        exitAction();
    }
}
