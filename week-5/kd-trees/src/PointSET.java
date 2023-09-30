import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> pointSet;

    // Create an empty set of points
    public PointSET() {
        this.pointSet = new SET<>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }


    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D pt : pointSet) {
            pt.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> points = new ArrayList<>();
        for (Point2D pt : pointSet) {
            if (rect.contains(pt)) {
                points.add(pt);
            }
        }
        return points;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D champ = null;
        double champDistance = Double.POSITIVE_INFINITY;
        for (Point2D pt : pointSet) {
            double xDiff = p.x() - pt.x();
            double yDiff = p.y() - pt.y();
            double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
            if (distance < champDistance) {
                champDistance = distance;
                champ = pt;
            }
        }
        return champ;
    }

    public static void main(String[] args) {
        PointSET set = new PointSET();
        set.insert(new Point2D(0, 0.5));
        set.insert(new Point2D(0, 0.1));
        set.insert(new Point2D(0.5, 0.5));
        set.insert(new Point2D(0.5, 0.1));

        RectHV rectangle = new RectHV(0.25, 0.1, 0.6, 0.6);
        set.draw();

        for (Point2D pt : set.range(rectangle)) {
            System.out.println(pt);
        }

        Point2D resident = new Point2D(0, 0.05);
        Point2D neighbour = set.nearest(resident);
        System.out.println("Nearest neighbour: " + neighbour);
    }
}