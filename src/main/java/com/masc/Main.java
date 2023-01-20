package com.masc;

import com.masc.service.impl.SorterIntAsc;
import com.masc.service.impl.SorterIntDesc;
import com.masc.service.impl.SorterStringAsc;
import com.masc.service.impl.SorterStringDesc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;

import static com.masc.Constants.*;
import static com.masc.Utils.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(NO_PARAMETERS);
            System.exit(0);
        }

        var arguments = new LinkedList<>(Arrays.asList(args));

        if (!arguments.contains(STRING_SORT) && !arguments.contains(INTEGER_SORT)) {
            System.out.println(NO_PARAMETERS);
            System.exit(0);
        }

        if (arguments.contains(STRING_SORT) && arguments.contains(INTEGER_SORT) ||
                (arguments.contains(ASCENDING_SORT) && arguments.contains(DESCENDING_SORT))) {
            System.out.println(INVALID_PARAMETERS);
            System.exit(0);
        }

        var isDescendingSort = arguments.contains(DESCENDING_SORT) &&
                !arguments.contains(ASCENDING_SORT);
        var isStrings = arguments.contains(STRING_SORT);

        var dashArgs = new String[]{STRING_SORT, INTEGER_SORT, ASCENDING_SORT, DESCENDING_SORT};
        Arrays.stream(dashArgs).forEach(arguments::remove);

        var outFile = arguments.remove();

        var tasks = createTasks(arguments);
        tasks.forEach(task -> new Thread(task).start());

        try (var writer = Files.newBufferedWriter(Path.of(outFile))) {
            if (isStrings) {
                var strings = getStrings(tasks);
                String previousString;

                if (isDescendingSort) {
                    previousString = String.valueOf(Character.MAX_VALUE);
                    new SorterStringDesc(tasks, strings, writer, previousString).sort();
                } else {
                    previousString = EMPTY_STRING;
                    new SorterStringAsc(tasks, strings, writer, previousString).sort();
                }
            } else {
                var integers = getIntegers(tasks);
                int previousNumber;

                if (isDescendingSort) {
                    previousNumber = Integer.MAX_VALUE;
                    new SorterIntDesc(tasks, integers, writer, previousNumber).sort();
                } else {
                    previousNumber = Integer.MIN_VALUE;
                    new SorterIntAsc(tasks, integers, writer, previousNumber).sort();
                }
            }
        } catch (IOException e) {
            System.out.println(UNABLE_OPEN_FILE + e.getMessage());
        }
    }
}