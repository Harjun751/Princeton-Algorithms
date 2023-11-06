import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CircularSuffixArrayTest {
    @Test
    void testPeriodicStringIndex(){
        CircularSuffixArray csa = new CircularSuffixArray("couscous");
        for (int i = 0; i < "couscous".length(); i++){
            System.out.println(csa.index(i));
        }
    }

    @Test
    void testPeriodicStringIndex2(){
        CircularSuffixArray csa = new CircularSuffixArray("*************");
        for (int i = 0; i < "*************".length(); i++){
            System.out.println(csa.index(i));
        }
    }

    @Test
    void testLongStr(){
        String str = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        CircularSuffixArray csa = new CircularSuffixArray(str);
        for (int i = 0; i < str.length(); i++){
            System.out.println(csa.index(i));
        }
    }

    @Test
    void testRandom(){
        CircularSuffixArray csa = new CircularSuffixArray("BAB");
        for (int i = 0; i < "BAB".length(); i++){
            System.out.println(csa.index(i));
        }
    }

    @Test
    void testAbra(){
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        assertEquals(12, csa.length());
        assertEquals(7, csa.index(2));
        assertEquals(5, csa.index(5));
        assertEquals(8, csa.index(6));
    }
    @Test
    void testMismatch(){
        CircularSuffixArray csa = new CircularSuffixArray("AABABBBBAB");
        assertEquals(9, csa.index(4));
    }
    @Test
    void testMismatch2(){
        CircularSuffixArray csa = new CircularSuffixArray("BBBBBBABBAABBBABBBABABAABBAAAABBABBBBBBAABABBBBBAA");
        assertEquals(32, csa.index(16));
    }

    @Test
    void testCSATimeTrial() throws IOException {
        String file = new String(Files.readAllBytes(Path.of("inputs/dickens.txt")));
        long start = System.nanoTime();
        CircularSuffixArray csa = new CircularSuffixArray(file);
        long end = System.nanoTime();
        System.out.printf("Elapsed time: %.2f ms", (end-start)/1000000.0);
    }

}