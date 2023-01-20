package com.masc.service;

import com.masc.Task;

import java.io.Writer;
import java.util.List;

/**
 * Абстрактный класс, описывающий сущность сортировщика.
 *
 * @param <T> тип сортируемых данных.
 */
public abstract class Sorter<T> {
    private final List<Task> tasks;
    private final List<T> lines;
    private final Writer writer;
    private T previousLine;

    public Sorter(List<Task> tasks, List<T> lines, Writer writer, T previousLine) {
        this.tasks = tasks;
        this.lines = lines;
        this.writer = writer;
        this.previousLine = previousLine;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<T> getLines() {
        return lines;
    }

    public Writer getWriter() {
        return writer;
    }

    public T getPreviousLine() {
        return previousLine;
    }

    public void setPreviousLine(T previousLine) {
        this.previousLine = previousLine;
    }

    protected abstract void sort();
}
