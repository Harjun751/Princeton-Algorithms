import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void compareLowerPointReturns1() {
        Point current = new Point(5,10);
        Point lower = new Point(6,3);

        int result = current.compareTo(lower);

        assertEquals(1,result);
    }
    @Test
    void compareHigherPointReturnsNeg1() {
        Point current = new Point(5,10);
        Point higher = new Point(1,11);

        int result = current.compareTo(higher);

        assertEquals(-1,result);
    }
    @Test
    void compareEqualYHigherXReturnsNeg1() {
        Point current = new Point(5,10);
        Point equalYHigherX = new Point(6,10);

        int result = current.compareTo(equalYHigherX);

        assertEquals(-1,result);
    }
    @Test
    void compareEqualYLowerXReturns1() {
        Point current = new Point(5,10);
        Point equalYLowerX = new Point(4,10);

        int result = current.compareTo(equalYLowerX);

        assertEquals(1,result);
    }

    @Test
    void compareEqualYEqualXReturns0() {
        Point current = new Point(5,10);
        Point equal = new Point(5,10);

        int result = current.compareTo(equal);

        assertEquals(0,result);
    }


    @Test
    void slopeReturnsCorrectPositiveSlope() {
        Point current = new Point(0,0);
        Point end = new Point(5,10);

        // y1-y0 / x1-x0
        // 10-0 / 5 - 0 = 2.0

        double result = current.slopeTo(end);

        assertEquals(2.0,result);
    }
    @Test
    void slopeReturnsCorrectNegSlope() {
        Point current = new Point(0,0);
        Point end = new Point(23,-124);

        // y1-y0 / x1-x0
        // -124 / 23

        double result = current.slopeTo(end);

        assertEquals((-124.0/23.0),result);
    }
    @Test
    void slopeReturns0ForHorizontalLine() {
        Point current = new Point(13,7);
        Point end = new Point(20,7);

        // y1-y0 / x1-x0
        // 7-7 / 20-13 = 0

        double result = current.slopeTo(end);

        assertEquals(0,result);
    }
    @Test
    void slopeReturnsInfForVerticalLine() {
        Point current = new Point(14,7);
        Point end = new Point(14,0);

        // y1-y0 / x1-x0
        // 7 / 0 = inf

        double result = current.slopeTo(end);

        assertEquals(Double.POSITIVE_INFINITY,result);
    }
    @Test
    void slopeReturnsNegInfForPoint() {
        Point current = new Point(13,7);
        Point end = new Point(13,7);


        double result = current.slopeTo(end);

        assertEquals(Double.NEGATIVE_INFINITY,result);
    }

    @Test
    void slopeOrderReturns1IfFirstArgIsGreater() {
        Point first = new Point(20,40);
        Point second = new Point(15,15);
        Point checker = new Point(0,0);

        Comparator<Point> slopeOrder = checker.slopeOrder();

        int result = slopeOrder.compare(first,second);

        assertEquals(1,result);
    }
    @Test
    void slopeOrderReturnsNeg1IfFirstArgIsSmaller() {
        Point first = new Point(12,2);
        Point second = new Point(15,15);
        Point checker = new Point(0,0);

        Comparator<Point> slopeOrder = checker.slopeOrder();

        int result = slopeOrder.compare(first,second);

        assertEquals(-1,result);
    }
    @Test
    void slopeOrderReturns0IfArgsEqual() {
        Point first = new Point(20,20);
        Point second = new Point(20,20);
        Point checker = new Point(0,0);

        Comparator<Point> slopeOrder = checker.slopeOrder();

        int result = slopeOrder.compare(first,second);

        assertEquals(0,result);
    }
    @Test
    void slopeOrderReturns1IfYEqualXHigher() {
        // Slope is steeper in first point, hence first is higher
        // even though first point is technically lesser than second
        Point first = new Point(19,20);
        Point second = new Point(20,20);
        assertEquals(first.compareTo(second), -1);
        Point checker = new Point(0,0);

        Comparator<Point> slopeOrder = checker.slopeOrder();

        int result = slopeOrder.compare(first,second);

        assertEquals(1,result);
    }

    @Test
    void slopeOrderWorksInNegative(){
        Point checker = new Point(-5, -5);

        Point leftPoint = new Point(-10, -10);
        Point rightPoint = new Point(0, -3);

        Comparator<Point> slopeOrder = checker.slopeOrder();

        int result = slopeOrder.compare(leftPoint,rightPoint);

        assertEquals(1,result);
    }

    @Test
    void slopeOrderComparesInfinities(){
        Point checker = new Point(0,0);

        Point firstPoint = new Point(0,100);
        Point secondPoint = new Point(0,0);

        Comparator<Point> slopeOrder = checker.slopeOrder();

        int result = slopeOrder.compare(firstPoint, secondPoint);

        assertEquals(1,result);
    }

}