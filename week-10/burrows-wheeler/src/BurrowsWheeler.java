

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String target = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(target);
        StringBuilder encoded = new StringBuilder();

        for (int i = 0; i < csa.length(); i++) {
            int index = csa.index(i);
            // add the last letter to encoded;
            // this is the row in which the original string ends up
            if (index == 0) {
                BinaryStdOut.write(i);
                // get the last character
                encoded.append(target.charAt(csa.length() - 1));
            } else {
                // get the character at index - 1
                encoded.append(target.charAt(index - 1));
            }
        }
        BinaryStdOut.write(encoded.toString());
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output

    public static void inverseTransform() {
        // inverse transform
        // from input get all the chars and put into an array
        int first = BinaryStdIn.readInt();
        ArrayList<Character> lastChars = new ArrayList<>();
        while (!BinaryStdIn.isEmpty()) {
            lastChars.add(BinaryStdIn.readChar());
        }
        // sort the lastChars array to get the firstChars
        char[] firstChars = keyIndexedCounting(lastChars);

        // one pass attempt
        int[] next = new int[lastChars.size()];

        // search STBag is populated during sorting
        STBag indexed = new STBag();
        for (int i = 0; i < firstChars.length; i++) {
            char firstChar = firstChars[i];
            char lastChar = lastChars.get(i);

            boolean needIndex = true;

            if (search.contains(lastChar)) {
                int index = search.get(lastChar);
                next[index] = i;
                needIndex = false;
            }
            if (indexed.contains(firstChar)) {
                int index = indexed.get(firstChar);
                next[i] = index;
            }
            if (needIndex) {
                indexed.add(lastChar, i);
            }

        }

        int curr = first;
        for (int i = 0; i < firstChars.length; i++) {
            BinaryStdOut.write(firstChars[curr]);
            curr = next[curr];
        }
        BinaryStdOut.flush();
    }

    private static STBag search;

    private static char[] keyIndexedCounting(ArrayList<Character> list) {
//        got a hint that this may be useful for getting the next[] array. we can think about it
        int N = list.size();
        int r1 = 256;
        int[] count = new int[r1 + 1];
        char[] sorted = new char[N];
        search = new STBag();

        // increment the count of character
        for (Character indexedCharacter : list) {
            count[indexedCharacter + 1]++;
        }

        // create the cumulative index list
        for (int r = 0; r < r1; r++) {
            count[r + 1] += count[r];
        }

        for (Character indexedCharacter : list) {
            search.add(indexedCharacter, count[indexedCharacter]);
            sorted[count[indexedCharacter]++] = indexedCharacter;
        }
        return sorted;
    }

    private static class STBag {
        private final ST<Character, Queue<Integer>> set;

        public STBag() {
            this.set = new ST<>();
        }

        public void add(Character c, int index) {
            Queue<Integer> q = set.get(c);
            if (q == null) {
                q = new Queue<>();
                set.put(c, q);
            }
            q.enqueue(index);
        }

        public int get(Character c) {
            if (set.contains(c)) {
                return set.get(c).dequeue();
            }
            return -1;
        }

        public boolean contains(Character c) {
            if (set.contains(c)) {
                return set.get(c).size() > 0;
            }
            return false;
        }
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException();
        }
    }

}