package ua.edu.ucu.iterators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IntIterable implements Iterable<Integer> {
    private List<Integer> intValues;

    public IntIterable(Integer ...values){
        intValues = Arrays.asList(values);
    }

    @Override
    public Iterator<Integer> iterator() {
        return  new IntIterator(intValues);
    }
}

class IntIterator implements java.util.Iterator<Integer> {
    private int index;
    private List<Integer> integerList;
    public IntIterator(List<Integer> list){
        integerList = list;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index != integerList.size() ;
    }

    @Override
    public Integer next() {
        int oldIndex = index;
        index++;
        return integerList.get(oldIndex);
    }
}
