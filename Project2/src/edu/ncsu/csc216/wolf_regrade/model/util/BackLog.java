package edu.ncsu.csc216.wolf_regrade.model.util;

import java.util.NoSuchElementException;

/**
 * Linked list implementation of a backlog.
 * @param <E> element type
 * @author Ajaunie White
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
    public void add(E element) {
        if (element == null) throw new NullPointerException();
        ListNode newNode = new ListNode(element, null, back);
        if (front == null) {
            front = newNode;
        } else {
            back.next = newNode;
        }
        back = newNode;
        size++;
    }

    @Override
    public E remove(int idx) {
        checkIndex(idx);
        ListNode current = getNode(idx);
        if (current.prev != null) current.prev.next = current.next;
        else front = current.next;
        if (current.next != null) current.next.prev = current.prev;
        else back = current.prev;
        size--;
        return current.data;
    }

    @Override
    public E set(int idx, E element) {
        if (element == null) throw new NullPointerException();
        checkIndex(idx);
        ListNode current = getNode(idx);
        E old = current.data;
        current.data = element;
        return old;
    }

    @Override
    public E get(int idx) {
        checkIndex(idx);
        return getNode(idx).data;
    }

    @Override
    public int size() { return size; }

    @Override
    public Iterator<E> iterator() { return new BackLogIterator(); }

    private void checkIndex(int idx) {
        if (idx < 0 || idx >= size) throw new IndexOutOfBoundsException();
    }

    private ListNode getNode(int idx) {
        ListNode current = front;
        for (int i = 0; i < idx; i++) current = current.next;
        return current;
    }

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
        private ListNode current = front;
        public boolean hasNext() { return current != null; }
        public E next() {
            if (!hasNext()) throw new NoSuchElementException();
            E data = current.data;
            current = current.next;
            return data;
        }
    }
}