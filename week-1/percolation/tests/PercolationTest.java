import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PercolationTest {
    @Test
    void testIsFullFor1SiteOpened(){
        Percolation perc = new Percolation(6);
        perc.open(1, 6);
        assertTrue(perc.isFull(1, 6));
    }
    @Test
    void testIsFullFor0SiteOpenedCornerCase(){
        Percolation perc = new Percolation(1);
        assertFalse(perc.percolates());
    }

}