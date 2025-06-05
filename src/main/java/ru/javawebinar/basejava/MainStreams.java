package ru.javawebinar.basejava;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainStreams {
    public static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (acc, digit) -> acc * 10 + digit);
    }

    public static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream()
                .mapToInt(Integer::intValue)
                .sum();

        boolean isSumOdd = sum % 2 != 0;

        return integers.stream()
                .filter(x -> isSumOdd == (x % 2 == 0))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        int[] test1 = {1, 2, 3, 3, 2, 3};
        System.out.println("minValue(" + Arrays.toString(test1) + ") = " + minValue(test1));

        int[] test2 = {9, 8};
        System.out.println("minValue(" + Arrays.toString(test2) + ") = " + minValue(test2));

        List<Integer> test3 = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("oddOrEven(" + test3 + ") = " + oddOrEven(test3));

        List<Integer> test4 = Arrays.asList(2, 4, 6, 8);
        System.out.println("oddOrEven(" + test4 + ") = " + oddOrEven(test4));
    }
}
