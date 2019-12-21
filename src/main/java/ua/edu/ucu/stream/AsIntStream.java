package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;
import ua.edu.ucu.iterators.IntIterable;

import java.util.ArrayList;
import java.util.Iterator;

public class AsIntStream implements IntStream {
    private  final String MAP = "map";
    private  final String FILTER = "filter";
    private  final String FLATMAP = "flatMap";
    private IntIterable valueIterable;
    private ArrayList<String> operations;
    private ArrayList<Object> operands;
    private long length;
    private AsIntStream() {
        // To Do
    }
    private AsIntStream(int ... values) {
        operations = new ArrayList<>();
        operands = new ArrayList<>();
        length = values.length;
        initValues(values);
    }
    private void initValues(int ... values){
        Integer [] integerValues = new Integer[values.length];
        for (int i = 0; i < integerValues.length; i++) {
            integerValues[i] = Integer.valueOf(values[i]);
        }
        valueIterable = new IntIterable(integerValues);
    }
    protected void setOperations(ArrayList<String> operationSet){
        operations = operationSet;
    }

    protected void setOperands(ArrayList<Object> operandsSet){
        operands = operandsSet;
    }
    public static IntStream of(int... values) {
        return new AsIntStream(values);
    }

    private static IntStream of(ArrayList<Integer> arrayList) {
        int [] array = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            array[i] = arrayList.get(i);
        }
        return AsIntStream.of(array);
    }
    public static IntStream combined (ArrayList<IntStream> intStreams){
        ArrayList<Integer> integers  = new ArrayList<>();
        for (IntStream intStream: intStreams){
            for(Integer integer: intStream.toArray()){
                integers.add(integer);
            }
        }
        return  AsIntStream.of(integers);
    }

    @Override
    public Double average() {
        //Terminal
        AsIntStream stream = this;
        if (!stream.operands.isEmpty()) {
            stream = (AsIntStream) stream.executeIntermediate();
        }
        return Double.valueOf(stream.sum())/ (double) stream.length;
    }

    @Override
    public Integer max() {
        //Terminal
        AsIntStream stream = this;
        if (!stream.operands.isEmpty()) {
            stream = (AsIntStream) stream.executeIntermediate();
        }
        Iterator<Integer> iterator = stream.valueIterable.iterator();
        return reduce(iterator.next(), Integer::max);
    }

    @Override
    public Integer min() {
        //Terminal
        AsIntStream stream = this;
        if (!stream.operands.isEmpty()) {
            stream = (AsIntStream) stream.executeIntermediate();
        }
        Iterator<Integer> iterator = stream.valueIterable.iterator();
        return reduce(iterator.next(), Integer::min);
    }

    @Override
    public long count() {
        //Terminal
        AsIntStream stream = this;
        if (!stream.operands.isEmpty()) {
            stream = (AsIntStream) stream.executeIntermediate();
        }
        return stream.length;
    }

    @Override
    public Integer sum() {
        //Terminal
        AsIntStream stream = this;
        if (!stream.operands.isEmpty()) {
            stream = (AsIntStream) stream.executeIntermediate();
        }
        Integer sum = 0;
        for (Integer integer : stream.valueIterable) {
            sum+=integer;
        }
        return sum;
    }

    @Override
    public  IntStream filter(IntPredicate predicate) {
        if (operations.isEmpty() || !operations.get(0).equals(FILTER)){
            operations.add(FILTER);
            operands.add(predicate);
            return this;
        } else {
            return executeFilter();
        }
    }

    private IntStream executeFilter() {
        IntPredicate predicate = (IntPredicate) operands.get(0);
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (Integer integer : valueIterable) {
            if (predicate.test(integer)) {
                arrayList.add(integer);
            }
        }
        operands.remove(0);
        operations.remove(0);
        return AsIntStream.of(arrayList);
    }

    private IntStream executeIntermediate(){
        AsIntStream stream = this;
        ArrayList<String> currentOperations = this.operations;
        ArrayList<Object> currentOperands = this.operands;
        int i = 0;
        while (!currentOperations.isEmpty()){
            switch (currentOperations.get(i)) {
                case FILTER:
                    IntPredicate predicate = (IntPredicate) operands.get(0);
                    stream = (AsIntStream) stream.filter(predicate);
                    stream.setOperations(currentOperations);
                    stream.setOperands(currentOperands);
                    break;
                case MAP:
                    IntUnaryOperator mapper = (IntUnaryOperator) operands.get(0);
                    stream = (AsIntStream) stream.map(mapper);
                    stream.setOperations(currentOperations);
                    stream.setOperands(currentOperands);
                    break;
                case FLATMAP:
                    IntToIntStreamFunction func = (IntToIntStreamFunction) operands.get(0);
                    stream = (AsIntStream) stream.flatMap(func);
                    stream.setOperations(currentOperations);
                    stream.setOperands(currentOperands);
                    break;
            }
        }
        return stream;
    }


    @Override
    public void forEach(IntConsumer action) {
        //Terminal
        AsIntStream stream = this;
        if (!stream.operands.isEmpty()) {
            stream = (AsIntStream) stream.executeIntermediate();
        }
        for (Integer integer:valueIterable){
            action.accept(integer);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {
        if(operations.isEmpty() || !operations.get(0).equals(MAP)){
            operations.add(MAP);
            operands.add(mapper);
            return this;
        } else {
            return executeMap();
        }
    }

    private IntStream executeMap() {
        IntUnaryOperator mapper = (IntUnaryOperator) operands.get(0);
        int [] integerValues = new int[(int) length];
        int index = 0;
        for (Integer integer : valueIterable){
            integerValues[index] = mapper.apply(integer);
            index++;
        }
        operands.remove(0);
        operations.remove(0);
        return AsIntStream.of(integerValues);
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {
        if(operations.isEmpty() || !operations.get(0).equals("flatMap")){
            operations.add(FLATMAP);
            operands.add(func);
            return this;
        } else {
            return executeFlatMap();
        }
    }

    private IntStream executeFlatMap() {
        IntToIntStreamFunction func = (IntToIntStreamFunction) operands.get(0);
        ArrayList<IntStream> intStreams = new ArrayList<>();
        for(Integer integer : valueIterable){
            intStreams.add(func.applyAsIntStream(integer));
        }
        operations.remove(0);
        operands.remove(0);
        return AsIntStream.combined(intStreams);
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        //Terminal
        AsIntStream stream = this;
        if (!stream.operands.isEmpty()) {
            stream = (AsIntStream) stream.executeIntermediate();
        }
        int current = identity;
        for(Integer integer : stream.valueIterable){
            current = op.apply(current, integer);
        }
        return current;
    }

    @Override
    public int[] toArray() {
        IntStream stream = executeIntermediate();
        int [] array = new int [(int) length];
        int index = 0;
        for (Integer integer : valueIterable) {
            array[index] = integer;
            index++;
        }
        return array;
    }

}
