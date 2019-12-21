package ua.edu.ucu.iterators;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IntIterable implements Iterable<Integer> {
    private List<Integer> intValues;

    public IntIterable(Integer... values) {
        intValues = Arrays.asList(values);
    }

    @Override
    public Iterator<Integer> iterator() {
        return new IntIterator(intValues);
    }
}

