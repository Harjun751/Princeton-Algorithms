import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testDimensionReturnsCorrectDimensionFor3() {
        int[][] boardArr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(boardArr);
        assertEquals(3, board.dimension());
    }

    @Test
    void testDimensionReturnsCorrectDimensionFor2() {
        int[][] boardArr = {{1, 2}, {3, 0}};
        Board board = new Board(boardArr);
        assertEquals(2, board.dimension());
    }

    @Test
    void testHammingReturnsCorrectVal() {
        int[][] boardArr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(boardArr);
        assertEquals(5, board.hamming());
    }

    @Test
    void testManhattanReturnsCorrectVal() {
        int[][] boardArr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(boardArr);
        assertEquals(10, board.manhattan());
    }

    @Test
    void testIsGoalReturnsTrueForCompletedBoard() {
        int[][] boardArr = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board = new Board(boardArr);
        assertTrue(board.isGoal());
    }

    @Test
    void testIsGoalReturnsFalseForIncompleteBoard() {
        int[][] boardArr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(boardArr);
        assertFalse(board.isGoal());
    }

    @Test
    void testEqualsReturnsTrueForSameBoard() {
        int[][] boardArr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(boardArr);
        Board boardClone = new Board(boardArr);
        assertTrue(board.equals(boardClone));
        assertTrue(boardClone.equals(board));
    }

    @Test
    void testEqualsReturnsFalseForDifferent() {
        int[][] boardArr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(boardArr);
        int[][] boardArr2 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
        Board board2 = new Board(boardArr2);
        assertFalse(board.equals(boardArr2));
        assertFalse(boardArr2.equals(board));
    }

    @Test
    void testNeighboursReturns4Neighbours() {
        int[][] boardArr = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board = new Board(boardArr);

        // Neighbour1
        int[][] neigh1 = {{8, 0, 3}, {4, 1, 2}, {7, 6, 5}};
        Board neighBoard1 = new Board(neigh1);
        // Neighbour2
        int[][] neigh2 = {{8, 1, 3}, {0, 4, 2}, {7, 6, 5}};
        Board neighBoard2 = new Board(neigh2);
        // Neighbour3              blaze it
        int[][] neigh3 = {{8, 1, 3}, {4, 2, 0}, {7, 6, 5}};
        Board neighBoard3 = new Board(neigh3);
        // Neighbour4
        int[][] neigh4 = {{8, 1, 3}, {4, 6, 2}, {7, 0, 5}};
        Board neighBoard4 = new Board(neigh4);

        Board[] expectedNeighBoards = {neighBoard1, neighBoard2, neighBoard3, neighBoard4};
        Iterable<Board> actualNeighBoards = board.neighbors();

        int boardCount = 0;
        // check if boards exit
        for (Board neighBoard : actualNeighBoards) {
            boolean found = false;
            for (Board expBoard : expectedNeighBoards) {
                if (expBoard.equals(neighBoard)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
            boardCount++;
        }
        assertEquals(4, boardCount);
    }

    @Test
    void testNeighboursReturns3Neighbours() {
        int[][] boardArr = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board = new Board(boardArr);

        // Neighbour1
        int[][] neigh1 = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board neighBoard1 = new Board(neigh1);
        // Neighbour2
        int[][] neigh2 = {{1, 2, 3}, {4, 0, 5}, {7, 8, 6}};
        Board neighBoard2 = new Board(neigh2);
        // Neighbour3              blaze it
        int[][] neigh3 = {{1, 3, 0}, {4, 2, 5}, {7, 8, 6}};
        Board neighBoard3 = new Board(neigh3);

        Board[] expectedNeighBoards = {neighBoard1, neighBoard2, neighBoard3};
        Iterable<Board> actualNeighBoards = board.neighbors();

        int boardCount = 0;
        // check if boards exit
        for (Board neighBoard : actualNeighBoards) {
            boolean found = false;
            for (Board expBoard : expectedNeighBoards) {
                if (expBoard.equals(neighBoard)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
            boardCount++;
        }
        assertEquals(3, boardCount);
    }

    @Test
    void testNeighboursReturns2Neighbours() {
        int[][] boardArr = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board board = new Board(boardArr);

        // Neighbour1
        int[][] neigh1 = {{1, 0, 3}, {4, 2, 5}, {7, 8, 6}};
        Board neighBoard1 = new Board(neigh1);
        // Neighbour2
        int[][] neigh2 = {{4, 1, 3}, {0, 2, 5}, {7, 8, 6}};
        Board neighBoard2 = new Board(neigh2);

        Board[] expectedNeighBoards = {neighBoard1, neighBoard2};
        Iterable<Board> actualNeighBoards = board.neighbors();

        int boardCount = 0;
        // check if boards exit
        for (Board neighBoard : actualNeighBoards) {
            boolean found = false;
            for (Board expBoard : expectedNeighBoards) {
                if (expBoard.equals(neighBoard)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
            boardCount++;
        }
        assertEquals(2, boardCount);
    }
}