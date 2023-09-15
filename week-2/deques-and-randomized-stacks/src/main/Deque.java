import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Let's try using a resizable array.
public class Deque<Item> implements Iterable<Item> {
    private Item[] q;

    private int back = 1;
    private int capacity = 2;
    private int front = 0;


    // construct an empty item array of back=1
    public Deque() {
        q = (Item[]) new Object[capacity];
    }

    // is the deque empty?
    public boolean isEmpty() {
        return (size() == 0);
    }

    // return the number of items on the deque
    public int size() {
        return back - front - 1;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (front < 0) {
            growArray();
        }
        q[front--] = item;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        // constant time
        if (back == capacity) {
            growArray();
        }
        q[back++] = item;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        // constant
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        Item oldFront = q[++front];
        q[front] = null;

        if (capacity > 1 && (size()) < (0.25 * capacity)) {
            shrinkArray();
        }

        return oldFront;
    }

    // remove and return the item from the back
    public Item removeLast() {
        // constant
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        Item oldBack = q[--back];
        q[back] = null;

        if (capacity > 1 && (size()) < (0.25 * capacity)) {
            shrinkArray();
        }

        return oldBack;
    }

    private void growArray() {
        // worst case = O(N)
        capacity = capacity * 2;
        if (capacity == 2) {
            // increase capacity again as adding to front or back
            // if capacity was increased from 1 will lead to growing again.
            capacity = 4;
        }
        int startPoint = capacity / 2 - 1;
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size(); i++) {
            copy[startPoint + i] = q[front + 1 + i];
        }
        // Update instance variables
        q = copy;
        back = startPoint + size();
        front = startPoint - 1;
    }

    private void shrinkArray() {
        // worst case O(N)
        capacity = (int) ((double) capacity / 2);
        Item[] copy = (Item[]) new Object[capacity];
        int startPoint = capacity - (capacity / 2);
        for (int i = 1; i < (back - front); i++) {
            copy[startPoint + i - 1] = q[front + i];
        }

        q = copy;
        back = startPoint + (back - front) - 1;
        front = startPoint - 1;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int i = front;

        public boolean hasNext() {
            return i < (back - 1);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (hasNext()) {
                // constant time
                return q[++i];
            } else {
                throw new NoSuchElementException();
            }
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> q = new Deque<>();
        q.addFirst(1);
        q.addFirst(2);
        q.addFirst(3);
        q.addFirst(4);
        // capacity = 4, size=4 -> size=1 required to trigger shrink
        q.removeFirst();
        q.removeFirst();
        q.removeFirst();
        // Should have shrinked by now. Check array size and items.
        int last = q.removeFirst();
        System.out.println(last);


    }

}
// Calculating the implementation memory size
// Item data type
// any primitive data type?
// assume worst case, so 8 bytes

// Deque() instance variables
// Item[] array -> 8N + 24 bytes
// int back -> 4 bytes
// int capacity -> 4 bytes
// int front -> 4 bytes

// ListIterator instance variables
// int i -> 4 bytes

// Total memory
// 8N + 40 bytes