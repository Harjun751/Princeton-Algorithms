import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;

import java.util.HashSet;

// TODO: OPTIMIZATIONS
//  Exploit that fact that when you perform a prefix query operation, it is usually almost identical to the previous prefix query, except that it is one letter longer.
//  Check if hashset is the way to go?

public class BoggleSolverOLD
{
    private final Trie26 wordSet;
    private ST<Integer, Bag<Integer>> adjacentList;
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolverOLD(String[] dictionary){
        if (dictionary==null){
            throw new IllegalArgumentException();
        }
        // create a trie of valid words
        wordSet = new Trie26();
        for (String str : dictionary){
            if (str.length()>2){
                wordSet.put(str);
            }
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    private BoggleBoard board;
    private HashSet<String> validWords;
    public Iterable<String> getAllValidWords(BoggleBoard board){
        if (board==null){
            throw new IllegalArgumentException();
        }
        this.board = board;
        this.validWords = new HashSet<>();
        this.adjacentList = new ST<>();
        precomputeBoggle();

        for (int i = 0; i < board.rows(); i++){
            for (int j = 0; j < board.cols(); j++) {
                search(i, j, "", new boolean[board.rows()][board.cols()]);
            }
        }
        return validWords;
    }

    private int getFlatIndex(int i, int j){
        return i * board.cols() + j;
    }

    private int[] get2DIndex(int x){
        int[] index = new int[2];
        index[0] = x / board.cols();
        index[1] = x % board.cols();
        return index;
    }

    private void precomputeBoggle(){
        for (int i = 0; i < board.rows(); i++){
            for (int j = 0; j < board.cols(); j++){
                int index = getFlatIndex(i,j);
                Bag<Integer> adjacent = new Bag<>();
                // get adjacent items
                // get upper item
                if (i-1 >= 0){
                    adjacent.add(getFlatIndex(i-1, j));
                    // get diagonally upper-right item
                    if (j+1 < board.cols()){
                        adjacent.add(getFlatIndex(i-1, j+1));
                    }
                    // get diagonally upper-left item
                    if (j-1 >= 0){
                        adjacent.add(getFlatIndex(i-1, j-1));
                    }
                }

                // get lower items
                if (i+1 < board.rows()){
                    adjacent.add(getFlatIndex(i+1, j));
                    // get diagonally lower-right item
                    if (j+1 < board.cols()){
                        adjacent.add(getFlatIndex(i+1, j+1));
                    }
                    // get diagonally lower-left item
                    if (j-1 >= 0){
                        adjacent.add(getFlatIndex(i+1, j-1));
                    }
                }

                // get item on left and right
                if (j+1 < board.cols()){
                    adjacent.add(getFlatIndex(i, j+1));
                }
                if (j-1 >= 0){
                    adjacent.add(getFlatIndex(i, j-1));
                }
                adjacentList.put(index, adjacent);
            }
        }
    }


    private String getLetter(int i, int j){
        String letter = String.valueOf(board.getLetter(i, j));
        if (letter.equals("Q")){
            letter = "QU";
        }
        return letter;
    }

    private void search(int i, int j, String currWord, boolean[][] marked){
        marked[i][j] = true;
        currWord += getLetter(i, j);
        if (wordSet.contains(currWord)){
            validWords.add(currWord);
        } else {
            // early termination: check if this is a prefix of any valid word
            if (!wordSet.prefixExists(currWord)){
                // we don't need to carry on - terminate
                return;
            }
        }

        for (int adj : adjacentList.get(getFlatIndex(i, j))){
            int[] index = get2DIndex(adj);
            if (!marked[index[0]][index[1]]){
                // recurse down, cloning visited
                search(index[0], index[1], currWord, marked);
                // we reset setting the flag of the just-visited node to false
                // so that the next adjacent node will be able to traverse properly
                // this is slightly cheaper than creating a new visited object
                marked[index[0]][index[1]] = false;
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        if (word==null){
            throw new IllegalArgumentException();
        }
        word = word.toUpperCase();
        if (wordSet.contains(word)){
            if (word.length() >= 8){
                return 11;
            } else if (word.length() == 7){
                return 5;
            } else if (word.length() == 6){
                return 3;
            } else if (word.length() == 5){
                return 2;
            } else if (word.length() >= 3){
                return 1;
            }
        }
        return 0;
    }

    private class Trie26{
        // a 26-way trie
        private static final int R = 26;
        private Node root = new Node();
        private class Node{
            private Node[] next = new Node[R];

            private boolean hasNext = false;
            private boolean isWord = false;
        }

        private void put(String key){
            root = put(root, key, 0);
        }

        private Node put(Node node, String word, int charIndex){
            if (node == null){
                node = new Node();
            }
            if (charIndex == word.length()) {
                // reach end of word - set flag, return
                node.isWord = true;
                return node;
            }
            node.hasNext = true;
            char c = word.charAt(charIndex);
            // minus 65 to start 0-indexing
            c -= 65;
            node.next[c] = put(node.next[c], word, charIndex+1);
            return node;
        }

        public boolean contains(String word){
            Node node = get(root, word, 0);
            if (node == null){
                return false;
            } else {
                return node.isWord;
            }
        }

        private Node get(Node node, String word, int charIndex){
            if (node == null){
                return null;
            }
            if (charIndex == word.length()){
                return node;
            }
            char c = word.charAt(charIndex);
            c -= 65;
            return get(node.next[c], word, charIndex+1);
        }

        public boolean prefixExists(String word){
            // node x is the subtrie where word is a possible prefix
            Node x = get(root, word, 0);
            if (x==null){
                return false;
            }
            return x.hasNext;
        }
    }
}