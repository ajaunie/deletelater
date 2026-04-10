package edu.ncsu.csc216.wolf_regrade.model.util;

/**
 * Concrete implementation of the ISortedList interface.
 * 
 * @author Ajaunie White
 */
public class SortedList<E extends Comparable<E>> implements ISortedList<E> {

    private int size;
    private ListNode front;

    public SortedList() {
        this.size = 0;
        this.front = null;
    }

    @Override
    public void add(E element) { }

    @Override
    public E remove(int idx) { return null; }

    @Override
    public boolean contains(E element) { return false; }

    @Override
    public E get(int idx) { return null; }

    @Override
    public int size() { return size; }

    private void checkIndex(int idx) { }

    /** Inner class renamed to ListNode. */
    private class ListNode {
        public E data;
        public ListNode next;

        public ListNode(E data, ListNode next) {
            this.data = data;
            this.next = next;
        }
    }
}