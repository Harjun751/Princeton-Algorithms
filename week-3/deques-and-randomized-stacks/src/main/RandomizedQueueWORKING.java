import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueueWORKING<Item> implements Iterable<Item> {
    private Item[] q;

    private int randomCount = 0;

    private Item[] latestShuffled = null;

    private int i = 0;
    private int capacity = 2;

    // construct an empty randomized queue
    public RandomizedQueueWORKING() {
        q = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (size() == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return i;
    }

    // add the item
    public void enqueue(Item item) {
        // If latest shuffled exists, re-create the q list
        setArray();
        // Constant time
        if (i == capacity) {
            extendArray();
        }
        if (item == null) {
            throw new IllegalArgumentException();
        }
        q[i++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // slice array, O(N)
        if (latestShuffled == null) {
            latestShuffled = sliceArray(i);
        } else if ((i-1) == 0.25 * q.length) {
            setArray();
            latestShuffled = q;
        }
        Item old = latestShuffled[i - 1];

        i--;

        latestShuffled[i] = null;
        // to address loitering on main q
        q = latestShuffled;

        // Total time usage -> 2*N
        return old;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
//        setArray();
//        Item[] sliced = sliceArray(i);
//        return sliced[i - 1];
        return q[StdRandom.uniformInt(i)];
    }

    private Item[] sliceArray(int size) {
        Item[] sliced = (Item[]) new Object[size];
        for (int j = 0; j < i; j++) {
            sliced[j] = q[j];
        }
        StdRandom.shuffle(sliced);
        randomCount+=1;
        return sliced;
    }

    private void extendArray() {
        capacity = capacity * 2;
        if (capacity == 0) {
            capacity = 2;
        }
        Item[] copy = (Item[]) new Object[capacity];
        for (int j = 0; j < i; j++) {
            copy[j] = q[j];
        }
        q = copy;
    }

    private Item[] setArray() {
        if (latestShuffled != null) {
            // Create another array
            if (i < (double) capacity / 2) {
                capacity = capacity / 2;
            }
            q = (Item[]) new Object[capacity];
            // Populate array
            for (int j = 0; j < i; j++) {
                q[j] = latestShuffled[j];
            }
            // destroy latestShuffled
            latestShuffled = null;
        }
        return q;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private int j = 0;
        private final Item[] slicedArray;

        ListIterator() {
            setArray();
            slicedArray = sliceArray(i);
        }

        public boolean hasNext() {
            return j < i;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (hasNext()) {
                // constant time
                return slicedArray[j++];
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueueWORKING<Integer> q = new RandomizedQueueWORKING<>();
        q.enqueue(1);
        q.dequeue();
        q.enqueue(2);
        q.enqueue(3);
        for (int x : q) {
            System.out.println(x);
        }
        q.enqueue(4);
        q.enqueue(5);
        System.out.println("Removed: " + q.dequeue());
        System.out.println("Sample: " + q.sample());
        System.out.println("Sample: " + q.sample());
        System.out.println("Printing all elements again...");
        for (int x : q) {
            System.out.println(x);
        }
//
//        RandomizedQueueWORKING<Integer> q = new RandomizedQueueWORKING<>();
//        for (int i=0; i<10; i++){
//            q.enqueue(StdRandom.uniformInt(101));
//        }
//        for (int i=0; i<10; i++){
//            q.dequeue();
//        }
//        System.out.println(q.randomCount);

    }

}