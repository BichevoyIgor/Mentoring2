package ru.bichevoy;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class MainTask {

    /**
     * Напишите программу, которая запрашивает у пользователя число и выводит его квадрат.
     * Обработайте случай, когда введено не число.
     */
    public void task1() {

        try (Scanner scanner = new Scanner(System.in)) {
            int num = scanner.nextInt();
            System.out.println(num * num);
        } catch (InputMismatchException e) {
            System.out.println("Введено не число");
            e.printStackTrace();
        }
    }

    /**
     * Создайте метод, который делит два числа и выбрасывает `ArithmeticException`, если второй аргумент равен нулю
     */
    public void task2(int num1, int num2) {

        try {
            System.out.println(num1 / num2);
        } catch (ArithmeticException e) {
            System.out.println("Делитель равен 0");
        }
    }

    /**
     * Реализуйте метод, работающий с массивом, и обработайте исключение `ArrayIndexOutOfBoundsException`.
     */
    public void task3(Object[] array) {
        try {
            Object o = array[array.length];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException");
        }
    }

    /**
     * Напишите код, в котором блок `finally` выводит сообщение независимо от того, было исключение или нет.
     */
    public void task4() {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            System.out.println("RuntimeException");
        } finally {
            System.out.println("Завершение работы метода");
        }
    }

    /**
     * Создайте собственное исключение, наследуемое от `Exception`, и выведите его с помощью `throw`.
     */
    private class MyException extends Exception {
        public MyException() {
        }

        public MyException(String message) {
            super(message);
        }

        public MyException(String message, Throwable cause) {
            super(message, cause);
        }

        public MyException(Throwable cause) {
            super(cause);
        }

        public MyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    public void task5() {
        try {
            throw new MyException("исключение MyException");
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Создайте `ArrayList` и добавьте в него пять имен. Выведите их в консоль.
     */
    public void task6() {
        List<String> list = new ArrayList<>();
        list.add("Ivan");
        list.add("Vladimor");
        list.add("Dmitry");
        list.add("Daniil");
        list.add("Andrey");
        System.out.println(list);
    }

    /**
     * Напишите программу для удаления третьего элемента из `LinkedList`
     */
    public void task7(List<?> list) {
        list.remove(2);
        System.out.println(list);
    }

    /**
     * Создайте `HashSet`, добавьте несколько элементов и проверьте, существует ли определенный элемент в коллекции.
     */
    public void task8() {
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Ivan");
        hashSet.add("Vladimor");
        hashSet.add("Dmitry");
        hashSet.add("Daniil");
        hashSet.add("Andrey");
        System.out.println(hashSet.contains("Ivn"));
    }

    /**
     * Создайте `HashMap` и добавьте в него пары ключ-значение (например, имена студентов и их оценки).
     */
    public void task9() {
        Map<String, List<Integer>> hashMap = new HashMap<>();
        hashMap.put("Ivan Ivanov", new ArrayList<>(asList(5, 3, 3, 3)));
        hashMap.put("Ivan Petrov", new ArrayList<>(asList(3, 3, 3, 3)));
    }

    /**
     * Напишите программу, которая сортирует список имен с помощью `TreeSet`.
     */
    public List<String> task10(List<String> names) {
        TreeSet<String> strings = new TreeSet<>(names);
        return new ArrayList<>(strings);
    }

    /**
     * Создайте обобщенный класс с методом, который возвращает переданное значение.
     */
    class Task11<T> {
        public T returner(T element) {
            return element;
        }
    }

    /**
     * Реализуйте обобщенный метод, который получает список элементов и возвращает его размер
     */
    public int task12(List<?> list) {
        return list.size();
    }

    /**
     * Создайте класс с обобщенным конструктором, который принимает и выводит параметр любого типа.
     */
    class Task13<T> {
        public Task13(T param) {
            System.out.println(param);
        }
    }

    /**
     * Напишите обобщенный метод, сравнивающий два элемента и возвращающий больший из них.
     */
    public <T> T task14(T param1, T param2) {
        //а по каким параметрам сравнивать неизвестные типы? я вижу только вариант по значению хэша
        return param1.hashCode() > param2.hashCode() ? param1 : param2;
    }

    /**
     * Создайте обобщенный класс с двумя параметрами типа и методом, который выводит их значения.
     */
    class Task15<T1, T2> {
        public void print(T1 param1, T2 param2) {
            System.out.println(param1);
            System.out.println(param2);
        }
    }

    /**
     * Реализуйте обобщенный интерфейс с методом для получения элемента. Создайте два класса, которые реализуют этот интерфейс.
     */
    interface Task16<T> {
        T getElement(T element);
    }

    class Task16Impl<T> implements Task16<T> {

        @Override
        public T getElement(T element) {
            return element;
        }
    }

    class Task16Impl2 implements Task16<String> {

        @Override
        public String getElement(String element) {
            return element;
        }
    }

    /**
     * Создайте `Optional` объект из строки и выведите ее длину, если она существует.
     */
    public void task17(String text) {
        Optional<String> opt = Optional.ofNullable(text);
        opt.ifPresent(s -> System.out.println(s.length()));
    }

    /**
     * Напишите программу, которая создает пустой `Optional` и заменяет его значением по умолчанию.
     */
    public void task18() {
        Optional<String> opt = Optional.empty();
        opt.orElse("default");
    }

    /**
     * Используйте `Optional`, чтобы обработать возможное значение `null` из метода, возвращающего строку.
     */
    public void task19() {
        String someMethod = "null";
        Optional<String> opt = Optional.ofNullable(someMethod);
        opt.ifPresentOrElse(str -> System.out.println("Значение присутствует: " + str),
                () -> System.out.println("Значение отсутствует"));
    }

    /**
     * Реализуйте метод, который возвращает `Optional` и преобразует его значение в верхний регистр, если оно существует.
     */
    public Optional<String> task20(String text) {
        return Optional.ofNullable(text)
                .map(String::toUpperCase);
    }

    /**
     * Используя `Optional`, реализуйте метод, который бросает исключение, если значение отсутствует.
     */
    public <T> void task21(T param) throws Exception {
        Optional<T> opt = Optional.of(param); // или так

        Optional<T> optionalT = Optional.ofNullable(param);
        optionalT.orElseThrow(Exception::new); // или так
    }

    /**
     * Создайте список чисел и используйте стрим, чтобы вывести все четные числа.
     */
    public void task22() {
        Stream.of(12, 7, 5, 77, 23, 33, 69, 20)
                .filter(x -> x % 2 == 0)
                .forEach(System.out::println);
    }

    /**
     * Используйте Stream API для преобразования списка строк в список их длин.
     */
    public List<Integer> task23(List<String> strings) {
        List<Integer> collect = strings.stream()
                .map(s -> s.length())
                .collect(Collectors.toList());

        return collect;
    }

    /**
     * Реализуйте программу, которая фильтрует список имен и выводит те, которые начинаются с определенной буквы.
     */
    public void task24(List<String> names, char startChar) {
        names.stream()
                .filter(x -> x.charAt(0) == startChar)
                .forEach(System.out::println);
    }

    /**
     * Используйте Stream API для нахождения первого элемента в списке, длина которого больше 3 символов.
     */
    public Optional<String> task25(List<String> strings) {

        return strings.stream()
                .filter(s -> s.length() > 3)
                .findFirst();

    }

    /**
     * Напишите программу, которая суммирует все числа в списке, используя стримы.
     */
    public int task26(List<Integer> list) {
        return list.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * Реализуйте программу, которая преобразует список чисел в список их квадратов с помощью Stream API.
     */
    public List<Integer> task27(List<Integer> list) {

        return list.stream()
                .map(x -> x * x)
                .collect(Collectors.toList());
    }

    /**
     * Используйте стрим для сортировки списка строк в алфавитном порядке.
     */
    public List<String> task28(List<String> list) {

        return list.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Напишите программу, которая собирает все уникальные элементы списка в сет, используя Stream API.
     */
    public <T> Set<T> task29(List<T> list) {
        return list.stream()
                .collect(Collectors.toSet());
    }

    /**
     * Используйте `parallelStream` для нахождения среднего значения чисел в большом списке и выведите результат.
     */
    public void task30(List<Integer> list) {
        OptionalDouble average = list.parallelStream()
                .mapToInt(Integer::intValue)
                .average();
        System.out.println(average.getAsDouble());

    }

    /**
     * Создайте программу, использующую Stream для подсчета количества элементов в списке,
     * удовлетворяющих определенному условию, например, больше 10.
     */
    public long task31(List<Integer> list) {
        return list.stream()
                .filter(x -> x > 10)
                .count();
    }

    /**
     * Используйте стрим для группировки списка строк по их первой букве и выведите результат.
     */
    public void task32(List<String> list) {
        Map<Character, List<String>> collect = list.stream()
                .collect(Collectors.groupingBy(word -> word.charAt(0)));
        System.out.println(collect);
    }

    /**
     * Напишите стрим, который объединяет несколько списков в один, удаляет дубликаты и выводит уникальные элементы.
     */
    public void task33(List<?> list1, List<?> list2) {
        Stream<?> stream = list1.stream();
        Stream<?> stream2 = list2.stream();
        Stream.concat(stream, stream2)
                .distinct()
                .forEach(System.out::println);
    }

    /**
     * Используйте Stream API с reduce для нахождения произведения всех чисел в списке
     */
    public Optional<Integer> task34(List<Integer> list) {
        return list.stream()
                .reduce((a, b) -> a * b);
    }

    /**
     * Создайте программу, которая использует flatMap для работы со списком списков чисел, чтобы вывести все числа в одном потоке.
     */
    public void task35(List<List<Integer>> lists){
        lists.stream()
                .flatMap(list -> list.stream())
                .forEach(System.out::println);
    }
}
