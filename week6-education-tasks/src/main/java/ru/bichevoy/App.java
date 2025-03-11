package ru.bichevoy;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {

    }
}

/**
 * Простой эхо-сервер: Напишите сервер, работающий на сокетах, который возвращает клиенту любое полученное сообщение без изменений.
 */
class EchoServer {
    void start() {
        try (ServerSocket serverSocket = new ServerSocket(9999);
             Socket client = serverSocket.accept();
             BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            String message = null;
            while (!"EXIT".equals(message)) {
                message = reader.readLine();
                writer.write(message + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Server shutdown");

    }
}

/**
 * Клиент для эхо-сервера: Создайте клиентскую программу, которая отправляет строку на эхо-сервер и получает ответ.
 */
class EchoClient {
    void start() {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 9999);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             Scanner scanner = new Scanner(System.in)) {
            String message = null;
            while (!"EXIT".equals(message)) {
                message = scanner.nextLine();
                writer.write(message + "\n");
                writer.flush();
                System.out.println(reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Client shutdown");
    }
}

/**
 * UDP-сервер: Реализуйте сервер, принимающий сообщения от клиентов через DatagramSocket и выводящий их в консоль.
 */
class UdpServer {
    void start() {
        try (DatagramSocket datagramSocket = new DatagramSocket(9999)) {
            while (true) {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                datagramSocket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * UDP-клиент: Напишите клиент, который отправляет сообщение на UDP-сервер.
 */
class UdpClient {
    void start() {
        try (DatagramSocket datagramSocket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)
        ) {
            while (true) {
                byte[] message = scanner.nextLine().getBytes();
                DatagramPacket packet = new DatagramPacket(message, message.length, InetAddress.getLocalHost(), 9999);
                datagramSocket.send(packet);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Получение IP-адресов: Используя InetAddress, напишите программу, которая выводит IP-адреса для заданного доменного имени.
 */

class Task5 {
    public static List<String> getIP(String url) {
        List<String> result = new ArrayList<>();
        try {
            InetAddress[] allByName = InetAddress.getAllByName(url);
            for (InetAddress inetAddress : allByName) {
                result.add(inetAddress.getHostAddress());
            }
            return result;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Чтение файла: Напишите программу, которая считывает содержимое текстового файла и выводит его на экран.
 */
class Task6 {
    static void readFile(String path) {
        File file = new File(path);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Запись в файл: Создайте программу для записи введенной пользователем строки в текстовый файл.
 */
class Task7 {
    static void writeFile(String path) {
        try (Scanner scanner = new Scanner(System.in);
             FileWriter writer = new FileWriter(path, true)) {
            while (true) {
                writer.append(scanner.nextLine() + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Копирование файла: Реализуйте приложение, которое копирует содержимое одного файла в другой, используя InputStream и OutputStream.
 */
class Task8 {
    static void fileCopy(String producer, String consumer) {
        try (InputStream inputStream = new FileInputStream(producer);
             OutputStream outputStream = new FileOutputStream(consumer)) {
            byte[] buf = new byte[1024];
            while (inputStream.available() > 0) {
                int readBytes = inputStream.read(buf);
                outputStream.write(buf, 0, readBytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Построчное чтение: Напишите программу, которая считывает файл построчно и выводит каждую строку в консоль.
 */
class Task9 {
    static void reader(String path) {
        File file = new File(path);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                System.out.println(reader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Буферизованное чтение и запись: Используя BufferedReader и BufferedWriter, создайте программу, копирующую текст из одного файла в другой.
 */
class Task10 {
    static void copyFile(String producer, String consumer) {
        try (BufferedReader reader = new BufferedReader(new FileReader(producer));
             BufferedWriter writer = new BufferedWriter(new FileWriter(consumer))) {
            while (reader.ready()) {
                writer.write(reader.readLine() + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Сериализация объекта: Напишите программу, которая сериализует объект класса (например, User) в файл.
 */
class Task11 {
    static void serial(User user, String path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


/**
 * Десериализация объекта: Создайте приложение, которое считывает объект из файла и выводит его атрибуты на экран.
 */
class Task12 {
    static User deSerial(String path) {
        try (FileInputStream inputStream = new FileInputStream(path);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            return (User) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Коллекция сериализуемых объектов: Реализуйте приложение, которое сериализует и десериализует коллекцию объектов (например, ArrayList<User>)
 */
class Task15 {
    static void serial(List<?> list, String path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ) {
            objectOutputStream.writeObject(list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static <T> List<T> deSerializ(String path) {
        try (FileInputStream inputStream = new FileInputStream(path);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            return (List<T>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

class User implements Serializable {

    private static final long serialVersionUID = -2072399294872389398L;
    final int age;
    final String name;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}

