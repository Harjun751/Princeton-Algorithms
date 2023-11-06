

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


        // use key index counting on the last column
        // to get the next array
        int[] next = new int[lastChars.size()];
        char[] firstChars = keyIndexedCounting(lastChars, next);

        int curr = first;
        for (int i = 0; i < firstChars.length; i++) {
            BinaryStdOut.write(firstChars[curr]);
            curr = next[curr];
        }
        BinaryStdOut.flush();
    }

    private static char[] keyIndexedCounting(ArrayList<Character> list, int[] next) {
        int r1 = 256;
        char[] sorted = new char[list.size()];
        int[] count = new int[r1 + 1];
        STBag indexed = new STBag();

        // increment the count of character
        int i = 0;
        for (Character indexedCharacter : list) {
            count[indexedCharacter + 1]++;
            // index the positions of the last character column
            indexed.add(indexedCharacter, i);
            i++;
        }

        // create the cumulative index list
        for (int r = 0; r < r1; r++) {
            count[r + 1] += count[r];
        }

        for (char c : list) {
            // calculate the next[] by finding the
            // first occurence of this character in
            // the indexed last characters
            // this character represents the first column
            // count[c] represents where the character is in sorted order
            next[count[c]] = indexed.get(c);
            sorted[count[c]++] = c;
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
            // assume contains() is already called
            return set.get(c).dequeue();
        }
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            long start = System.nanoTime();
            inverseTransform();
            long end = System.nanoTime();
            System.out.printf("Elapsed time %.2f ms", (end-start)/1000000.0);
        } else {
            throw new IllegalArgumentException();
        }
    }

}