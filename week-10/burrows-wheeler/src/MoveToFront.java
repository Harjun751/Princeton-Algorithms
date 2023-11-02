import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.ST;

public class MoveToFront {
//    private static BinaryStdIn stream;

    private static BiST<Character, Integer> letterSet;

    // extended ascii set
    private static final int R = 255;

    // read character
    // print position
    // move character to front position
    // TODO: Check why it outputs EOL (it works tho..)
    public static void encode() {
        while (!BinaryStdIn.isEmpty()) {
            // read character
            char c = BinaryStdIn.readChar();
            // get position of character
            BinaryStdOut.write(letterSet.getValueByKey(c), 8);
            // remove character from sequence
            int position = letterSet.getValueByKey(c);
            letterSet.delete(c);
            // shift all other characters up to c by + 1 ( this makes char c's pos = 0)
            for (int i = position - 1; i >= 0; i--) {
                char x = letterSet.getKeyByValue(i);
                letterSet.update(x, i + 1);
            }
            letterSet.insert(c, 0);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
//        Initialize an ordered sequence of 256 characters,
//        where extended ASCII character i appears ith in the sequence.
//        Now, read each 8-bit character i (but treat it as an integer between 0 and 255) from standard input one at a time;
//        write the ith character in the sequence; and move that character to the front.
//        Check that the decoder recovers any encoded message.
        while (!BinaryStdIn.isEmpty()) {
            // read character (it is the index)
            char index = BinaryStdIn.readChar();
            // get character at index
            char c = letterSet.getKeyByValue((int) index);
            BinaryStdOut.write(c);
            // move to front

            // remove character from sequence
            int position = letterSet.getValueByKey(c);
            letterSet.delete(c);
            // shift all other characters up to c by + 1 ( this makes char c's pos = 0)
            for (int i = position - 1; i >= 0; i--) {
                char x = letterSet.getKeyByValue(i);
                letterSet.update(x, i + 1);
            }
            letterSet.insert(c, 0);
        }
        BinaryStdOut.flush();
    }

    private static void initAscii() {
        // initialize the ascii set with R+1 ascii characters
        letterSet = new BiST<>();
        for (int i = 0; i < R; i++) {
            letterSet.insert((char) i, i);
        }
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        initAscii();
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException();
        }
    }

    // A class for a 1 to 1 bidirectional symbol table
    private static class BiST<T1 extends Comparable<T1>, T2 extends Comparable<T2>> {
        // id : string
        private final ST<T1, T2> keyValueSet;
        // string : id
        private final ST<T2, T1> valueKeySet;

        public BiST() {
            keyValueSet = new ST<>();
            valueKeySet = new ST<>();
        }

        public void insert(T1 key, T2 value) {
            keyValueSet.put(key, value);
            valueKeySet.put(value, key);
        }

        public void delete(T1 key) {
            T2 value = keyValueSet.get(key);
            keyValueSet.delete(key);
            valueKeySet.delete(value);
        }

        public T2 getValueByKey(T1 key) {
            return keyValueSet.get(key);
        }

        public T1 getKeyByValue(T2 value) {
            return valueKeySet.get(value);
        }


        public Iterable<T2> valueIterator() {
            return valueKeySet.keys();
        }

        public boolean valueContains(T2 value) {
            return valueKeySet.contains(value);
        }

        public void update(T1 key, T2 newValue) {
            T2 oldValue = keyValueSet.get(key);
            valueKeySet.remove(oldValue);

            keyValueSet.put(key, newValue);
            // overwrite any other keys with same value
            valueKeySet.put(newValue, key);
        }
    }
}