import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;

    private Item[] shuffled = null;

    private int i = 0;
    private int capacity = 2;

    // construct an empty randomized queue
    public RandomizedQueue() {
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
        // If shuffled exists, re-create the q list
        updateArray();
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
        if (shuffled == null) {
            shuffled = shuffleArray();
            q=null;
        }
        Item old = shuffled[i - 1];

        i--;

        shuffled[i] = null;

        // decrease size of array
        if (i <= 0.25*capacity){
            capacity = (int) (capacity*0.25);
            Item[] aux = (Item[]) new Object[capacity];
            // Populate array
            for (int j = 0; j < i; j++) {
                aux[j] = shuffled[j];
            }
            shuffled = aux;
        }

        // Total time usage -> 2*N
        return old;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) {
            throw new NoSuchElementException();
        }
        int r = StdRandom.uniformInt(0, i);
        updateArray();
        return q[r];
    }

    private Item[] shuffleArray() {
        Item[] newShuffled = (Item[]) new Object[i];
        for (int j = 0; j < i; j++) {
            newShuffled[j] = q[j];
        }
        StdRandom.shuffle(newShuffled);
        return newShuffled;
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

    private Item[] updateArray() {
        if (shuffled != null) {
            // check if shrinkage is required
            if (i <= 0.25*capacity){
                capacity = (int) (capacity*0.25);
                q = (Item[]) new Object[capacity];
                // Populate array
                for (int j = 0; j < i; j++) {
                    q[j] = shuffled[j];
                }
                shuffled = null;
            } else {
                q = shuffled;
                shuffled = null;
                capacity = q.length;
            }
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
            updateArray();
            slicedArray = shuffleArray();
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
        RandomizedQueue<Integer> q = new RandomizedQueue<>();
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

    }

}