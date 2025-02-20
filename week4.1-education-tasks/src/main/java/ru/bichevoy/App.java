package ru.bichevoy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

public class App {
    static final List<String> list = new ArrayList<>();
    static String s = "tik";
    static int general;

    public static void main(String[] args) {
        task22();
    }

    /**
     * Использование `wait()` и `notify()`: Реализуйте простую программу, использующую `wait()` и `notify()` для
     * синхронизации выполнения двух потоков.
     */
    static void task6() {
        Object mon = new Object();

        Thread t1 = new Thread(() -> {
            synchronized (mon) {
                while (true) {
                    if (s.equals("tik")) {
                        s = "tak";
                        System.out.println(s);
                        mon.notify();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        mon.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (mon) {
                while (true) {
                    if (s.equals("tak")) {
                        s = "tik";
                        System.out.println(s);
                        mon.notify();
                    }
                    try {
                        mon.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        t1.start();
        t2.start();
    }

    /**
     * Симуляция `interrupt()`**: Напишите поток, который выполняет долгую задачу, и прерывайте его через `interrupt()`.
     * Почему ничего не вышло?
     */
    static void task7() {
        Thread anotherThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Hello");
            }
        });
        anotherThread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        anotherThread.interrupt();
    }

    /**
     * Демонстрация race condition: Напишите программу, которая приводит к состоянию гонки, обновляя общую переменную
     * из двух потоков.
     */
    static void task12() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                general++;
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                general++;
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            System.out.println(general);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Использование `synchronized` для предотвращения `race condition`**: Примените `synchronized` к предыдущему
     * примеру, чтобы устранить состояние гонки.
     */
    static void task13() {
        Object mon = new Object();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                synchronized (mon) {
                    general++;
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                synchronized (mon) {
                    general++;
                }
            }
        });

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            System.out.println(general);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Реализация шаблона "Производитель-Потребитель"**: Напишите программу, демонстрирующую взаимодействие производителя
     * и потребителя через общий буфер.
     */
    static void task20() {
        Object mon = new Object();
        Thread producer = new Thread(() -> {

            while (true) {
                synchronized (mon) {
                    if (list.size() < 10) {
                        list.add("Хлеб");
                        System.out.println("Положил на полку хлеб, хлеба: " + list.size());
                        mon.notify();
                    } else {
                        try {
                            System.out.println("Хлеба много, перекур");
                            mon.notify();
                            mon.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });

        Thread consumer = new Thread(() -> {

            while (true) {
                synchronized (mon) {
                    if (list.isEmpty()) {
                        try {
                            System.out.println("Хлеба нет, я подожду");
                            mon.notify();
                            mon.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        list.remove(0);
                        System.out.println("Хлеб купили, хлеба: " + list.size());
                        mon.notify();
                    }
                }
            }
        });

        producer.start();
        consumer.start();
    }

    /**
     * Реализация таймера с использованием `TimerTask` и потоков: Напишите программу, запускающую периодическую задачу
     * с использованием потоков.
     */
    static void task22() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (int i = 1; i <= 10; i++) {
                    System.out.println(i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        while (true) {
            Thread thread = new Thread(task);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }

}

/**
 * Создайте класс, управляемый потоками, для отслеживания параметров приложения: Используйте `synchronized` для безопасности доступа к данным.
 */
class Task23 {
    int loadCPU;
    int loadMemory;
    int loadEthernet;

    public synchronized int getLoadCPU() {
        getLoadCPU();
        return loadCPU;
    }

    public synchronized int getLoadMemory() {
        getLoadMemory();
        return loadMemory;
    }

    public synchronized int getLoadEthernet() {
        getLoadEthernet();
        return loadEthernet;
    }

    private void setLoadCPU() {
        loadCPU = new Random().nextInt(100);
    }

    private void setLoadMemory() {
        loadMemory = new Random().nextInt(100);
    }

    private void setLoadEthernet() {
        loadEthernet = new Random().nextInt(100);
    }
}
