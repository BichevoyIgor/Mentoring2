package ru.bichevoy;


import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Slf4j
public class Client {
    private final String HOST;
    private final int PORT;
    private String login;

    public Client(String HOST, int PORT) {
        this.HOST = HOST;
        this.PORT = PORT;
        start();
    }

    public void start() {
        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)
        ) {
            log.info("Client started {}", socket);
            System.out.printf("Команды:" +
                    "\n%s - выход" +
                    "\n%s - задать логин пользователя" +
                    "\n%s - отправить сообщение в чат комнаты" +
                    "\n", Commands.EXIT.getVal(), Commands.LOGIN.getVal(), Commands.CHAT.getVal());
            login(in, out, scanner);
            socketListener(in);
            keyboardListener(out, scanner);
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод инициализации логина для сессии
     *
     */
    private void login(DataInputStream in, DataOutputStream out, Scanner scanner) throws IOException {
        System.out.println("Укажите ваш логин: ");
        while (login == null) {
            String login = scanner.nextLine();
            out.writeUTF(Commands.LOGIN.getVal() + login);
            String msgFromServer = in.readUTF();
            if (msgFromServer.startsWith(Commands.LOGIN.getVal())) {
                this.login = msgFromServer.split("=")[1];
                System.out.println("Ваш логин установлен: " + this.login);
            } else {
                System.out.println(msgFromServer);
            }
        }
    }

    /**
     * Обработка команд с клавиатуры
     *
     */
    private void keyboardListener(DataOutputStream out, Scanner scanner) throws IOException {
        List<String> commandsListWithoutGame = Arrays.stream(Commands.values())
                .filter(c -> !c.equals(Commands.GAME))
                .map(Commands::getVal)
                .collect(Collectors.toList());
        while (true) {
            String text = scanner.nextLine();
            if (text.equals(Commands.EXIT.getVal())) {
                out.writeUTF(text);
                out.flush();
                log.info(Commands.EXIT.getVal());
                break;
            }
            String[] enteredMsg = text.split("=", 2);
            if (enteredMsg.length != 2) {
                if (!commandsListWithoutGame.contains(enteredMsg[0] + "=")) {
                    text = (Commands.GAME.getVal() + text).toUpperCase();
                }
            }
            out.writeUTF(text);
            out.flush();
        }
    }

    /**
     * Обработка сообщений поступающих от сервера в отдельном daemon потоке
     *
     */
    private void socketListener(DataInputStream in) {
        Thread readThread = new Thread(() -> {
            while (true) {
                try {
                    String msgFromServer = in.readUTF();
                    if (msgFromServer.startsWith(Commands.LOGIN.getVal())) {
                        String newLogin = msgFromServer.split("=")[1];
                        log.info("{} изменил логин на {}", this.login, newLogin);
                        this.login = newLogin;
                        System.out.println("Ваш логин установлен: " + this.login);
                        continue;
                    }
                    System.out.println(msgFromServer);
                } catch (IOException e) {
                    log.error(Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }
            }
        });
        readThread.setDaemon(true);
        readThread.start();
    }
}