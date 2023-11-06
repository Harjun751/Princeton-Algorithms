public class CircularSuffixArray {
    private final static int R = 256;
    private final int[] index;

    private final int strLength;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        index = new int[s.length()];
        strLength = s.length();
        CircularSuffix[] suffixes = new CircularSuffix[s.length()];

        // create circular suffix array
        for (int i = 0; i < s.length(); i++) {
            suffixes[i] = new CircularSuffix(s, i);
            index[i] = i;
        }
        msdSort(suffixes);
    }

    // sort using manber-myers msd
    private void msdSort(CircularSuffix[] array) {
        sort(array, new CircularSuffix[strLength], index, new int[strLength], 0, strLength - 1, 0);
    }

    private void sort(CircularSuffix[] a, CircularSuffix[] arr, int[] indexArr, int[] auxIndex, int lo, int hi, int d) {
        if (hi <= lo + 15) {
            insertSort(a, indexArr, lo, hi, d);
            return;
        }
        // create a frequency list of characters that occur in the string
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            // get the char at the ith string in the array, at the dth pos
            count[a[i].charAt(d) + 2]++;
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
            int x = count[a[i].charAt(d) + 1];
            arr[x] = a[i];
            auxIndex[x] = indexArr[i];
            count[a[i].charAt(d) + 1]++;
        }
        for (int i = lo; i <= hi; i++) {
            a[i] = arr[i - lo];
            indexArr[i] = auxIndex[i - lo];
        }
        for (int r = 0; r < R; r++) {
            sort(a, arr, indexArr, auxIndex, lo + count[r], lo + count[r + 1] - 1, d + 1);
        }
    }

    private static void insertSort(CircularSuffix[] a, int[] indexes, int lo, int hi, int d){
        for (int i = lo; i <= hi; i++){
            for (int j = i; j>lo && less(a[j], a[j-1], d); j--){
                exch(a, j, j-1);
                int old = indexes[j];
                indexes[j] = indexes[j-1];
                indexes[j-1] = old;
            }
        }
    }

    private static void exch(Object[] a, int i, int j){
        Object old = a[i];
        a[i] = a[j];
        a[j] = old;
    }

    private static boolean less(CircularSuffix v, CircularSuffix w, int d){
        for (int i = d; i < v.original.length(); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return false;
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

    private static class CircularSuffix {
        //        implicitly define a circ suffix.
//        reference to input string
//        pointer/ Index? of first character
        private final String original;
        private final int shift;

        public CircularSuffix(String original, int shift) {
            this.original = original;
            this.shift = shift;
        }

        public int charAt(int d) {
            if (d < original.length()) {
                int index = d + this.shift;
                if (index > original.length() - 1) {
                    index -= original.length();
                }
                return original.charAt(index);
            } else {
                return -1;
            }
        }
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}
