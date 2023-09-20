import edu.princeton.cs.algs4.MinPQ;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    private final int moves;

    private final SearchNode finalNode;

    private final boolean solvable;

    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode prevNode;
        private final int moves;

        private final int priority;


        private SearchNode(Board brd, SearchNode prev, int moves) {
            this.board = brd;
            this.prevNode = prev;
            this.moves = moves;
            this.priority = this.board.manhattan() + this.moves;

        }

        @Override
        public int compareTo(SearchNode other) {
            return Double.compare(this.priority, other.priority);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        Board twin = initial.twin();

        MinPQ<SearchNode> mainPQ = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();

        SearchNode mainNode = new SearchNode(initial, null, 0);
        SearchNode twinNode = new SearchNode(twin, null, 0);

        mainPQ.insert(mainNode);
        twinPQ.insert(twinNode);

        SearchNode mainSearch = mainPQ.delMin();
        SearchNode twinSearch = twinPQ.delMin();

        while (!mainSearch.board.isGoal() && !twinSearch.board.isGoal()) {
            for (Board board : mainSearch.board.neighbors()) {
                if (mainSearch.prevNode != null && board.equals(mainSearch.prevNode.board)) {
                    continue;
                }
                SearchNode node = new SearchNode(board, mainSearch, mainSearch.moves + 1);
                // check if board was in history
                mainPQ.insert(node);
            }
            for (Board board : twinSearch.board.neighbors()) {
                if (twinSearch.prevNode != null && board.equals(twinSearch.prevNode.board)) {
                    continue;
                }
                SearchNode node = new SearchNode(board, twinSearch, twinSearch.moves + 1);
                // check if board was in history
                twinPQ.insert(node);
            }
            mainSearch = mainPQ.delMin();
            twinSearch = twinPQ.delMin();
        }
        if (twinSearch.board.isGoal() && !mainSearch.board.isGoal()) {
            this.solvable = false;
            finalNode = mainSearch;
            this.moves = -1;
        } else {
            this.solvable = true;
            finalNode = mainSearch;
            this.moves = mainSearch.moves;
        }
    }
    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (moves == -1) {
            return null;
        }
        Board[] sln = new Board[moves + 1];
        int index = 0;
        SearchNode node = finalNode;
        sln[index++] = node.board;
        while (node.prevNode != null) {
            node = node.prevNode;
            sln[index++] = node.board;
        }
        int finalIndex = index;
        return () -> new ReverseBoardIterator(sln, finalIndex);
    }

    private static class ReverseBoardIterator implements Iterator<Board> {
        private final Board[] arr;
        private int maxIndex;


        public ReverseBoardIterator(Board[] arr, int maxIndex) {
            this.arr = arr;
            this.maxIndex = maxIndex - 1;
        }

        @Override
        public boolean hasNext() {
            return maxIndex >= 0;
        }

        @Override
        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return arr[maxIndex--];
        }
    }


    public static void main(String[] args) {
        int[][] brd = new int[][]{{1, 2, 7}, {0, 4, 3}, {6, 5, 8}};
        Board main = new Board(brd);

        Solver slv = new Solver(main);
        System.out.println(slv.moves);
    }
}