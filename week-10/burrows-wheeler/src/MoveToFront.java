import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.NoSuchElementException;

public class MoveToFront {
    private static final int R = 256;

    // read character
    // print position
    // move character to front position
    public static void encode() {
        DoublyLinkedList LL = initAsciiLL();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = LL.getIndex(c);
            BinaryStdOut.write(index, 8);
            LL.moveToFront(index);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        DoublyLinkedList letterSet = initAsciiLL();
//        Initialize an ordered sequence of 256 characters,
//        where extended ASCII character i appears ith in the sequence.
//        Now, read each 8-bit character i (but treat it as an integer between 0 and 255) from standard input one at a time;
//        write the ith character in the sequence; and move that character to the front.
//        Check that the decoder recovers any encoded message.
        while (!BinaryStdIn.isEmpty()) {
            // read character (it is the index)
            int index = BinaryStdIn.readChar();

            // get character at index
            char c = letterSet.get(index);
            BinaryStdOut.write(c);

            // add to movements
            letterSet.moveToFront(index);
        }
        BinaryStdOut.flush();
    }

    private static DoublyLinkedList initAsciiLL() {
        // initialize the ascii set with R+1 ascii characters
        DoublyLinkedList LL = new DoublyLinkedList((char) 0);
        for (int i = 1; i < R; i++) {
            LL.add((char) i);
        }
        return LL;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static class DoublyLinkedList {
        private Node root;

        private Node last;

        public DoublyLinkedList(Character item) {
            this.root = new Node();
            last = root;
        }

        public void add(Character item) {
            Node newNode = new Node(item);
            newNode.before = last;
            last.next = newNode;
            last = newNode;
        }

        public Character get(int index) {
            Node t = root;
            // index-traverse
            for (int i = 0; i < index; i++) {
                t = t.next;
            }
            return t.item;
        }

        public int getIndex(Character item) {
            Node t = root;
            int i = 0;
            while (t != null) {
                if (t.item == item) {
                    return i;
                }
                t = t.next;
                i += 1;
            }
            throw new NoSuchElementException();
        }

        public void moveToFront(int index) {
            Node t = root;
            // index-traverse
            for (int i = 0; i < index; i++) {
                t = t.next;
            }
            if (t == root) {
                return;
            }
            // change the next field on the previous node
            // to the next node
            t.before.next = t.next;
            if (t.next != null) {
                t.next.before = t.before;
            }
            t.before = null;
            // set the current root as t's next
            t.next = root;
            // set root's before
            root.before = t;
            // update root
            root = t;
        }


        private static class Node {
            public char item;
            public Node next;
            public Node before;

            public Node(char item) {
                this.item = item;
            }

            public Node(char item, Node next, Node before) {
                this.item = item;
                this.next = next;
                this.before = before;
            }

            public Node() {

            }
        }
    }
}