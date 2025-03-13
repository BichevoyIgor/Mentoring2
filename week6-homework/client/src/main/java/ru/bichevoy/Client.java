package ru.bichevoy;


import lombok.extern.slf4j.Slf4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

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
            System.out.println("Команды:" +
                    "\nEXIT - выход" +
                    "\nLOGIN= - задать логин пользователя" +
                    "\nCATFACT - получить факт о котике" +
                    "\n");
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
     * @param in
     * @param out
     * @param scanner
     * @throws IOException
     */
    private void login(DataInputStream in, DataOutputStream out, Scanner scanner) throws IOException {
        System.out.println("Укажите ваш логин: ");
        while (login == null) {
            String login = scanner.nextLine();
            out.writeUTF("LOGIN=" + login);
            String msgFromServer = in.readUTF();
            if (msgFromServer.startsWith("LOGIN=")) {
                this.login = msgFromServer.split("=")[1];
                System.out.println("Ваш логин установлен: " + this.login);
            } else {
                System.out.println(msgFromServer);
            }
        }
    }

    /**
     * Обработка команд с клавиатуры
     * @param out
     * @param scanner
     * @throws IOException
     */
    private void keyboardListener(DataOutputStream out, Scanner scanner) throws IOException {
        while (true) {
            String text = scanner.nextLine();
            out.writeUTF(text);
            out.flush();
            if (text.equals("EXIT")) {
                log.info("EXIT");
                break;
            }
        }
    }

    /**
     * Обработка сообщений поступающих от сервера в отдельном daemon потоке
     * @param in
     */
    private void socketListener(DataInputStream in) {
        Thread readThread = new Thread(() -> {
            while (true) {
                try {
                    String msgFromServer = in.readUTF();
                    if (msgFromServer.startsWith("LOGIN=")) {
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
