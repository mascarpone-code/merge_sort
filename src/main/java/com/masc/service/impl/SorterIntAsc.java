package com.masc.service.impl;

import com.masc.Task;
import com.masc.service.Sorter;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static com.masc.Constants.LINE_FEED;
import static com.masc.Constants.UNABLE_OPEN_FILE;
import static com.masc.Utils.*;

/**
 * Реализация сортировщика целых чисел по возрастанию.
 */
public class SorterIntAsc extends Sorter<Integer> {
    public SorterIntAsc(List<Task> tasks, List<Integer> integers, Writer writer, Integer previousInt) {
        super(tasks, integers, writer, previousInt);
    }

    @Override
    public void sort() {
        while (!getLines().isEmpty()) {
            var lineToWriteIndex = 0;
            var lineToWrite = getLines().get(lineToWriteIndex);

            for (int i = 1; i < getLines().size(); i++) {
                var nextLine = getLines().get(i);

                if (nextLine <= lineToWrite) {
                    lineToWrite = nextLine;
                    lineToWriteIndex = i;
                }
            }

            if (lineToWrite >= getPreviousLine()) {
                try {
                    getWriter().write(lineToWrite + LINE_FEED);
                    setPreviousLine(getLines().remove(lineToWriteIndex));
                } catch (IOException e) {
                    System.out.println(UNABLE_OPEN_FILE + e.getMessage());
                }
            } else {
                getLines().remove(lineToWriteIndex);
            }

            getNextNumber(getTasks(), getLines(), lineToWriteIndex);
        }
    }
}
