import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class BruteCollinearPointsTest {
    @Test
    void testSegmentsReturnsListOfSegment() {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Point p3 = new Point(3, 3);
        Point p4 = new Point(4, 4);
        Point p5 = new Point(1023, 1232);
        Point[] points = new Point[]{p1, p2, p3, p4, p5};

        BruteCollinearPoints BCP = new BruteCollinearPoints(points);
        LineSegment[] segments = BCP.segments();

        assertEquals(1, BCP.numberOfSegments());

        LineSegment segment = segments[0];
        LineSegment nSegment = new LineSegment(p1, p4);
        assertTrue(Objects.equals(segment.toString(), nSegment.toString()));
        LineSegment fSegment = new LineSegment(p1, p5);
        assertFalse(Objects.equals(segment.toString(), fSegment.toString()));
    }

    @Test
    void testSegmentsReturnsListOfSegments() {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Point p3 = new Point(3, 3);
        Point p4 = new Point(4, 4);
        Point p5 = new Point(10, 20);
        Point p6 = new Point(20, 40);
        Point p7 = new Point(30, 60);
        Point p8 = new Point(40, 80);
        Point p9 = new Point(1023, 1232);
        Point[] points = new Point[]{p1, p2, p3, p4, p5, p6, p7, p8};

        BruteCollinearPoints BCP = new BruteCollinearPoints(points);
        LineSegment[] segments = BCP.segments();

        assertEquals(2, BCP.numberOfSegments());

        LineSegment segment = segments[1];
        LineSegment nSegment = new LineSegment(p5, p8);
        assertTrue(Objects.equals(segment.toString(), nSegment.toString()));
        LineSegment fSegment = new LineSegment(p1, p5);
        assertFalse(Objects.equals(segment.toString(), fSegment.toString()));
    }

    @Test
    void testSegmentsReturnsListOfSegmentsWithShuffledInput() {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(2, 2);
        Point p3 = new Point(3, 3);
        Point p4 = new Point(4, 4);
        Point p5 = new Point(10, 20);
        Point p6 = new Point(20, 40);
        Point p7 = new Point(30, 60);
        Point p8 = new Point(40, 80);
        Point p9 = new Point(1023, 1232);
        Point[] points = new Point[]{p1, p2, p3, p4, p5, p6, p7, p8};
        StdRandom.shuffle(points);

        BruteCollinearPoints BCP = new BruteCollinearPoints(points);

        assertEquals(2, BCP.numberOfSegments());

        LineSegment[] segments = BCP.segments();

        String[] output = new String[2];
        for (int i = 0; i < 2; i++) {
            output[i] = segments[i].toString();
        }

        String output1 = new LineSegment(p1, p4).toString();
        String output2 = new LineSegment(p5, p8).toString();
        assertTrue(Arrays.stream(output).anyMatch(output1::contains));
        assertTrue(Arrays.stream(output).anyMatch(output2::contains));
    }

    @Test
    void testSegmentsReturnsEmptyListOfSegments() {
        Point p1 = new Point(1123, 12000);
        Point p2 = new Point(1892, -23);
        Point p3 = new Point(6666, 1231);
        Point p4 = new Point(329, 923);
        Point p5 = new Point(1023, 1232);
        Point[] points = new Point[]{p1, p2, p3, p4, p5};

        BruteCollinearPoints BCP = new BruteCollinearPoints(points);
        LineSegment[] segments = BCP.segments();

        assertEquals(0, BCP.numberOfSegments());
    }

    @Test
    void testSegmentsWithInput8() {
        Point p1 = new Point(10000, 0);
        Point p2 = new Point(0, 10000);
        Point p3 = new Point(3000, 7000);
        Point p4 = new Point(7000, 3000);
        Point p5 = new Point(20000, 21000);
        Point p6 = new Point(3000, 4000);
        Point p7 = new Point(14000, 15000);
        Point p8 = new Point(6000, 7000);
        Point[] points = new Point[]{p1, p2, p3, p4, p5, p6, p7, p8};

        BruteCollinearPoints BCP = new BruteCollinearPoints(points);
        LineSegment[] segments = BCP.segments();

        assertEquals(2, BCP.numberOfSegments());
    }
}