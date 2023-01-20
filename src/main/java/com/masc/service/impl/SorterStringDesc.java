package com.masc.service.impl;

import com.masc.Task;
import com.masc.service.Sorter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static com.masc.Constants.LINE_FEED;
import static com.masc.Constants.UNABLE_OPEN_FILE;
import static com.masc.Utils.getNextLine;

/**
 * Реализация сортировщика строк по убыванию.
 */
public class SorterStringDesc extends Sorter<String> {
    public SorterStringDesc(List<Task> tasks, List<String> lines, Writer writer, String previousLine) {
        super(tasks, lines, writer, previousLine);
    }

    @Override
    public void sort() {
        while (!getLines().isEmpty()) {
            var lineToWriteIndex = 0;
            var lineToWrite = getLines().get(lineToWriteIndex);

            for (int i = 1; i < getLines().size(); i++) {
                var nextLine = getLines().get(i);

                if (nextLine.compareTo(lineToWrite) >= 0) {
                    lineToWrite = nextLine;
                    lineToWriteIndex = i;
                }
            }

            if (lineToWrite.compareTo(getPreviousLine()) <= 0) {
                try {
                    getWriter().write(lineToWrite + LINE_FEED);
                    setPreviousLine(getLines().remove(lineToWriteIndex));
                } catch (IOException e) {
                    System.out.println(UNABLE_OPEN_FILE + e.getMessage());
                }
            } else {
                getLines().remove(lineToWriteIndex);
            }

            getNextLine(getTasks(), getLines(), lineToWriteIndex);
        }
    }
}
