public class CircularSuffixArray {
    private final String[] suffixes;
    private final static int R = 256;
    private final String[] sorted;
    private final int[] index;

    private final int strLength;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        index = new int[s.length()];
        strLength = s.length();
        suffixes = new String[s.length()];

        // create circular suffix array
        for (int i = 0; i < s.length(); i++) {
            String suffix = s.substring(i) + s.substring(0, i);
            suffixes[i] = suffix;
            index[i] = i;
        }
        sorted = suffixes;
        msdSort();
    }

    private static int charAt(String s, int d) {
        if (d < s.length()) {
            return s.charAt(d);
        } else {
            return -1;
        }
    }

    // sort using manber-myers msd
    private void msdSort() {
        sort(sorted, new String[strLength], index, new int[strLength], 0, strLength - 1, 0);
    }

    private void sort(String[] a, String[] arr, int[] indexArr, int[] auxIndex, int lo, int hi, int d) {
        if (hi <= lo) {
            return;
        }
        // create a frequency list of characters that occur in the string
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            // i don't quite understand the +2 > find out why.
            // get the char at the ith string in the array, at the dth pos
            count[charAt(a[i], d) + 2]++;
        }
        for (int r = 0; r < R + 1; r++) {
            // create the cumulate list
            // the cumulates define at which index do
            // strings with that character start at.
            count[r + 1] += count[r];
        }
        for (int i = lo; i <= hi; i++) {
            // using the CUMULATE count field, get index of insert
            // then, insert item in said index.
            int x = count[charAt(a[i], d) + 1];
            arr[x] = a[i];
            // what we're saying is that
            // the index of the item just placed at x
            // is i. which is correct for the first recursion.
            auxIndex[x] = indexArr[i];
            count[charAt(a[i], d) + 1]++;
        }
        for (int i = lo; i <= hi; i++) {
            a[i] = arr[i - lo];
            indexArr[i] = auxIndex[i - lo];
        }
        for (int r = 0; r < R; r++) {
            sort(a, arr, indexArr, auxIndex, lo + count[r], lo + count[r + 1] - 1, d + 1);
        }
    }

    // length of s
    public int length() {
        return strLength;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
//        // get string
        if (i >= strLength || i < 0) {
            throw new IllegalArgumentException();
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}
