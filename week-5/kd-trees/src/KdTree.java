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

    private Node put(Node node, boolean compareX, Point2D p, double xmin, double ymin, double xmax, double ymax) {
        if (node == null || node.p == null) {
            RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
            Node created = new Node(p, rect);
            if (root == null) {
                root = created;
            }
            return created;
        }

        int cmp = comparePoints(p, node.p, compareX);

        // if the point to be inserted has a smaller x-coordinate than the point at the root, go left; otherwise go right
        if (cmp < 0) {
            // Go down the tree, setting the rectangle parameters as we go down
            if (compareX) {
                // if the point is TO THE LEFT
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

    private boolean search(Node node, boolean compareX, Point2D p) {
        // end case - reach the end of the tree
        if (node == null || node.p == null) {
            return false;
        }

        int cmp = comparePoints(p, node.p, compareX);
        if (cmp < 0) {
            // search left tree
            return search(node.lb, !compareX, p);
        } else if (cmp > 0) {
            // search right tree
            return search(node.rt, !compareX, p);
        } else {
            // if point is bordering, need to check both sides
            // end case - point is equal to the border and hence it is present
            if (node.p.x() == p.x() && node.p.y() == p.y()) {
                return true;
            }
            boolean isInLeft = search(node.lb, !compareX, p);
            if (isInLeft) {
                return true;
            }
            // search right tree - as left is already searched,
            // we can return whatever the result of the right tree is.
            return search(node.rt, !compareX, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        drawR(root, true);
    }

    private void drawR(Node node, boolean compareX) {
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
                // search left subtree
                searchRect(node.lb, query, arr);
            }
        }
        // check if right subtree intersects
        if (node.rt != null) {
            if (node.rt.rect.intersects(query)) {
                // search right subtree
                searchRect(node.rt, query, arr);
            }
        }
    }

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

    private Point2D searchNearest(Node node, boolean compareX, Point2D query, double champDistance) {
        // ALGO AGAIN
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
        if (node == null) {
            return null;
        }

        double distance = calcDistance(node.p, query);
        if (distance < champDistance) {
            champDistance = distance;
        }

        int cmp = comparePoints(query, node.p, compareX);

        Node followNode = null;
        Node oppositeNode = null;
        // set the "follow" node, i.e. the node where the point is on the SAME side of the split
        // and the "opposite" node, where the point is NOT in that side.
        if (cmp <= 0) {
            if (node.lb != null) {
                followNode = node.lb;
            }
            if (node.rt != null) {
                oppositeNode = node.rt;
            }
        } else {
            if (node.rt != null) {
                followNode = node.rt;
            }
            if (node.lb != null) {
                oppositeNode = node.lb;
            }
        }

        Point2D followPoint = null;
        Point2D oppositePoint = null;
        double followDistance = Double.POSITIVE_INFINITY;
        double oppositeDistance = Double.POSITIVE_INFINITY;
        if (followNode != null) {
            followPoint = searchNearest(followNode, !compareX, query, champDistance);
            followDistance = calcDistance(followPoint, query);
        }
        if (oppositeNode != null) {
            // for the opposite node, search it only if there is a possible best case scenario.
            // calculating the best case scenario
            double x;
            double y;
            if (compareX) {
                x = node.getX(node);
                y = query.y();
                // if point falls in rectangle
                if (node.rect.ymax() > query.y() && node.rect.ymin() < query.y()) {
                    y = query.y();
                } else if (node.rect.ymax() < query.y()) { // if point falls above ymax
                    y = node.rect.ymax();
                } else if (query.y() < node.rect.ymin()) {   // if point falls below ymin
                    y = node.rect.ymin();
                }
            } else {
                x = query.x();
                // if point falls in rectangle
                if (node.rect.xmax() > query.x() && node.rect.xmin() < query.x()) {
                    x = query.x();
                } else if (node.rect.xmax() < query.x()) { // if point falls above xmax
                    x = node.rect.xmax();
                } else if (query.x() < node.rect.xmin()) {   // if point falls below xmin
                    x = node.rect.xmin();
                }
                y = node.getY(node);
            }
            double xdiff = x - query.x();
            double ydiff = y - query.y();
            double bestCaseDist = Math.sqrt(xdiff * xdiff + ydiff * ydiff);

            if (followDistance < champDistance) {
                // if the follow distance beats the best, check using the followDistance
                if (bestCaseDist < followDistance) {
                    oppositePoint = searchNearest(oppositeNode, !compareX, query, followDistance);
                    oppositeDistance = calcDistance(oppositePoint, query);
                }
            } else if (bestCaseDist < champDistance) {
                // else, just check the opposite node
                oppositePoint = searchNearest(oppositeNode, !compareX, query, champDistance);
                oppositeDistance = calcDistance(oppositePoint, query);
            }
        }

        if (followDistance < champDistance && followDistance < oppositeDistance) {
            return followPoint;
        } else if (oppositeDistance < champDistance && oppositeDistance < followDistance) {
            return oppositePoint;
        } else if (oppositeDistance == followDistance && oppositeDistance < champDistance) {
            if (oppositePoint.x() > followPoint.x()) {
                return oppositePoint;
            } else {
                return followPoint;
            }
        } else {
            return node.p;
        }
    }

    private int comparePoints(Point2D thisPt, Point2D thatPt, boolean compareX) {
        double thisComparator;
        double thatComparator;
        if (compareX) {
            thisComparator = thisPt.x();
            thatComparator = thatPt.x();
        } else {
            thisComparator = thisPt.y();
            thatComparator = thatPt.y();
        }
        if (thisComparator < thatComparator) {
            return -1;
        } else if (thisComparator > thatComparator) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
    }
}