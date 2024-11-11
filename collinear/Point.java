import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public double slopeTo(Point that) {
        if (this.x == that.x)
            if (this.y == that.y) return Double.NEGATIVE_INFINITY;
            else return Double.POSITIVE_INFINITY;

        if (this.y == that.y) return 0.0;

        return ((double) (that.y - this.y) / (double) (that.x - this.x));
    }

    public int compareTo(Point that) {
        int differenceY = this.y - that.y;

        if (differenceY != 0) return differenceY;
        else return (this.x - that.x);
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public Comparator<Point> slopeOrder() {
        return new BySlope(this);
    }

    private static class BySlope implements Comparator<Point> {
        private final Point p;

        public BySlope(Point p) {
            this.p = p;
        }

        public int compare(Point p1, Point p2) {
            double slopeTo1 = this.p.slopeTo(p1);
            double slopeTo2 = this.p.slopeTo(p2);

            if (slopeTo1 > slopeTo2) return 1;
            if (slopeTo1 < slopeTo2) return -1;
            else return 0;
        }
    }
}
