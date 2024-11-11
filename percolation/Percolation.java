import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final int TOP = 0;
    private final int bottom;
    private int size;
    private int openSitesCounter;
    private boolean[][] opened;
    private final WeightedQuickUnionUF quickUnionUF;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        size             = n;
        bottom           = (size * size) + 1;
        opened           = new boolean[size][size]; // Creates a matrice with all the sites setted to false
        quickUnionUF     = new WeightedQuickUnionUF((size * size) + 2);
        openSitesCounter = 0;
    }

    private void checkException(int row, int col) {
        if (row <= 0 || row > size || col <= 0 || col > size) throw new IllegalArgumentException();
    }

    public void open(int row, int col) {
        checkException(row, col);

        if (opened[row - 1][col - 1]) return;

        opened[row - 1][col - 1] = true;
        openSitesCounter++;

        if (row == 1) {
            quickUnionUF.union(convertBiToUnidimensional(row, col), TOP);
        }

        if (row == size) {
            quickUnionUF.union(convertBiToUnidimensional(row, col), bottom);
        }

        if (row > 1 && isOpen(row - 1, col)) {
            quickUnionUF.union(convertBiToUnidimensional(row, col), convertBiToUnidimensional(row - 1, col));
        }

        if (row < size && isOpen(row + 1, col)) {
            quickUnionUF.union(convertBiToUnidimensional(row, col), convertBiToUnidimensional(row + 1, col));
        }

        if (col > 1 && isOpen(row, col - 1)) {
            quickUnionUF.union(convertBiToUnidimensional(row, col), convertBiToUnidimensional(row, col - 1));
        }

        if (col < size && isOpen(row, col + 1)) {
            quickUnionUF.union(convertBiToUnidimensional(row, col), convertBiToUnidimensional(row, col + 1));
        }
    }

    public boolean isOpen(int row, int col) {
        checkException(row, col);
        return opened[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        if ((row > 0 && row <= size) && (col > 0 && col <= size)) {
            return quickUnionUF.find(TOP) == quickUnionUF.find(convertBiToUnidimensional(row, col));
        } else throw new IllegalArgumentException();
    }

    private int convertBiToUnidimensional(int row, int col) {
        return size * (row - 1) + col;
    }

    public int numberOfOpenSites() {
        return openSitesCounter;
    }

    public boolean percolates() {
        return quickUnionUF.find(TOP) == quickUnionUF.find(bottom);
    }
}
