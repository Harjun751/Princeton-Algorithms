import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DequeTest {

    @Test
    void testIsEmptyReturnsFalseForNewList() {
        Deque<Integer> q = new Deque<>();
        assertTrue(q.isEmpty());
    }

    @Test
    void testSizeReturnsZeroForNewList() {
        Deque<Integer> q = new Deque<>();
        assertEquals(q.size(),0);
    }

    @Test
    void testAddFirstAddsToTheFront() {
        Deque<Integer> q = new Deque<>();
        q.addFirst(5);
        q.addFirst(6);
        int firstItem = q.removeFirst();
        assertEquals(firstItem, 6);
    }


    @Test
    void testAddLastAddsToTheBack() {
        Deque<Integer> q = new Deque<>();
        q.addFirst(5);
        q.addLast(7);
        int lastItem = q.removeLast();
        assertEquals(lastItem, 7);
    }

    @Test
    void testRemoveFirstRemovesFirstItem() {
        Deque<Integer> q = new Deque<>();
        q.addFirst(5);
        q.addLast(8);
        q.addLast(9);
        q.addFirst(4);
        int first = q.removeFirst();
        assertEquals(4, first);
    }

    @Test
    void testShrinkArrayCorrectlyShrinks(){
        Deque<Integer> q = new Deque<>();
        q.addFirst(1);
        q.addFirst(2);
        q.addFirst(3);
        q.addFirst(4);
        // capacity = 4, size=4 -> size=1 required to trigger shrink
        q.removeLast();
        q.removeLast();
        q.removeLast();
        // Should have shrinked by now. Check array size and items.
        assertEquals(1, q.size());
        int last = q.removeLast();
        assertEquals(4,last);
    }

    @Test
    void removeLast() {
        Deque<Integer> q = new Deque<>();
        q.addFirst(5);
        q.addLast(8);
        q.addLast(9);
        q.addFirst(4);
        int last = q.removeLast();
        assertEquals(9, last);
    }

    @Test
    void testIntermixedClassToAddFirstAndRemoveAndIsEmpty(){
        Deque<Integer> q = new Deque<>();
        double pAddFirst = 0.8;
        double pIsEmpty = 0.1;
        double pRemove = 0.1;

        int cycles = 5000;

        for (int i = 0; i<cycles; i++){
            int ranNum = StdRandom.uniformInt(1000);
            if (StdRandom.bernoulli(pAddFirst)) {
                q.addFirst(ranNum);
            } else {
                if (StdRandom.bernoulli(0.5)){
                    q.isEmpty();
                } else {
                    q.removeLast();
                }
            }
        }
    }
}