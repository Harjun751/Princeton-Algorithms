import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoggleSolverTest {
    @Test
    void testCtor(){
        In in = new In("inputs/dictionary-zingarelli2005.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
    }

    @Test
    void testDictionaryScore(){
        In in = new In("inputs/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        assertEquals(0, solver.scoreOf("ZimbleZop"));
        assertEquals(2, solver.scoreOf("Queue"));
        assertEquals(11, solver.scoreOf("ABBREVIATURES"));
        assertEquals(11, solver.scoreOf("ABDICANT"));
        assertEquals(3, solver.scoreOf("ABDABS"));
        assertEquals(5, solver.scoreOf("abelian"));
        assertEquals(1, solver.scoreOf("ABED"));
        assertEquals(2, solver.scoreOf("ABEND"));
        assertEquals(1, solver.scoreOf("ape"));
        assertEquals(0, solver.scoreOf("at"));
        assertEquals(0, solver.scoreOf("a"));
    }

    @Test
    void test4x4Board(){
        In in = new In("inputs/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);

        BoggleBoard board = new BoggleBoard("inputs/board4x4.txt");

        for (String str : solver.getAllValidWords(board)){
            System.out.println(str);
        }
    }

    @Test
    void testQBoard(){
        In in = new In("inputs/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);

        BoggleBoard board = new BoggleBoard("inputs/board-q.txt");

        for (String str : solver.getAllValidWords(board)){
            System.out.println(str);
        }
    }
    @Test
    void test2000PtsBoard(){
        In in = new In("inputs/dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);

        BoggleBoard board = new BoggleBoard("inputs/board-points2000.txt");

        for (String str : solver.getAllValidWords(board)){
            System.out.println(str);
        }
    }

    @Test
    void ABTest4x4Board(){
        In in = new In("inputs/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolverOLD old = new BoggleSolverOLD(dictionary);

        BoggleBoard board = new BoggleBoard("inputs/board4x4.txt");


        SET<String> strSet = new SET<>();
        for (String str : old.getAllValidWords(board)){
            strSet.add(str);
        }

        BoggleSolver solver = new BoggleSolver(dictionary);
        for (String str : solver.getAllValidWords(board)){
            assertTrue(strSet.contains(str));
            strSet.remove(str);
        }
        assertTrue(strSet.isEmpty());
    }

    @Test
    void ABTestQuBoard(){
        In in = new In("inputs/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolverOLD old = new BoggleSolverOLD(dictionary);

        BoggleBoard board = new BoggleBoard("inputs/board-q.txt");

        SET<String> strSet = new SET<>();
        for (String str : old.getAllValidWords(board)){
            strSet.add(str);
        }

        BoggleSolver solver = new BoggleSolver(dictionary);
        for (String str : solver.getAllValidWords(board)){
            assertTrue(strSet.contains(str));
            strSet.remove(str);
        }
        assertTrue(strSet.isEmpty());
    }

    @Test
    void speedTest(){
        In in = new In("inputs/dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver slv = new BoggleSolver(dictionary);
        BoggleSolverOLD old = new BoggleSolverOLD(dictionary);

        long newDuration = 0;
        long oldDuration = 0;
        long startTime;
        long endTime;
        for (int i = 0; i<50; i++){
            BoggleBoard board = new BoggleBoard(4, 4);
            startTime = System.nanoTime();
            slv.getAllValidWords(board);
            endTime = System.nanoTime();
            newDuration += (endTime - startTime);

            startTime = System.nanoTime();
            old.getAllValidWords(board);
            endTime = System.nanoTime();
            oldDuration += (endTime - startTime);
        }

        System.out.println("Old soln runtime: " + oldDuration/1000000000.0 + "s");
        System.out.println("New soln runtime: " + newDuration/1000000000.0 + "s");
        System.out.printf("%.2f%% improvement%n", (double) oldDuration/newDuration);
        assertTrue((double) oldDuration/newDuration > 1);
    }
}