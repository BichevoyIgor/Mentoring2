package ru.bichevoy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {

    }
}

/**
 * Использование AtomicInteger: Создайте счетчик, который безопасно увеличивается несколькими потоками с помощью AtomicInteger.
 */
class Task1 {
    private final AtomicInteger count = new AtomicInteger(0);

    void intcrement() {
        count.incrementAndGet();
    }

    int getCount() {
        return count.get();
    }
}

/**
 * Простой пример с ReentrantLock: Напишите программу, использующую ReentrantLock для синхронизации доступа к общему ресурсу.
 */
class Task2 {
    private final Lock lock = new ReentrantLock();
    private int count = 0;

    void intcrement() {
        lock.lock();
        try {
            count += 1;
        } finally {
            lock.unlock();
        }
    }

    public int getCount() {
        return count;
    }
}

/**
 * Реализация задачи с Callable: Создайте Callable, который возвращает сумму от 1 до 100, и выполните его с помощью ExecutorService.
 */
class Task3 {
    int demo() {
        try (ExecutorService service = Executors.newSingleThreadExecutor()) {
            Future<Integer> sumFromCall = service.submit(() -> {
                int sum = 0;
                for (int i = 1; i <= 100; i++) {
                    sum += i;
                }
                return sum;
            });
            return sumFromCall.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Использование ThreadPool: Создайте фиксированный пул потоков и выполните в нем 5 задач, каждая из которых выводит свое имя.
 */
class Task4 {
    public void demo() {
        try (ExecutorService service = Executors.newFixedThreadPool(3)) {
            Runnable runnable = () -> System.out.printf("Имя потока: %s\n", Thread.currentThread().getName());
            for (int i = 0; i < 5; i++) {
                service.execute(runnable);
            }
        }
    }
}

/**
 * Задача с Future: Напишите программу, которая запускает задачу с отложенной задержкой и получает результат с помощью Future.
 */
class Task5 {

    String demo() {
        try (ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor()) {
            ScheduledFuture<String> schedule = service.schedule(() -> "Hello", 5, TimeUnit.SECONDS);
            return schedule.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Простой пример с CompletableFuture.runAsync(): Напишите код, который выполняет задачу асинхронно и выводит сообщение после ее завершения.
 */
class Task6 {
    void demo() {
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        completableFuture.thenAccept(_ -> System.out.println("Task in CompletableFuture was completed"));
    }
}

/**
 * Синхронизация с CountDownLatch: Реализуйте программу, где 3 потока ждут завершения основного процесса с использованием CountDownLatch.
 */
class Task7 {
    void demo() {
        int countForLatch = 3;
        CountDownLatch latch = new CountDownLatch(countForLatch);
        Runnable runnable = () -> {
            try {
                latch.await();
                System.out.printf("%s - я дождался\n", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        Thread t3 = new Thread(runnable);

        t1.start();
        t2.start();
        t3.start();
        for (int i = 0; i < countForLatch; i++) {
            try {
                System.out.println(latch.getCount());
                Thread.sleep(500);
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

/**
 * Пример с CyclicBarrier: Создайте программу, где несколько потоков одновременно достигают барьера и продолжают выполнение.
 */
class Task8 {
    void demo() {
        CyclicBarrier barrier = new CyclicBarrier(3);
        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                if (i == 30) {
                    try {
                        System.out.printf("%s start await\n", Thread.currentThread().getName());
                        barrier.await();
                        System.out.printf("%s start after await\n", Thread.currentThread().getName());
                    } catch (InterruptedException | BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                if (i == 2) {
                    try {
                        System.out.printf("%s start await\n", Thread.currentThread().getName());
                        barrier.await();
                        System.out.printf("%s start after await\n", Thread.currentThread().getName());
                    } catch (InterruptedException | BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                if (i == 14) {
                    try {
                        System.out.printf("%s start await\n", Thread.currentThread().getName());
                        barrier.await();
                        System.out.printf("%s start after await\n", Thread.currentThread().getName());
                    } catch (InterruptedException | BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}

/**
 * Использование Exchanger: Напишите пары потоков, которые обмениваются данными с использованием Exchanger.
 */
class Task9 {
    void demo() {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> {
            try {
                String exchange = exchanger.exchange("Hello from " + Thread.currentThread().getName());
                System.out.printf("%s получил фразу: %s\n", Thread.currentThread().getName(), exchange);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(500);
                String exchange = exchanger.exchange("Hello from " + Thread.currentThread().getName());
                System.out.printf("%s получил фразу: %s\n", Thread.currentThread().getName(), exchange);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}

/**
 * Использование ForkJoinPool для параллельной задачи: Создайте задачу, которая разделяет работу на части с помощью ForkJoinPool и возвращает объединенный результат.
 */
class Task11 {
    void demoFindMax(int[] ar) {
        ForkJoinPool pool = new ForkJoinPool();
        RecursiveTask<Integer> subTask11 = new SubTask11(0, ar.length, ar);
        Integer invoke = pool.invoke(subTask11);
        System.out.println(invoke);
        pool.close();
    }
}

class SubTask11 extends RecursiveTask<Integer> {
    int from;
    int to;
    private int[] ar;

    public SubTask11(int from, int to, int[] ar) {
        this.from = from;
        this.to = to;
        this.ar = ar;
    }

    @Override
    protected Integer compute() {
        if (to - from < 10) {
            int max = 0;
            for (int i = from; i < to; i++) {
                max = Math.max(max, ar[i]);
            }
            return max;
        } else {
            RecursiveTask<Integer> s = new SubTask11(0, (from + to) / 2, ar);
            RecursiveTask<Integer> s2 = new SubTask11((from + to) / 2, to, ar);
            s.fork();
            s2.fork();
            return Math.max(s.join(), s2.join());
        }
    }
}

/**
 * Композиции с CompletableFuture: Напишите программу, в которой задачи выполняются последовательно, а результат первой передается второй.
 */
class Task12 {
    void demo() {
        CompletableFuture<String> res1 = CompletableFuture.supplyAsync(() -> "Hello");
        CompletableFuture<String> res2 = res1.thenApply(s -> s.concat(" "));
        CompletableFuture<String> stringCompletableFuture = res2.thenApply(s -> s.concat("world"));
        try {
            System.out.println(stringCompletableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Использование ConcurrentHashMap: Разработайте программу, безопасно добавляющую и извлекающую элементы из ConcurrentHashMap.
 */
class Task13 {
    private ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    public int getValByKey(String key) {
        return map.get(key);
    }

    public void put(String key, int val) {
        map.put(key, val);
    }
}

/**
 * Пример использования ThreadLocal: Создайте программу, использующую ThreadLocal для хранения уникальных данных потока.
 */
class Task14 implements Runnable {
    ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Override
    public void run() {
        threadLocal.set(Thread.currentThread().getName());
        System.out.println(threadLocal.get());
    }
}

/**
 * Реализация Производитель-Потребитель с блокирующей очередью: Напишите класс, использующий LinkedBlockingQueue как буфер между производителем и потребителем.
 */
class Task15 {
    LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public void put(String text) {
        try {
            queue.put(text);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getElement() {
        return queue.poll();
    }

    public int sizeQueue() {
        return queue.size();
    }
}

/**
 * Оптимизация пулов потоков: Напишите программу для оценки производительности приложения при разных размерах пула потоков.
 */
class Task16 {
    void demo() {
        for (int i = 1; i <= 10; i++) {
            long startTime = System.nanoTime();
            runTask(i);
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            System.out.println("Размер пула: " + i + ", Время выполнения (мс): " + duration / 1_000_000);
        }
    }

    void runTask(int poolSize) {
        ExecutorService service = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < 10_000; i++) {
            service.execute(() -> Math.pow(999, 12));
        }
        service.shutdown();
        while (!service.isTerminated()) {
        }
    }
}

/**
 * Параллельная обработка данных: Переработайте список строк в uppercase параллельно с использованием ForkJoinPool.
 */
class Task17 {
    void demo(List<String> strings) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask task = new ChangeList(strings, 0, strings.size());
        forkJoinPool.invoke(task);
        forkJoinPool.close();
    }
}

class ChangeList extends RecursiveAction {
    private List<String> list;
    private int from;
    private int to;
    private int treshhold;

    public ChangeList(List<String> list, int from, int to) {
        this.list = list;
        this.from = from;
        this.to = to;
        this.treshhold = 5000;
    }

    @Override
    protected void compute() {
        if ((to - from) < treshhold) {
            for (int i = from; i < to; i++) {
                list.set(i, list.get(i).toUpperCase());
            }
            return;
        }
        int mid = from + (to - from) / 2;
        ChangeList task1 = new ChangeList(list, from, mid);
        ChangeList task2 = new ChangeList(list, mid, to);
        invokeAll(task1, task2);
    }
}

/**
 * Многопоточное сетевое приложение: Создайте сервер, обрабатывающий клиентские подключения в пуле потоков.
 */
class Task18 {
    private final static ExecutorService service = Executors.newFixedThreadPool(100);

    void demo(Runnable task) {
        service.execute(task);
    }

    void shutdown() {
        service.shutdown();
    }
}

/**
 * Синхронизация доступа к флагу с использованием atomic переменных: Напишите программу изменения состояния флага через атомарные операции.
 */
class Task19 {
    private AtomicBoolean flag = new AtomicBoolean();

    public void trueFlag() {
        flag.set(true);
    }

    public void falseFlag() {
        flag.set(false);
    }

    public boolean getValue() {
        return flag.get();
    }
}

/**
 * Создание асинхронных воркфлоу с CompletableFuture: Используйте thenCompose для построения цепочки асинхронных операций с обработкой ошибок.
 */
class Task20 {
    void demo() {
        CompletableFuture.supplyAsync(() -> "Hello").thenCompose(res -> CompletableFuture.supplyAsync(() -> {
            if (res == null) {
                throw new RuntimeException("Пришло не слово");
            }
            return res + " World";
        })).handle((res, er) -> {
            if (er != null) {
                return er.getMessage();
            }
            return res + "!";
        });
    }
}

/**
 * Использование семафора для ограничения количества потоков, входящих в критическую секцию: Реализуйте контроль входа пользователей в систему.
 */
class Task22 {
    private Semaphore semaphore = new Semaphore(2);

    void demo() {
        try {
            semaphore.acquire();
            System.out.println("critical block");
            semaphore.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Пример реализации рекурсии в ForkJoinPool: Напишите программу для вычисления суммы в массиве рекурсивно.
 */
class Task23 {
    void demo() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int[] ar = new int[100_000];
        for (int i = 0; i < ar.length; i++) {
            ar[i] = i;
        }
        RecursiveTask<Long> task = new Subtask23(ar, 0, ar.length);
        System.out.println(forkJoinPool.invoke(task));
        forkJoinPool.close();
    }
}

class Subtask23 extends RecursiveTask<Long> {
    int[] array;
    int first;
    int last;

    public Subtask23(int[] array, int first, int last) {
        this.array = array;
        this.first = first;
        this.last = last;
    }

    @Override
    protected Long compute() {
        if (last - first < 10000) {
            long sum = 0;
            for (int i = first; i < last; i++) {
                sum += array[i];
            }
            return sum;
        }
        int mid = first + (last - first) / 2;
        RecursiveTask<Long> task1 = new Subtask23(array, first, mid);
        RecursiveTask<Long> task2 = new Subtask23(array, mid, last);
        invokeAll(task1, task2);
        return task1.join() + task2.join();
    }
}

/**
 * Управление состоянием через CompletableFuture: Создайте несколько задач, обрабатывающих исключения при их возникновении.
 */
class Task24 {
    void demo() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> 5).thenApply(res -> {
            if ((Object) res instanceof String) {
                return res + " world";
            } else {
                throw new RuntimeException("поступило не число");
            }
        }).exceptionally(ex -> "Возникло исключение " + ex.getMessage());
        try {
            System.out.println(completableFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

/**
 * Многопоточная обработка файлов: Реализуйте параллельную обработку файлов для анализа их содержимого.
 */
class Task25 {
    void demo() throws IOException, ExecutionException, InterruptedException {
        Path path1 = Path.of(String.format("D:%sdata.txt", File.separator));
        Path path2 = Path.of(String.format("D:%sdata2.txt", File.separator));
        CompletableFuture<String> file1Lines = CompletableFuture.supplyAsync(() -> {
            try {
                return Files.readAllLines(path1).stream().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        String file2Lines = Files.readAllLines(path2).stream().collect(Collectors.joining("\n"));
        System.out.println(file1Lines.get().equals(file2Lines));
    }
}

/**
 * Группировка потоков с использованием CyclicBarrier и ExecutorService: Напишите работу, где потоки необходимо синхронизировать на разных этапах выполнения.
 */
class Task26 {
    void demo() throws BrokenBarrierException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(5);
        CyclicBarrier barrier = new CyclicBarrier(5);
        for (int i = 0; i < 4; i++) {
            final int x = i;
            service.execute(() -> {
                try {
                    Thread.sleep(x * 200);
                    System.out.println(Thread.currentThread().getName() + " готов");
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        service.shutdown();
        System.out.println(Thread.currentThread().getName() + " жду остальных");
        barrier.await();
        System.out.println("Поехали");
    }
}

/**
 * Потокобезопасный кэш с использованием ConcurrentHashMap: Разработка кэша, поддерживающего многопоточный доступ.
 */
class Task27 {
    private ConcurrentHashMap<String, Integer> cash = new ConcurrentHashMap<>();

    public Integer getVal(String key) {
        return cash.get(key);
    }

    public void setVal(String key, Integer value) {
        cash.put(key, value);
    }

}

/**
 * Асинхронная обработка веб-запросов: Разработка обработчика запросов интернет-магазина, работающего в асинхронном стиле.
 */
class Task28 {
    private static ConcurrentHashMap<Integer, String> repository = new ConcurrentHashMap<>();
    private static Lock lock = new ReentrantLock();

    String getInfoById(int id) throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> repository.get(id));
        return completableFuture.get();
    }

    synchronized int put(String value) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            lock.lock();
            int id = repository.size();
            repository.put(id, value);
            lock.unlock();
            return id;
        });

        return completableFuture.get();
    }
}

/**
 * Эксперименты с ThreadLocal для хранения данных запроса: Сделайте приложение, использующее ThreadLocal для операций временного хранения.
 */
class Task30 {
    ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    void demo() {
        threadLocal.set(new Random().nextInt(100));
    }

    public int getThreadLocal() {
        return threadLocal.get();
    }
}