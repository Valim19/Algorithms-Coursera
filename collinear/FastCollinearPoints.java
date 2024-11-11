import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private int segmentCount;
    private LineSegment[] collinearSegments;
    private Point[][] collinearSegmentsInternal;

    public FastCollinearPoints(Point[] points) {
        validateInput(points);

        segmentCount = 0;
        collinearSegments = new LineSegment[points.length];
        collinearSegmentsInternal = new Point[points.length][2];

        for (Point p : points) {
            Point[] slopesToP = getSlopesTo(p, points);

            for (int j = 0; j < slopesToP.length - 2; j++) {
                Point q = slopesToP[j];
                Point[] candidatePoints = Arrays.copyOfRange(slopesToP, j + 1, slopesToP.length);
                Point[] collinearSequence = findCollinearSequence(p, q, candidatePoints);

                if (collinearSequence.length >= 4) {
                    addSegment(collinearSequence);
                    break;
                }
            }
        }
    }

    public int numberOfSegments() {
        return segmentCount;
    }

    public LineSegment[] segments() {
        return Arrays.copyOfRange(collinearSegments, 0, numberOfSegments());
    }

    private boolean isIncluded(Point[] segment) {
        for (int i = 0; i < segmentCount; i++) {
            if (Arrays.equals(segment, collinearSegmentsInternal[i])) {
                return true;
            }
        }
        return false;
    }

    private Point[] findCollinearSequence(Point p, Point q, Point[] candidatePoints) {
        Stack<Point> collinearPoints = new Stack<Point>();
        collinearPoints.push(p);
        collinearPoints.push(q);

        for (Point r : candidatePoints) {
            if (areCollinear(p, collinearPoints.peek(), r)) collinearPoints.push(r);
            else break;
        }

        return stackToArray(collinearPoints);
    }

    private Point[] stackToArray(Stack<Point> stack) {
        Point[] array = new Point[stack.size()];
        int index = 0;

        for (Point point : stack) {
            array[index++] = point;
        }

        return array;
    }

    private void addSegment(Point[] ps) {
        Arrays.sort(ps);
        Point[] segment = new Point[] { ps[0], ps[ps.length - 1] };

        if (!isIncluded(segment)) {
            collinearSegmentsInternal[segmentCount] = segment;
            collinearSegments[segmentCount] = new LineSegment(segment[0], segment[1]);
            segmentCount++;
        }
    }

    public static void main(String[] args) {
        In input = new In(args[0]);
        int n = input.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = input.readInt();
            int y = input.readInt();
            points[i] = new Point(x, y);
        }

        FastCollinearPoints collinear = new FastCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }

    private void validateInput(Point[] points) {
        if (points == null) throw new IllegalArgumentException("The input cannot be null");

        for (Point point : points) if (point == null) throw new IllegalArgumentException("The input cannot contain null points");

        for (int i = 0; i < points.length; i++)
            for (int j = i + 1; j < points.length; j++)
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException("The input cannot contain repeated points");
    }

    private boolean areCollinear(Point p1, Point p2, Point p3) {
        return (Double.compare(p1.slopeTo(p2), p1.slopeTo(p3)) == 0);
    }

    private Point[] getSlopesTo(Point p, Point[] points) {
        Point[] slopesToP = Arrays.copyOf(points, points.length);
        Arrays.sort(slopesToP, p.slopeOrder());

        return Arrays.copyOfRange(slopesToP, 1, slopesToP.length);
    }
}
