import java.util.Arrays;

public class FastCollinearPoints {
    private int numberOfSegments = 0;
    private final Point[] points;
    private LineSegment[] segmentsStore = null;

    private static class Slope implements Comparable<Slope> {
        public Point point;
        public double slope;

        public Slope(Point pt, double slope) {
            this.point = pt;
            this.slope = slope;
        }

        @Override
        public int compareTo(Slope o) {
            return Double.compare(this.slope, o.slope);
        }
    }

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    if (points[i].compareTo(points[j]) == 0) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        Point[] aux = Arrays.copyOf(points, points.length);
        Merge.sort(aux);
        this.points = aux;
    }

    public int numberOfSegments() {
        this.segments();
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        if (segmentsStore != null) {
            return segmentsStore.clone();
        }
        numberOfSegments = 0;
        // n**2logn
        // max amount of segments?
        int n = points.length;
        int max_segment = ((n * n) - (2 * n - 1)) / 4;
        LineSegment[] segments = new LineSegment[max_segment];
        int j = 1;

        // iterate through all items of loop
        for (int i = 0; i < points.length; i++) {
            Point origin = points[i];

            // create a slope array containing all other eles
            Slope[] slopeArray = new Slope[points.length - j];
            int index = 0;
            while (j < points.length) {
                Slope nSlope = new Slope(points[j], origin.slopeTo(points[j]));
                slopeArray[index] = nSlope;
                j++;
                index++;
            }
            // Sorts slope array
            Merge.sort(slopeArray);

            // Check for samey slopes
            // Create the line segment for those slopes
            // add to segments
            int k = 0;
            double[] prevSlopes = new double[i];
            // Creates a slope with the previous element
            // to check for repeats
            if (i >= 1) {
                for (int x = 0; x < i; x++) {
                    prevSlopes[x] = origin.slopeTo(points[x]);
                }
            }
            // changed slopearray.length-2 to slopearray.length-i
            while (k < slopeArray.length - 2) {
                int pointLen = 1;
                double currSlope = slopeArray[k].slope;
                // checks if the current slope is the same as the others
                while (k <= slopeArray.length - 2 && currSlope == slopeArray[k + 1].slope) {
                    pointLen++;
                    k++;
                }
                if (pointLen >= 3) {
                    boolean carryOn = true;
                    for (double sl : prevSlopes) {
                        if (slopeArray[k].slope == sl) {
                            carryOn = false;
                            break;
                        }
                    }
                    if (carryOn) {
                        Point end = slopeArray[k].point;
                        LineSegment seg = new LineSegment(origin, end);
                        segments[numberOfSegments] = seg;
                        numberOfSegments++;
                    }
                }
                k++;
            }
            j = i + 1;
        }
        LineSegment[] fsegments = new LineSegment[numberOfSegments];
        System.arraycopy(segments, 0, fsegments, 0, fsegments.length);
        segmentsStore = fsegments;
        return segmentsStore.clone();
    }

    private static class Merge {
        private static <T extends Comparable<T>> void sort(T[] arr) {
            T[] aux = (T[]) new Comparable[arr.length];
            sort(arr, aux, 0, arr.length - 1);
        }

        private static <T extends Comparable<T>> void merge(T[] arr, T[] aux, int lo, int mid, int hi) {
            // copy array
            if (hi + 1 - lo >= 0) System.arraycopy(arr, lo, aux, lo, hi + 1 - lo);

            int j = mid + 1;
            int i = lo;
            for (int k = lo; k <= hi; k++) {
                if (i > mid) {
                    arr[k] = aux[j++];
                } else if (j > hi) {
                    arr[k] = aux[i++];
                } else if (aux[j].compareTo(aux[i]) <= -1) {
                    // if auxj is lower than aux i
                    arr[k] = aux[j++];
                } else {
                    arr[k] = aux[i++];
                }
            }
        }

        private static <T extends Comparable<T>> void sort(T[] arr, T[] aux, int lo, int hi) {
            if (lo >= hi) {
                return;
            }

            int mid = (lo + hi) / 2;

            sort(arr, aux, lo, mid);
            sort(arr, aux, mid + 1, hi);

            merge(arr, aux, lo, mid, hi);
        }
    }

    public static void main(String[] args) {
    }
}