package ru.bichevoy;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
@Slf4j
public class Server {
    private final int PORT;
    private final int AVAILABLE_COUNT_CLIENTS;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ClientHandler> clients;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<GameRoom> gameRoomList;
    private boolean serverOn;

    public Server(int PORT, int availableCountClients) {
        this.PORT = PORT;
        this.AVAILABLE_COUNT_CLIENTS = availableCountClients;
        this.serverOn = true;
        clients = new ArrayList<>();
        gameRoomList = new ArrayList<>();
        serverRun();
    }

    private void serverRun() {
        log.info("Server started");
        ExecutorService executorService = Executors.newFixedThreadPool(AVAILABLE_COUNT_CLIENTS);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (serverOn) {
                Socket clientSocket = serverSocket.accept();
                log.info("Client connected {}", clientSocket);
                GameRoom gameRoom = getGameRoom();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this, gameRoom);
                clients.add(clientHandler);
                executorService.execute(clientHandler);
            }
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Отправка сообщения в чат комнаты
     */
    public void broadcastMessage(GameRoom gameRoom, String msg) {
        if (gameRoom.getGamer1() != null) {
            gameRoom.getGamer1().sendMessage(msg);
        }
        if (gameRoom.getGamer2() != null) {
            gameRoom.getGamer2().sendMessage(msg);
        }
    }

    /**
     * Метод возвращает комнату свободную для игрока
     */
    private GameRoom getGameRoom() {
        for (GameRoom gameRoom : gameRoomList) {
            if (gameRoom.haveFreeSpace()) {
                return gameRoom;
            }
        }
        GameRoom gameRoom = new GameRoom();
        gameRoomList.add(gameRoom);
        return gameRoom;
    }

    void removeClient(ClientHandler client) {
        clients.remove(client);
        GameRoom gameRoom = client.getGameRoom();
        gameRoom.removeClient(client);
        broadcastMessage(gameRoom, String.format("Игрок %s покинул игру", client.getLogin()));
        if (gameRoom.isEmpty()) {
            gameRoomList.remove(gameRoom);
        }
        log.info("Client logout {}", client.getLogin());
    }
}
