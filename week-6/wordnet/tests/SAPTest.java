import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
class SAPTest {

    @Test
    void testDigraph1_1() {
        In in = new In("inputs/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 5;
        int w = 7;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(3, length);
        assertEquals(1, ancestor);
    }

    @Test
    void testDigraph1_2() {
        In in = new In("inputs/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 3;
        int w = 3;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(0, length);
    }

    @Test
    void testDigraph1_cache_test() {
        In in = new In("inputs/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 5;
        int w = 7;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        sapCopy.length(w, v);

        assertEquals(3, length);
        assertEquals(1, ancestor);
    }

    @Test
    void testDigraph1_cache_test2() {
        In in = new In("inputs/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        Integer[] v = new Integer[] { 5 };
        Integer[] w = new Integer[] { 7 };

        sapCopy.length(5, 7);
        sapCopy.ancestor(5, 7);

        int length = sapCopy.length(List.of(v), List.of(w));
        int ancestor = sapCopy.ancestor(List.of(v), List.of(w));

        assertEquals(3, length);
        assertEquals(1, ancestor);
    }

    @Test
    void testDigraph2_1() {
        In in = new In("inputs/digraph2.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 2;
        int w = 0;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(4, length);
        assertEquals(0, ancestor);
    }

    @Test
    void testDigraph3_1() {
        In in = new In("inputs/digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 0;
        int w = 9;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(3, length);
        assertEquals(11, ancestor);
    }

    @Test
    void testDigraph3_2() {
        In in = new In("inputs/digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 10;
        int w = 3;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(-1, length);
    }

    @Test
    void testDigraph4_1() {
        In in = new In("inputs/digraph4.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 7;
        int w = 3;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(5, length);
        assertEquals(6, ancestor);
    }

    @Test
    void testDigraph4_2() {
        In in = new In("inputs/digraph4.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 0;
        int w = 6;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(2, length);
        assertEquals(6, ancestor);
    }

    @Test
    void testDigraph5_1() {
        In in = new In("inputs/digraph5.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 0;
        int w = 8;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(2, length);
    }

    @Test
    void testDigraph5_2() {
        In in = new In("inputs/digraph5.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 5;
        int w = 16;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(-1, length);
    }

    @Test
    void testDigraph6_1() {
        In in = new In("inputs/digraph6.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 7;
        int w = 2;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(2, length);
        assertEquals(3, ancestor);
    }

    @Test
    void testDigraph6_2() {
        In in = new In("inputs/digraph6.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 6;
        int w = 4;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(3, length);
        assertEquals(4, ancestor);
    }

    @Test
    void testDigraph9_1() {
        In in = new In("inputs/digraph9.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 7;
        int w = 0;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(2, length);
        assertEquals(6, ancestor);
    }

    @Test
    void testDigraph9_2() {
        In in = new In("inputs/digraph9.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 4;
        int w = 1;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);

        assertEquals(1, length);
    }

    @Test
    void testWordnetDigraph_1() {
        In in = new In("inputs/digraph-wordnet.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 1745;
        int w = 4090;

        long start = System.nanoTime();
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        long end = System.nanoTime();
        System.out.printf("Elapsed Time %.2f ms", (end - start) / 1000000.0);

        assertEquals(4, length);
        assertEquals(42539, ancestor);
    }

    @Test
    void testDigraph1_3() {
        In in = new In("inputs/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 6;
        int w = 6;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(0, length);
        assertEquals(6, ancestor);
    }

    @Test
    void testDigraph3_3() {
        In in = new In("inputs/digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 7;
        int w = 5;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(-1, length);
    }

    @Test
    void testDigraph3_4() {
        In in = new In("inputs/digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 13;
        int w = 9;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(5, length);
        assertEquals(11, ancestor);
    }

    @Test
    void testDigraph3_5() {
        In in = new In("inputs/digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 8;
        int w = 13;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(5, length);
    }

    @Test
    void testDigraph4_3() {
        In in = new In("inputs/digraph4.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 1;
        int w = 4;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(3, length);
    }


    @Test
    void testDigraph5_3() {
        In in = new In("inputs/digraph5.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 17;
        int w = 21;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(5, length);
    }


    @Test
    void testDigraph6_3() {
        In in = new In("inputs/digraph6.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 5;
        int w = 1;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(4, length);
    }

    @Test
    void testDigraph6_4() {
        In in = new In("inputs/digraph6.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 0;
        int w = 5;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(5, length);
    }

    @Test
    void testDigraph9_3() {
        In in = new In("inputs/digraph9.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 0;
        int w = 4;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(3, length);
    }

    @Test
    void testDigraph9_4() {
        In in = new In("inputs/digraph9.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 4;
        int w = 7;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(3, length);
        assertEquals(4, ancestor);
    }

    @Test
    void testDigraph9_5() {
        In in = new In("inputs/digraph9.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 8;
        int w = 5;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(1, length);
        assertEquals(8, ancestor);
    }

    @Test
    void testDigraph9_6() {
        In in = new In("inputs/digraph9.txt");
        Digraph G = new Digraph(in);
        SAP sapCopy = new SAP(G);
        int v = 2;
        int w = 5;
        int length = sapCopy.length(v, w);
        int ancestor = sapCopy.ancestor(v, w);
        assertEquals(4, length);
        assertEquals(2, ancestor);
    }
}