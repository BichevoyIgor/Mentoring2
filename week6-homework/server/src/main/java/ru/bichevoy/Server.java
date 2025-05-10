package ru.bichevoy;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.bichevoy.repository.MessageRepositoryImpl;
import ru.bichevoy.repository.MessageRepositoryService;

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
    private List<ClientHandler> clients;
    private boolean serverOn;
    private MessageRepositoryService messageRepositoryService;

    public Server(int PORT, int availableCountClients) {
        this.PORT = PORT;
        this.AVAILABLE_COUNT_CLIENTS = availableCountClients;
        this.serverOn = true;
        clients = new ArrayList<>();
        messageRepositoryService = new MessageRepositoryService(new MessageRepositoryImpl());
        serverRun();
    }

    private void serverRun() {
        log.info("Server started");
        ExecutorService executorService = Executors.newFixedThreadPool(AVAILABLE_COUNT_CLIENTS);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (serverOn) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                executorService.execute(clientHandler);
                log.info("Client connected {}", clientSocket);
            }
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Метод отправки сообщения всем клиентам
     * @param message
     */
    void broadcastMessage(String message) {
        if (!message.contains("присоединился к чату")){
            messageRepositoryService.add(message);
        }
        for (ClientHandler client : clients) {
            if (client.getLogin() != null) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Метод проверки существования ника в чате
     * @param login
     * @return
     */
    boolean loginIsPresent(String login) {
        for (ClientHandler client : clients) {
            if (client.getLogin() != null && client.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

    void removeClient(ClientHandler client) {
        clients.remove(client);
        broadcastMessage(String.format("%s покинул чат", client.getLogin()));
        log.info("Client logout {}", client.getLogin());
    }
}
