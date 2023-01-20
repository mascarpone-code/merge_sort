package com.masc;

import java.io.BufferedReader;
import java.io.IOException;

import static com.masc.Constants.SPACE_SYMBOL;

/**
 * Класс представляет собой задачу по чтению строк из файла.
 * Задача выполняется в отдельном потоке.
 * После прочтения одной строки поток ожидает,
 * пока не будет пробуждён для чтения следующей строки.
 */
public class Task implements Runnable {
    private final BufferedReader reader;
    private String line;

    Task(BufferedReader reader) {
        this.reader = reader;
    }

    public String getLine() {
        return line;
    }

    @Override
    public void run() {
        try {
            String nextLine;

            while (reader.ready()) {
                if (!(nextLine = reader.readLine()).contains(SPACE_SYMBOL) &&
                        !nextLine.isBlank()) {
                    line = nextLine;

                    synchronized (this) {
                        wait();
                    }
                }
            }

            line = null;
            reader.close();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void resumeReading() {
        notify();
    }
}