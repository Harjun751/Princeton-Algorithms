
import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;

    private LineSegment[] segmentStore = null;
    private int nSegments = 0;

    private void sortPoints(Point[] points) {
        int i = 1;

        while (i < points.length) {
            // if point on left is larger than point on right
            while (i >= 1 && points[i - 1].compareTo(points[i]) > 0) {
                // exchange points[i-1] and points[i]
                Point old = points[i - 1];
                points[i - 1] = points[i];
                points[i] = old;
                // reset pointer and do again
                i--;
            }
            i++;
        }
    }

    public BruteCollinearPoints(Point[] points) {
        if (points == null){
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i<points.length; i++){
            for (int j = 0; j<points.length; j++){
                if (i!=j){
                    if (points[i].compareTo(points[j])==0){
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        this.points = Arrays.copyOf(points, points.length);
    }

    public int numberOfSegments() {
        this.segments();
        return nSegments;
    }

    public LineSegment[] segments() {
        if (segmentStore!=null){
            return segmentStore.clone();
        }
        nSegments = 0;
        LineSegment[] segs = new LineSegment[points.length];

        int p = 0;
        int q = 1;
        int r = 2;
        int s = 3;
        int n = points.length;

        // O(N^4) comparisons
        // additionally, there is the sort -> O(4^2)
        // 6 arr for O(N) memory
        while (p < n) {
            while (q < n) {
                while (r < n) {
                    while (s < n) {
                        Point pointP = points[p];
                        Point pointQ = points[q];
                        Point pointR = points[r];
                        Point pointS = points[s];
                        if (pointP.slopeTo(pointQ) == pointP.slopeTo(pointR) && pointP.slopeTo(pointQ) == pointP.slopeTo(pointS)) {
                            Point[] points = new Point[]{pointP, pointQ, pointR, pointS};
                            sortPoints(points);
                            segs[nSegments] = new LineSegment(points[0], points[3]);
                            nSegments++;
                        }
                        s++;
                    }
                    r++;
                    s = r + 1;
                }
                q++;
                r = q + 1;
                s = r + 1;

            }
            p++;
            q = p + 1;
            r = q + 1;
            s = r + 1;
        }

        LineSegment[] fsegments = new LineSegment[nSegments];
        System.arraycopy(segs, 0, fsegments, 0, fsegments.length);
        segmentStore = fsegments;
        return segmentStore.clone();
    }

    public static void main(String[] args) {
    }
}