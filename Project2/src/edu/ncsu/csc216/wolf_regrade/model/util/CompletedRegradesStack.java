package edu.ncsu.csc216.wolf_regrade.model.util;

import edu.ncsu.csc216.wolf_regrade.model.regrade.RegradeRequest;

/**
 * Concrete array-based implementation of the IStack interface for RegradeRequest objects.
 * Follows last-in-first-out (LIFO) order. The array grows (doubles in capacity) as needed.
 * 
 * @author Your Name
 */
public class CompletedRegradesStack implements IStack<RegradeRequest> {

    /** Initial capacity of the underlying array */
    private static final int INITIAL_CAPACITY = 10;
    /** Underlying array to store elements */
    private RegradeRequest[] list;
    /** Current number of elements in the stack */
    private int size;

    /**
     * Constructs an empty CompletedRegradesStack with the initial capacity.
     */
    public CompletedRegradesStack() {
        list = new RegradeRequest[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Adds the element to the top of the stack, growing the array if needed.
     * 
     * @param element element to add
     * @throws NullPointerException if element is null
     */
    @Override
    public void push(RegradeRequest element) {
    }

    /**
     * Removes and returns the element at the top of the stack.
     * 
     * @return element at the top of the stack
     * @throws java.util.NoSuchElementException if the stack is empty
     */
    @Override
    public RegradeRequest pop() {
        return null;
    }

    /**
     * Returns the element at the top of the stack without removing it.
     * 
     * @return element at the top of the stack
     * @throws java.util.NoSuchElementException if the stack is empty
     */
    @Override
    public RegradeRequest peek() {
        return null;
    }

    /**
     * Returns the number of elements in the stack.
     * 
     * @return number of elements in the stack
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if the stack contains no elements.
     * 
     * @return true if the stack is empty
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns an iterator that traverses from top to bottom (most recently pushed first).
     * 
     * @return an Iterator for this stack
     */
    @Override
    public Iterator<RegradeRequest> iterator() {
        return new CompletedRegradesStackIterator();
    }

    /**
     * Doubles the capacity of the underlying array when it is full.
     * 
     * @param capacity the current capacity to grow from
     */
    private void checkCapacity(int capacity) {
    }

    /**
     * Inner iterator class that traverses the stack from top to bottom.
     */
    private class CompletedRegradesStackIterator implements Iterator<RegradeRequest> {

        /** Current index in the iteration (starts at top of stack) */
        private int current;

        /**
         * Constructs a CompletedRegradesStackIterator starting at the top of the stack.
         */
        public CompletedRegradesStackIterator() {
            this.current = size - 1;
        }

        /**
         * Returns true if there are more elements to iterate over.
         * 
         * @return true if there is a next element
         */
        @Override
        public boolean hasNext() {
            return false;
        }

        /**
         * Returns the next element in the iteration (from top to bottom).
         * 
         * @return the next element
         * @throws java.util.NoSuchElementException if there are no more elements
         */
        @Override
        public RegradeRequest next() {
            return null;
        }
    }
}