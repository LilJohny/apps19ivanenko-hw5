package ua.edu.ucu;


import ua.edu.ucu.stream.AsIntStream;
import ua.edu.ucu.stream.IntStream;

public class StreamApp {

    public static int streamOperations(IntStream intStream) {
        //IntStream intStream = AsIntStream.of(-1, 0, 1, 2, 3); // input values
        int res = intStream
                .filter(x -> x > 0) // 1, 2, 3
                .map(x -> x * x) // 1, 4, 9
                .flatMap(x ->
                        AsIntStream.of(x - 1, x, x + 1)) // 0, 1, 2, 3, 4, 5, 8, 9, 10
                .reduce(0, (sum, x) -> sum += x); // 42
        return res;
    }

    public static int[] streamToArray(IntStream intStream) {
        int[] intArr = intStream.toArray();
        return intArr;
    }

    public static String streamForEach(IntStream intStream) {
        StringBuilder str = new StringBuilder();
        intStream.forEach(str::append);
        return str.toString();
    }

    public static long streamCount(IntStream intStream) {
        long res = intStream.filter((x) -> x > 0).count();
        return res;
    }

    public static Integer streamSum(IntStream intStream) {
        Integer res = intStream.sum();
        return res;
    }

    public static Integer streamMax(IntStream intStream) {
        Integer res = intStream.filter((x) -> x < 3).max();
        return res;
    }

    public static Integer streamMin(IntStream intStream) {
        Integer res = intStream.filter((x) -> x >= 0).min();
        return res;
    }

    public static Double streamAverage(IntStream intStream) {
        Double res = intStream.filter((x) -> x >= 0).average();
        return res;
    }
}
