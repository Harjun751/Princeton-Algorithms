import edu.princeton.cs.algs4.BinaryStdIn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CircularSuffixArrayTest {
    @Test
    void testPeriodicStringIndex(){
        CircularSuffixArray csa = new CircularSuffixArray("*************");
        System.out.println(csa.index(1));
    }

    @Test
    void testAbra(){
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        assertEquals(12, csa.length());
        assertEquals(7, csa.index(2));
        assertEquals(5, csa.index(5));
        assertEquals(8, csa.index(6));
    }

}