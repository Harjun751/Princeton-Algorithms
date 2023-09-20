import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private final int[][] board;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    // ENSURE IMMUTABILITY
    public Board(int[][] tiles) {
        int[][] copy = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        this.board = copy;
    }

    // string representation of this board
    public String toString() {
        StringBuilder finalStr = new StringBuilder();
        finalStr.append(dimension()).append("\n").append(" ");
        for (int[] intArr : this.board) {
            for (int num : intArr) {
                finalStr.append(num).append("  ");
            }
            finalStr.append("\n ");
        }
        return finalStr.toString();
    }

    // board dimension n
    public int dimension() {
        return this.board.length;
    }

    // number of tiles out of place
    public int hamming() {
        int tHamming = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                int currNum = this.board[i][j];
                if (currNum == 0) {
                    continue;
                }
                // get the desired Y index
                int desiredY = (currNum - 1) / dimension();
                // get the desired x index
                int desiredX = currNum % dimension() - 1;
                if (desiredX == -1) {
                    desiredX = dimension() - 1;
                }
                if (i != desiredY || j != desiredX) {
                    tHamming += 1;
                }
            }
        }
        return tHamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int tHattan = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                int currNum = this.board[i][j];
                if (currNum == 0) {
                    continue;
                }
                // get the desired Y index
                int desiredY = (currNum - 1) / dimension();
                // get the desired x index
                int desiredX = currNum % dimension() - 1;
                if (desiredX == -1) {
                    desiredX = dimension() - 1;
                }
                tHattan += Math.abs(j - desiredX);
                tHattan += Math.abs(i - desiredY);
            }
        }
        return tHattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (other.dimension() != this.dimension()) {
            return false;
        }

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.board[i][j] != other.board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[] findZero() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (board[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{};
    }

    private static class BoardIterator implements Iterator<Board> {
        private final Board[] arr;
        private final int maxIndex;

        private int currIndex = 0;

        public BoardIterator(Board[] arr, int maxIndex) {
            this.arr = arr;
            this.maxIndex = maxIndex;
        }

        @Override
        public boolean hasNext() {
            return currIndex + 1 <= maxIndex;
        }

        @Override
        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return arr[currIndex++];
        }
    }

    private Board createBoard(int oldX, int oldY, int newX, int newY) {
        int[][] copy = new int[this.dimension()][this.dimension()];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        copy[oldX][oldY] = copy[newX][newY];
        copy[newX][newY] = 0;
        return new Board(copy);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // find the 0
        // check for left, right, top, down items
        // create a new board with such changes
        int[] index = findZero();
        int indexI = index[0];
        int indexJ = index[1];

        Board[] neighBoards = new Board[4];
        int numNeighbours = 0;
        // Top swap
        if (indexI - 1 >= 0) {
            Board newBoard = createBoard(indexI, indexJ, indexI - 1, indexJ);
            neighBoards[numNeighbours] = newBoard;
            numNeighbours += 1;
        }
        // Bottom swap
        if (indexI + 1 < this.board.length) {
            Board newBoard = createBoard(indexI, indexJ, indexI + 1, indexJ);
            neighBoards[numNeighbours] = newBoard;
            numNeighbours += 1;
        }
        // Left swap
        if (indexJ - 1 >= 0) {
            Board newBoard = createBoard(indexI, indexJ, indexI, indexJ - 1);
            neighBoards[numNeighbours] = newBoard;
            numNeighbours += 1;
        }
        // Right swap
        if (indexJ + 1 < this.board.length) {
            Board newBoard = createBoard(indexI, indexJ, indexI, indexJ + 1);
            neighBoards[numNeighbours] = newBoard;
            numNeighbours += 1;
        }
        int finalNumNeighbours = numNeighbours;
        return () -> new BoardIterator(neighBoards, finalNumNeighbours);
    }

    public Board twin() {
        int[][] copy = new int[this.dimension()][this.dimension()];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        boolean valid = false;
        int x = 0;
        int y = 0;
        while (!valid) {
            if (x + 1 < dimension()) {
                if (copy[y][x + 1] != 0 && copy[y][x] != 0) {
                    int old = copy[y][x];
                    copy[y][x] = copy[y][x + 1];
                    copy[y][x + 1] = old;
                    valid = true;
                } else {
                    x += 1;
                }
            } else {
                // go to next row
                y += 1;
                x = 0;
            }
        }
        return new Board(copy);
    }


    // unit testing (not graded)
    public static void main(String[] args) {
        Board main = new Board(new int[][]{{5, 0, 4}, {2, 3, 8}, {7, 1, 6}});
        Board twin = main.twin();
        System.out.println(main);
        System.out.println("TWIN");
        System.out.println(twin);
        System.out.println(twin.equals(main.twin()));
    }

}