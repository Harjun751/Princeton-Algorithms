import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

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

        //      3a: get first character of sequence
        //      3b: get all occurrences of t[x] where last index of t[x] string is equal to FIRST character of the previous sequence
        //              i.e. we are finding the index of the next sequence of the original array
        //      3b: if necessary, apply rule:
        //              If sorted row i and j both start with the same character and i < j, then next[i] < next[j].
        // now, using the next[], construct the text -> start at first, append first character of next[first] sequence and so on.

        // get first and target string from input
        int first = BinaryStdIn.readInt();
        String target = BinaryStdIn.readString();

        // Step 1: sort input to get first column characters for t[]
        char[] charArray = target.toCharArray();
        Arrays.sort(charArray);
        String sorted = new String(charArray);

        // Step 2: create t[] -> string array where t[i] = sorted.index(i) + string.index(i)
        String t[] = new String[target.length()];
        for (int i = 0; i < target.length(); i++) {
            t[i] = "" + sorted.charAt(i) + target.charAt(i);
        }

        // Step 3: create next[] from first
        int[] next = new int[target.length()];
        boolean[] used = new boolean[target.length()];
        for (int x = 0; x < target.length(); x++) {
            // get 0th index
            String firstChar = String.valueOf(t[x].charAt(0));
//            BinaryStdOut.write(firstChar);

            // calculate next
            for (int i = 0; i < target.length(); i++) {
                String lastChar = String.valueOf(t[i].charAt(1));
                if (lastChar.equals(firstChar)) {
                    if (!used[i]) {
                        next[x] = i;
                        used[i] = true;
                        break;
                    }
                }
            }
        }
        int curr = first;
        while (true) {
            String firstChar = String.valueOf(t[curr].charAt(0));
            BinaryStdOut.write(firstChar);
            curr = next[curr];

            if (curr == first) {
                break;
            }
        }
        BinaryStdOut.flush();
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