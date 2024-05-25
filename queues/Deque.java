/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first, last;

    private class Node {
        Node prev;
        Item item;
        Node next;
    }

    public Deque() {
        first = null;
        last = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item can't be null");
        }
        Node oldFirst = first;
        first = new Node();
        first.next = oldFirst;
        first.prev = null;
        first.item = item;
        size++;
        if (oldFirst != null) oldFirst.prev = first;
        if (last == null) last = first;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item can't be null");
        }
        Node oldLast = last;
        last = new Node();
        last.next = null;
        last.prev = oldLast;
        last.item = item;
        size++;
        if (oldLast != null) oldLast.next = last;
        if (first == null) {
            first = last;
        }
    }

    public Item removeFirst() {
        if (size == 0) throw new NoSuchElementException("you don't have a node");
        Item item = first.item;
        first = first.next;

        if (first == null) {
            last = null;
        }
        else first.prev = null;
        size--;
        return item;
    }

    public Item removeLast() {
        if (size == 0) throw new NoSuchElementException("you don't have a node");
        Item item = last.item;
        last = last.prev;

        if (last == null) {
            first = null;
        }
        else {
            last.next = null;
        }
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addLast(5);
        deque.addFirst(5);
        deque.removeFirst();
        deque.removeLast();

        // Use the iterator to print the elements in the deque
        for (Integer item : deque) {
            System.out.println(item);
        }
    }
}
