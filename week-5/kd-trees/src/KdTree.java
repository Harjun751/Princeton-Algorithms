import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D pt, RectHV rect) {
            this.p = pt;
            this.rect = rect;
        }

        public double getX(Node n) {
            return n.p.x();
        }

        public double getY(Node n) {
            return n.p.y();
        }
    }


    // Create an empty set of points
    public KdTree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }


    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (contains(p)) {
            return;
        }
        put(root, true, p, 0, 0, 1, 1);
        size += 1;
    }

    private Node put(Node node, Boolean compareX, Point2D p, double xmin, double ymin, double xmax, double ymax) {
        if (node == null || node.p == null) {
            RectHV rect;
            if (!compareX) {
                rect = new RectHV(xmin, ymin, xmax, ymax);
            } else {
                rect = new RectHV(xmin, ymin, xmax, ymax);
            }
            Node created = new Node(p, rect);
            if (root == null) {
                root = created;
            }
            return created;
        }

        double thisComparator;
        double pointComparator;
        if (compareX) {
            thisComparator = node.p.x();
            pointComparator = p.x();
        } else {
            thisComparator = node.p.y();
            pointComparator = p.y();
        }
        // if the point to be inserted has a smaller x-coordinate than the point at the root, go left; otherwise go right
        if (pointComparator < thisComparator) {
            // invert compare x as the level goes down
            if (compareX) {
                // if the point is TO THE LEFT
                // lets assume ymin, etc. is updated
                node.lb = put(node.lb, !compareX, p, xmin, ymin, node.getX(node), ymax);
            } else {
                // if the point is BELOW
                node.lb = put(node.lb, !compareX, p, xmin, ymin, xmax, node.getY(node));
            }
        } else {
            if (compareX) {
                // if point is to THE RIGHT
                node.rt = put(node.rt, !compareX, p, node.getX(node), ymin, xmax, ymax);
            } else {
                // if point is ABOVE
                node.rt = put(node.rt, !compareX, p, xmin, node.getY(node), xmax, ymax);
            }
        }

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return search(root, true, p);
    }

    private boolean search(Node node, Boolean compareX, Point2D p) {
        if (node == null || node.p == null) {
            return false;
        }

        boolean isInRight;
        boolean isInLeft;

        double thisComparator;
        double pointComparator;
        if (compareX) {
            thisComparator = node.p.x();
            pointComparator = p.x();
        } else {
            thisComparator = node.p.y();
            pointComparator = p.y();
        }
        if (pointComparator < thisComparator) {
            isInLeft = search(node.lb, !compareX, p);
            if (isInLeft) {
                return true;
            }
        } else if (pointComparator > thisComparator) {
            isInRight = search(node.rt, !compareX, p);
            if (isInRight) {
                return true;
            }
        } else {
            // if point is bordering, need to check both sides
            if (node.p.x() == p.x() && node.p.y() == p.y()) {
                return true;
            }
            isInLeft = search(node.lb, !compareX, p);
            if (isInLeft) {
                return true;
            }
            isInRight = search(node.rt, !compareX, p);
            if (isInRight) {
                return true;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        drawR(root, true);
    }

    private void drawR(Node node, Boolean compareX) {
        if (node == null) {
            return;
        }
        if (node.p != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();
            if (compareX) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
        }

        drawR(node.lb, !compareX);
        drawR(node.rt, !compareX);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> points = new ArrayList<>();
        searchRect(root, rect, points);
        return points;
    }

    private boolean isInRectangle(Point2D pt, RectHV rect) {
        if (pt.x() <= rect.xmax() && pt.x() >= rect.xmin()) {
            return pt.y() <= rect.ymax() && pt.y() >= rect.ymin();
        }
        return false;
    }

    private void searchRect(Node node, RectHV query, ArrayList<Point2D> arr) {
        if (node == null || node.p == null) {
            return;
        }
        if (isInRectangle(node.p, query)) {
            arr.add(node.p);
        }

        // check if left subtree intersects
        if (node.lb != null) {
            if (node.lb.rect.intersects(query)) {
                searchRect(node.lb, query, arr);
            }
        }
        if (node.rt != null) {
            if (node.rt.rect.intersects(query)) {
                searchRect(node.rt, query, arr);
            }
        }
    }

    // TODO: A less inefficient nearest
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return searchNearest(root, true, p, Double.POSITIVE_INFINITY);
    }

    private double calcDistance(Point2D thisPoint, Point2D thatPoint) {
        double xdiff = thatPoint.x() - thisPoint.x();
        double ydiff = thatPoint.y() - thisPoint.y();
        return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
    }
    private Point2D searchNearest(Node node, boolean compareX, Point2D query, double champDistance){
        // FUCKING ALGO AGAIN
        // if null, return

        // calculate distance for this point
            // set champion distance if so

        // FOLLOW SPLIT ALWAYS
        // compare query point with this node
        // set FOLLOW node, and OPPOSITE node
        // search FOLLOW node for nearest point
        // calculate BEST CASE SCENARIO for OPPOSITE node
            // if opposite node may have a BETTER POINT, search opposite node
        // COMPARE OPPOSITE BEST, FOLLOW BEST
        // return appropriate best
        if (node==null){
            return null;
        }

        double distance = calcDistance(node.p, query);
        if (distance < champDistance){
            champDistance = distance;
        }

        int cmp = comparePoints(query, node.p, compareX);

        Node followNode = null;
        Node oppositeNode = null;
        if (cmp<=0){
            if (node.lb!=null){
                followNode = node.lb;
            }
            if (node.rt!=null){
                oppositeNode = node.rt;
            }
        } else if (cmp > 0){
            if (node.rt!=null){
                followNode = node.rt;
            }
            if (node.lb!=null){
                oppositeNode = node.lb;
            }
        }

        Point2D followNearest = null;
        Point2D oppositeNearest = null;
        double followDistance = Double.POSITIVE_INFINITY;
        double oppositeDistance = Double.POSITIVE_INFINITY;
        if (followNode!=null){
            followNearest = searchNearest(followNode, !compareX, query, champDistance);
            followDistance = calcDistance(followNearest, query);
        }
        if (oppositeNode!=null){
            Point2D bestCaseScenario;
            if (compareX){
                bestCaseScenario = new Point2D(node.getX(node), query.y());
            } else {
                bestCaseScenario = new Point2D(query.x(), node.getY(node));
            }
            double bestCaseDist = calcDistance(bestCaseScenario, query);

            // TODO CHECK IF CHAMPDISTANCE IS WHAT I SHOULD BE USING HERE.
            if (followDistance < champDistance){
                oppositeNearest = searchNearest(oppositeNode, !compareX, query, followDistance);
                oppositeDistance = calcDistance(oppositeNearest, query);
            } else if (bestCaseDist < champDistance){
                oppositeNearest = searchNearest(oppositeNode, !compareX, query, champDistance);
                oppositeDistance = calcDistance(oppositeNearest, query);
            }
        }

        if (followDistance < champDistance && followDistance < oppositeDistance){
            return followNearest;
        } else if (oppositeDistance < champDistance && oppositeDistance < followDistance){
            return oppositeNearest;
        } else if (oppositeDistance==followDistance && oppositeDistance < champDistance){
            if (oppositeNearest.x() > followNearest.x()){
                return oppositeNearest;
            } else {
                return followNearest;
            }
        } else {
            return node.p;
        }
    }
    private int comparePoints(Point2D thisPt, Point2D thatPt, boolean compareX){
        double thisComparator;
        double thatComparator;
        if (compareX) {
            thisComparator = thisPt.x();
            thatComparator = thatPt.x();
        } else {
            thisComparator = thisPt.y();
            thatComparator = thatPt.y();
        }
        if (thisComparator < thatComparator){
            return -1;
        } else if (thisComparator > thatComparator){
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();

        tree.insert(new Point2D(0.372, 0.497));
        tree.insert(new Point2D(0.564, 0.413));
        tree.insert(new Point2D(0.226, 0.577));
        tree.insert(new Point2D(0.144, 0.179));
        tree.insert(new Point2D(0.083, 0.51));
        tree.insert(new Point2D(0.32, 0.708));
        tree.insert(new Point2D(0.417, 0.362));
        tree.insert(new Point2D(0.862, 0.825));
        tree.insert(new Point2D(0.785, 0.725));
        tree.insert(new Point2D(0.499, 0.208));


        Point2D query = new Point2D(0.978,0.361);
        tree.draw();
        StdDraw.setPenRadius(0.01);
        query.draw();
        System.out.println(tree.nearest(query));

        PointSET set = new PointSET();
        set.insert(new Point2D(0.372, 0.497));
        set.insert(new Point2D(0.564, 0.413));
        set.insert(new Point2D(0.226, 0.577));
        set.insert(new Point2D(0.144, 0.179));
        set.insert(new Point2D(0.083, 0.51));
        set.insert(new Point2D(0.32, 0.708));
        set.insert(new Point2D(0.417, 0.362));
        set.insert(new Point2D(0.862, 0.825));
        set.insert(new Point2D(0.785, 0.725));
        set.insert(new Point2D(0.499, 0.208));

        System.out.println(set.nearest(query));
    }
}