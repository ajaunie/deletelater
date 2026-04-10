package edu.ncsu.csc216.wolf_regrade.model.util;

/**
 * Concrete linked-list implementation of the IBackLog interface.
 * Elements are added to the end and can be accessed or removed from any position.
 * 
 * @param <E> the type of elements in the list
 * @author Your Name
 */
public class BackLog<E> implements IBackLog<E> {

    /** Number of elements in the list */
    private int size;
    /** Reference to the first node */
    private ListNode front;
    /** Reference to the last node */
    private ListNode back;

    /**
     * Constructs an empty BackLog.
     */
    public BackLog() {
        this.size = 0;
        this.front = null;
        this.back = null;
    }

    /**
     * Adds the element to the back of the list.
     * 
     * @param element element to add
     * @throws NullPointerException if element is null
     */
    @Override
    public void add(E element) {
    }

    /**
     * Removes and returns the element at the given index.
     * 
     * @param idx index to remove element from
     * @return element at the given index
     * @throws IndexOutOfBoundsException if the idx is out of bounds for the list
     */
    @Override
    public E remove(int idx) {
        return null;
    }

    /**
     * Replaces the element at the given index with the specified element
     * and returns the original element.
     * 
     * @param idx     index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the idx is out of bounds for the list
     * @throws NullPointerException      if element is null
     */
    @Override
    public E set(int idx, E element) {
        return null;
    }

    /**
     * Returns the element at the given index.
     * 
     * @param idx index of the element to retrieve
     * @return element at the given index
     * @throws IndexOutOfBoundsException if the idx is out of bounds for the list
     */
    @Override
    public E get(int idx) {
        return null;
    }

    /**
     * Returns the number of elements in the list.
     * 
     * @return number of elements in the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in this BackLog.
     * 
     * @return an Iterator for this BackLog
     */
    @Override
    public Iterator<E> iterator() {
        return new BackLogIterator();
    }

    /**
     * Validates that the given index is within bounds for the list.
     * 
     * @param idx the index to check
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    private void checkIndex(int idx) {
    }

    /**
     * Inner node class for the singly-linked list.
     */
    private class ListNode {

        /** The data stored in this node */
        public E data;
        /** Reference to the next node */
        public ListNode next;

        /**
         * Constructs a ListNode with the given data and a null next reference.
         * 
         * @param data the data to store
         */
        public ListNode(E data) {
            this.data = data;
            this.next = null;
        }

        /**
         * Constructs a ListNode with the given data and next reference.
         * 
         * @param data the data to store
         * @param next reference to the next node
         */
        public ListNode(E data, ListNode next) {
            this.data = data;
            this.next = next;
        }
    }

    /**
     * Inner iterator class for traversing the BackLog from front to back.
     */
    private class BackLogIterator implements Iterator<E> {

        /** The current node in the iteration */
        private ListNode current;

        /**
         * Constructs a BackLogIterator starting at the front of the list.
         */
        public BackLogIterator() {
            this.current = front;
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
         * Returns the next element in the iteration.
         * 
         * @return the next element
         * @throws java.util.NoSuchElementException if there are no more elements
         */
        @Override
        public E next() {
            return null;
        }
    }
}