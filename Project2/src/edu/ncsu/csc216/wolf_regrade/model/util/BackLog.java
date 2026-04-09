package edu.ncsu.csc216.wolf_regrade.model.util;

/**
 * Concrete implementation of the IBackLog interface.
 */
public class BackLog<E> implements IBackLog<E> {

    private int size;
    private ListNode front;
    private ListNode back;

    public BackLog() {
        this.size = 0;
        this.front = null;
        this.back = null;
    }

    @Override
    public void add(E element) { }

    @Override
    public E remove(int idx) { return null; }

    @Override
    public E set(int idx, E element) { return null; }

    @Override
    public E get(int idx) { return null; }

    @Override
    public int size() { return size; }

    @Override
    public Iterator<E> iterator() { return new BackLogIterator(); }

    private void checkIndex(int idx) { }

    /** Inner class renamed to ListNode. */
    private class ListNode {
        public E data;
        public ListNode next;
        public ListNode prev;

        public ListNode(E data, ListNode next, ListNode prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private class BackLogIterator implements Iterator<E> {
        private ListNode current;

        public BackLogIterator() {
            this.current = front;
        }

        @Override
        public boolean hasNext() { return false; }

        @Override
        public E next() { return null; }
    }
}