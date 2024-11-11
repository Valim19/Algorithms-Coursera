import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KdTree {
    private Node root = null;
    private int size  = 0;

    private static class Node {
        private final Point2D point;
        private Node left, right;
        private final boolean useX;

        public Node(Point2D p, Node lt, Node r, boolean alternate) {
            point = p;
            left  = lt;
            right = r;
            useX  = alternate;
        }

    }

    private void checkNull(Object obj) {
        if (obj == null) throw new IllegalArgumentException("Parameter can't be null");
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        checkNull(p);

        if (root == null) {
            root = new Node(p, null, null, true);
            ++size;
        } else if (!contains(p)) {
            ++size;
            insert(root, p);
        }
    }

    private void insert(Node parent, Point2D p) {
        Comparator<Point2D> comparator = parent.useX ? Point2D.X_ORDER : Point2D.Y_ORDER;

        if (comparator.compare(parent.point, p) > 0) {
            if (parent.left == null) parent.left = new Node(p, null, null, !parent.useX);
            else insert(parent.left, p);
        } else {
            if (parent.right == null) parent.right = new Node(p, null, null, !parent.useX);
            else insert(parent.right, p);
        }
    }

    public boolean contains(Point2D p) {
        checkNull(p);
        return contains(root, p);
    }

    private boolean contains(Node parent, Point2D p) {
        if (parent == null) return false;

        Comparator<Point2D> comparator = parent.useX ? Point2D.X_ORDER : Point2D.Y_ORDER;

        if (parent.point.equals(p)) return true;

        if (comparator.compare(parent.point, p) > 0) return contains(parent.left, p);
        else return contains(parent.right, p);
    }

    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node n, RectHV rect) {
        if (n == null) return;

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        n.point.draw();

        StdDraw.setPenRadius();

        if (n.useX) { // vertical
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.point.x(), rect.ymin(), n.point.x(), rect.ymax());
            draw(n.left, new RectHV(rect.xmin(), rect.ymin(), n.point.x(), rect.ymax()));
            draw(n.right, new RectHV(n.point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        }
        else { // horizontal
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), n.point.y(), rect.xmax(), n.point.y());
            draw(n.left, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), n.point.y()));
            draw(n.right, new RectHV(rect.xmin(), n.point.y(), rect.xmax(), rect.ymax()));
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        List<Point2D> points = new ArrayList<>();
        range(root, rect, points);
        return points;
    }

    private void range(Node current, RectHV rect, List<Point2D> points) {
        if (current == null)
            return;

        if (rect.contains(current.point))
            points.add(current.point);

        if (current.useX) { // vertical
            if (current.point.x() >= rect.xmin() && current.point.x() <= rect.xmax()) {
                range(current.left, rect, points);
                range(current.right, rect, points);
            }
            else if (current.point.x() <= rect.xmin()) {
                range(current.right, rect, points);
            }
            else {
                range(current.left, rect, points);
            }
        }
        else { // horizontal
            if (current.point.y() >= rect.ymin() && current.point.y() <= rect.ymax()) {
                range(current.left, rect, points);
                range(current.right, rect, points);
            }
            else if (current.point.y() <= rect.ymin()) {
                range(current.right, rect, points);
            }
            else {
                range(current.left, rect, points);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        return nearest(root, root, p, Double.POSITIVE_INFINITY, new RectHV(0, 0, 1, 1));
    }

    private Point2D nearest(Node current, Node currentMin, Point2D p, double minDistance,
                            RectHV nodeRect) {
        if (current == null)
            return currentMin == null ? null : currentMin.point;

        double distance = p.distanceSquaredTo(current.point);
        if (distance < minDistance) {
            currentMin = current;
            minDistance = distance;
        }

        if (nodeRect.distanceSquaredTo(p) > minDistance)
            return currentMin == null ? null : currentMin.point;

        RectHV leftRect, rightRect;

        if (current.useX) {
            leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), current.point.x(),
                    nodeRect.ymax());
            rightRect = new RectHV(current.point.x(), nodeRect.ymin(), nodeRect.xmax(),
                    nodeRect.ymax());
        }
        else {
            leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(),
                    current.point.y());
            rightRect = new RectHV(nodeRect.xmin(), current.point.y(), nodeRect.xmax(),
                    nodeRect.ymax());
        }

        Point2D rightNearest = nearest(current.right, currentMin, p, minDistance, rightRect);
        Point2D leftNearest = nearest(current.left, currentMin, p, minDistance, leftRect);

        return p.distanceSquaredTo(rightNearest) < p.distanceSquaredTo(leftNearest)
                ? rightNearest
                : leftNearest;
    }
}
