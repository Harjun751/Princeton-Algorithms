import edu.princeton.cs.algs4.BinaryStdIn;

import java.util.ArrayList;
import java.util.Collections;

public class CircularSuffixArray {
    private final ArrayList<String> suffixes;
    private final String[] sorted;
    private final int[] index;

    private final int strLength;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s==null){
            throw new IllegalArgumentException();
        }
        index = new int[s.length()];
        strLength = s.length();
        suffixes = new ArrayList<>();
        sorted = new String[s.length()];

        // create circular suffix array
        for (int i = 0; i < s.length(); i++) {
            String suffix = s.substring(i) + s.substring(0, i);
            suffixes.add(suffix);
        }

        ArrayList<String> sortedArrayList = new ArrayList<>(suffixes);

        Collections.sort(sortedArrayList);
        // sort the array of strings in lexical order
        int i = 0;
        for (String suf : sortedArrayList) {
            sorted[i] = suf;
            i++;
        }
        // Custom sort required
        // while sorting, remember the index from which the suffix came from
        // then, add index[sortedIndex] = original index.
    }

    // length of s
    public int length() {
        return strLength;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        // get string
        if (i >= strLength){
            throw new IllegalArgumentException();
        }
        String o = sorted[i];
        return suffixes.indexOf(o);
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}
