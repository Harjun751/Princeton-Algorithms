import edu.princeton.cs.algs4.BinaryStdIn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircularSuffixArrayTest {
    @Test
    void testPeriodicStringIndex(){
        CircularSuffixArray csa = new CircularSuffixArray("couscous");
        for (int i = 0; i < "couscous".length(); i++){
            System.out.println(csa.index(i));
        }
    } @Test
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

}