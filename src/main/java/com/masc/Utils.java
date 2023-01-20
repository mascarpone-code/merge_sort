package com.masc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static com.masc.Constants.*;

public class Utils {
    /**
     * Метод создаёт по одному экземпляру классов {@link Task} и
     * {@link java.io.BufferedReader} для каждого исходного файла.
     *
     * @param filenames список имён исходных файлов.
     * @return список задач {@link Task}.
     */
    public static List<Task> createTasks(List<String> filenames) {
        var tasks = new ArrayList<Task>();

        for (var filename : filenames) {
            var path = Path.of(filename);

            if (Files.exists(path)) {
                try {
                    var reader = Files.newBufferedReader(path);

                    if (reader.ready()) {
                        tasks.add(new Task(reader));
                    }
                } catch (IOException e) {
                    System.out.println(UNABLE_OPEN_FILE + filename);
                }
            } else {
                System.out.println(FILE + filename + NOT_FOUND);
            }
        }

        return tasks;
    }

    /**
     * Создание списка чисел для сортировки.
     * Читаются первые строки из каждого файла.
     *
     * @param tasks список задач {@link Task}.
     * @return список чисел.
     */
    public static List<Integer> getIntegers(List<Task> tasks) {
        var integers = new ArrayList<Integer>();

        for (var task : tasks) {
            String nextLine;

            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                nextLine = task.getLine();

                if (isNumber(nextLine)) {
                    integers.add(Integer.parseInt(nextLine));
                    break;
                } else {
                    task.resumeReading();
                }
            }
        }

        return integers;
    }

    /**
     * Создание списка строк для сортировки.
     * Читаются первые строки из каждого файла.
     *
     * @param tasks список задач {@link Task}.
     * @return список строк.
     */
    public static List<String> getStrings(List<Task> tasks) {
        var lines = new ArrayList<String>();

        for (var task : tasks) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            lines.add(task.getLine());
        }

        return lines;
    }

    /**
     * Получение следующего числа из файла и вставка его в список для сравнения.
     * Если получено значение {@code null}, это означает, что в файле не осталось строк для чтения и он закрыт.
     * В таком случае задача {@link Task}, связанная с данным файлом, удаляется из списка задач.
     *
     * @param tasks            список активных задач {@link Task}.
     * @param numbers          список чисел для добавления следующего числа.
     * @param lineToWriteIndex индекс элемента в списке, в который будет вставлено число.
     *                         Также индекс задачи {@link Task} в списке задач.
     */
    public static void getNextNumber(List<Task> tasks, List<Integer> numbers, int lineToWriteIndex) {
        String nextLine;

        while (true) {
            if ((nextLine = readNextLine(tasks, lineToWriteIndex)) != null) {
                if (isNumber(nextLine)) {
                    numbers.add(lineToWriteIndex, Integer.parseInt(nextLine));
                    break;
                }
            } else {
                tasks.remove(lineToWriteIndex);
                break;
            }
        }
    }

    /**
     * @param tasks            список активных задач {@link Task}.
     * @param lines            список строк для добавления следующей строки
     * @param lineToWriteIndex индекс элемента в списке, в который будет вставлена строка.
     *                         Также индекс задачи {@link Task} в списке задач.
     */
    public static void getNextLine(List<Task> tasks, List<String> lines, int lineToWriteIndex) {
        var nextLine = readNextLine(tasks, lineToWriteIndex);

        if (nextLine != null) {
            lines.add(lineToWriteIndex, nextLine);
        } else {
            tasks.remove(lineToWriteIndex);
        }
    }

    /**
     * Метод возобновляет чтение следующей строки из файла (пробуждает поток).
     *
     * @param tasks            список активных задач {@link Task}.
     * @param lineToWriteIndex индекс элемента в списке, в который будет добавлена прочитанная строка.
     *                         Также индекс задачи {@link Task} в списке активных задач.
     * @return следующая прочитанная строка либо {@code null}, если в файле не осталось строк.
     */
    private static String readNextLine(List<Task> tasks, int lineToWriteIndex) {
        Task nextTask;
        nextTask = tasks.get(lineToWriteIndex);
        nextTask.resumeReading();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return nextTask.getLine();
    }

    /**
     * Проверка, является ли строка числом.
     *
     * @param s проверяемая строка.
     * @return {@code true}, если строка является числом, иначе {@code false}.
     */
    private static boolean isNumber(String s) {
        return s.matches(POSITIVE_PATTERN) || s.matches(NEGATIVE_PATTERN);
    }
}
