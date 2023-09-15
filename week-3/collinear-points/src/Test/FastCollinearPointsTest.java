import edu.princeton.cs.algs4.StdRandom;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FastCollinearPointsTest {
    @Test
    void testSegmentsReturnsListOfSegment(){
        Point p1 = new Point(1,1);
        Point p2 = new Point(2,2);
        Point p3 = new Point(3,3);
        Point p4 = new Point(4,4);
        Point p5 = new Point(1023,1232);
        Point[] points = new Point[] { p1,p2, p3, p4, p5 };

        FastCollinearPoints FCP = new FastCollinearPoints(points);
        LineSegment[] segments = FCP.segments();

        assertEquals(1, FCP.numberOfSegments());

        LineSegment segment = segments[0];
        LineSegment nSegment = new LineSegment(p1, p4);
        assertTrue(Objects.equals(segment.toString(), nSegment.toString()));
        LineSegment fSegment = new LineSegment(p1,p5);
        assertFalse(Objects.equals(segment.toString(), fSegment.toString()));
    }

    @Test
    void testNinetyRandomPoints(){
        // 90 random points in a 10-by-10 grid
        int x=0;
        int y=0;
        Point[] points = new Point[90];
        for (int i = 0; i < 90; i++){
            Point point;
            if (StdRandom.bernoulli(0.5)){
                point = new Point(x++, y);
            } else {
                point = new Point(x, y++);
            }
            points[i] = point;
        }

        FastCollinearPoints FCP = new FastCollinearPoints(points);

        System.out.println(FCP.numberOfSegments());
        System.out.println("hi");
    }

    @Test
    void testImmutabilityOfFCP(){
        Point p1 = new Point(1,1);
        Point p2 = new Point(2,2);
        Point p3 = new Point(3,3);
        Point p4 = new Point(4,4);
        Point p5 = new Point(1023,1232);
        Point[] points = new Point[] { p1,p2, p3, p4, p5 };

        FastCollinearPoints FCP = new FastCollinearPoints(points);
        LineSegment[] segments = FCP.segments();

        assertEquals(1, FCP.numberOfSegments());

        FCP.numberOfSegments();
        FCP.segments();
        FCP.numberOfSegments();
        FCP.numberOfSegments();
        FCP.numberOfSegments();
        LineSegment[] segments2 = FCP.segments();
        assertEquals(segments2, segments);
    }

    @Test
    void testSegmentsReturnsListOfSegments(){
        Point p1 = new Point(1,1);
        Point p2 = new Point(2,2);
        Point p3 = new Point(3,3);
        Point p4 = new Point(4,4);
        Point p5 = new Point(10,20);
        Point p6 = new Point(20,40);
        Point p7 = new Point(30,60);
        Point p8 = new Point(40,80);
        Point p9 = new Point(1023,1232);
        Point[] points = new Point[] { p1,p2, p3, p4, p5, p6, p7, p8 };

        FastCollinearPoints FCP = new FastCollinearPoints(points);
        LineSegment[] segments = FCP.segments();

        assertEquals(2, FCP.numberOfSegments());

        LineSegment segment = segments[1];
        LineSegment nSegment = new LineSegment(p5, p8);
        assertTrue(Objects.equals(segment.toString(), nSegment.toString()));
        LineSegment fSegment = new LineSegment(p1,p5);
        assertFalse(Objects.equals(segment.toString(), fSegment.toString()));
    }

    @Test
    void testSegmentsReturnsListOfSegmentsWithShuffledInput(){
        Point p1 = new Point(1,1);
        Point p2 = new Point(2,2);
        Point p3 = new Point(3,3);
        Point p4 = new Point(4,4);
        Point p5 = new Point(10,20);
        Point p6 = new Point(20,40);
        Point p7 = new Point(30,60);
        Point p8 = new Point(40,80);
        Point p9 = new Point(1023,1232);
        Point[] points = new Point[] { p1,p2, p3, p4, p5, p6, p7, p8 };
        StdRandom.shuffle(points);

        FastCollinearPoints FCP = new FastCollinearPoints(points);

        LineSegment[] segments = FCP.segments();

        assertEquals(2, FCP.numberOfSegments());

        String[] output = new String[2];
        for (int i = 0; i<2; i++){
            output[i] = segments[i].toString();
        }

        String output1 = new LineSegment(p1,p4).toString();
        String output2 = new LineSegment(p5,p8).toString();
        assertTrue(Arrays.stream(output).anyMatch(output1::contains));
        assertTrue(Arrays.stream(output).anyMatch(output2::contains));
    }

    @Test
    void testSegmentsReturnsEmptyListOfSegments(){
        Point p1 = new Point(1123,12000);
        Point p2 = new Point(1892,-23);
        Point p3 = new Point(6666,1231);
        Point p4 = new Point(329,923);
        Point p5 = new Point(1023,1232);
        Point[] points = new Point[] { p1,p2, p3, p4, p5 };

        FastCollinearPoints FCP = new FastCollinearPoints(points);
        LineSegment[] segments = FCP.segments();

        assertEquals(0, FCP.numberOfSegments());
    }

    @Test
    void testSegmentsReturnsListOfBiggerSegment(){
        Point p1 = new Point(1,1);
        Point p2 = new Point(2,2);
        Point p3 = new Point(3,3);
        Point p4 = new Point(4,4);
        Point p10 = new Point(5,5);
        Point p5 = new Point(10,20);
        Point p6 = new Point(20,40);
        Point p7 = new Point(30,60);
        Point p8 = new Point(40,80);
        Point p9 = new Point(1023,1232);
        Point[] points = new Point[] { p1,p2, p3, p4, p5, p6, p7, p8, p10 };
        StdRandom.shuffle(points);

        FastCollinearPoints FCP = new FastCollinearPoints(points);

        LineSegment[] segments = FCP.segments();

        assertEquals(2, FCP.numberOfSegments());

        String[] output = new String[2];
        for (int i = 0; i<2; i++){
            output[i] = segments[i].toString();
        }

        String output1 = new LineSegment(p1,p10).toString();
        String output2 = new LineSegment(p5,p8).toString();
        assertTrue(Arrays.stream(output).anyMatch(output1::contains));
        assertTrue(Arrays.stream(output).anyMatch(output2::contains));
    }

    @Test
    void testSegmentsWithInput8(){
        Point p1 = new Point(10000,0);
        Point p2 = new Point(0,10000);
        Point p3 = new Point(3000,7000);
        Point p4 = new Point(7000,3000);
        Point p5 = new Point(20000,21000);
        Point p6 = new Point(3000,4000);
        Point p7 = new Point(14000,15000);
        Point p8 = new Point(6000,7000);
        Point[] points = new Point[] { p1,p2, p3, p4, p5,p6,p7,p8 };

        FastCollinearPoints FCP = new FastCollinearPoints(points);
        LineSegment[] segments = FCP.segments();

        assertEquals(2, FCP.numberOfSegments());
    }@Test
    void testSegmentsWithInput9(){
        Point p1 = new Point(4000, 30000);
        Point p2 = new Point(3500, 28000);
        Point p3 = new Point(3000, 26000);
        Point p4 = new Point(2000, 22000);
        Point p5 = new Point(1000, 18000);
        Point p6 = new Point(13000, 21000);
        Point p7 = new Point(23000, 16000);
        Point p8 = new Point(28000, 13500);
        Point p9 = new Point(28000,  5000);
        Point p10 = new Point(28000,  1000);
        Point[] points = new Point[] { p1,p2, p3, p4, p5,p6,p7,p8, p9, p10};

        FastCollinearPoints FCP = new FastCollinearPoints(points);
        LineSegment[] segments = FCP.segments();

        assertEquals(2, FCP.numberOfSegments());
    }

}