package ua.edu.ucu.iterators;

import java.util.List;
import java.util.NoSuchElementException;

public class IntIterator implements java.util.Iterator<Integer> {
    private int index;
    private List<Integer> integerList;

    public IntIterator(List<Integer> list) {
        integerList = list;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index != integerList.size();
    }

    @Override
    public Integer next() {
        if (hasNext()) {
            int oldIndex = index;
            index++;
            return integerList.get(oldIndex);
        } else {
            throw new NoSuchElementException();
        }
    }
}
