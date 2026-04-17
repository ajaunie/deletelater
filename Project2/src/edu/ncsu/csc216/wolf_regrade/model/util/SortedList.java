package edu.ncsu.csc216.wolf_regrade.model.util;

/**
 * Linked list implementation that keeps objects in sorted order.
 * @param <E> element type extending Comparable
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
    public void add(E element) {
        if (element == null) throw new NullPointerException();
        if (contains(element)) throw new IllegalArgumentException("Duplicate element.");
        if (front == null || element.compareTo(front.data) < 0) {
            front = new ListNode(element, front);
        } else {
            ListNode current = front;
            while (current.next != null && element.compareTo(current.next.data) > 0) {
                current = current.next;
            }
            current.next = new ListNode(element, current.next);
        }
        size++;
    }

    @Override
    public E remove(int idx) {
        if (idx < 0 || idx >= size) throw new IndexOutOfBoundsException();
        E data;
        if (idx == 0) {
            data = front.data;
            front = front.next;
        } else {
            ListNode current = front;
            for (int i = 0; i < idx - 1; i++) current = current.next;
            data = current.next.data;
            current.next = current.next.next;
        }
        size--;
        return data;
    }

    @Override
    public boolean contains(E element) {
        ListNode current = front;
        while (current != null) {
            if (current.data.equals(element)) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public E get(int idx) {
        if (idx < 0 || idx >= size) throw new IndexOutOfBoundsException();
        ListNode current = front;
        for (int i = 0; i < idx; i++) current = current.next;
        return current.data;
    }

    @Override
    public int size() { return size; }

    private class ListNode {
        public E data;
        public ListNode next;
        public ListNode(E data, ListNode next) {
            this.data = data;
            this.next = next;
        }
    }
}