package edu.ncsu.csc216.wolf_regrade.model.util;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;

/**
 * Concrete implementation of the IStack interface using an array-based list.
 * @param <E> type of elements in the stack
 * @author Your Name
 */
public class CompletedRegradesStack<E> implements IStack<E> {

    /** Initial capacity of the underlying array */
    private static final int INITIAL_CAPACITY = 10;
    /** Underlying array to store elements */
    private E[] list;
    /** Current number of elements in the stack */
    private int size;

    /**
     * Constructs an empty stack.
     */
    @SuppressWarnings("unchecked")
    public CompletedRegradesStack() {
        this.list = (E[]) new Object[INITIAL_CAPACITY];
        this.size = 0;
    }

    @Override
    public void push(E element) {
        // Placeholder
    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new CompletedRegradesStackIterator();
    }

    /**
     * Increases capacity of the array if needed.
     */
    private void checkCapacity() {
        // Placeholder
    }

    /**
     * Inner class for the stack iterator.
     */
    private class CompletedRegradesStackIterator implements Iterator<E> {
        /** Current index in iteration */
        private int current;

        /**
         * Constructs the iterator.
         */
        public CompletedRegradesStackIterator() {
            this.current = size - 1;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public E next() {
            return null;
        }
    }
}