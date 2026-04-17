package edu.ncsu.csc216.wolf_regrade.model.util;

import java.util.NoSuchElementException;
import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;

/**
 * Array-based stack implementation for completed requests.
 * @author Ajaunie White
 */
public class CompletedRegradesStack implements IStack<RegradeRequest> {
    private static final int INITIAL_CAPACITY = 10;
    private RegradeRequest[] list;
    private int size;

    public CompletedRegradesStack() {
        this.list = new RegradeRequest[INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void push(RegradeRequest element) {
        if (element == null) throw new NullPointerException();
        if (size == list.length) {
            RegradeRequest[] next = new RegradeRequest[list.length * 2];
            System.arraycopy(list, 0, next, 0, size);
            list = next;
        }
        list[size++] = element;
    }

    @Override
    public RegradeRequest pop() {
        if (isEmpty()) throw new NoSuchElementException();
        RegradeRequest data = list[--size];
        list[size] = null;
        return data;
    }

    @Override
    public RegradeRequest peek() {
        if (isEmpty()) throw new NoSuchElementException();
        return list[size - 1];
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public Iterator<RegradeRequest> iterator() { return new CompletedRegradesStackIterator(); }

    private class CompletedRegradesStackIterator implements Iterator<RegradeRequest> {
        private int current = size - 1;
        public boolean hasNext() { return current >= 0; }
        public RegradeRequest next() {
            if (!hasNext()) throw new NoSuchElementException();
            return list[current--];
        }
    }
}