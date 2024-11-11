import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int BOARD_DIMENSION;
    private final int[][] BOARD;

    public Board(int[][] tiles) {
        BOARD_DIMENSION = tiles.length;

        BOARD = copyBoardContent(tiles);
    }

    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append(BOARD_DIMENSION).append("\n");

        for (int row = 0; row < BOARD_DIMENSION; row++) {
            for (int col = 0; col < BOARD_DIMENSION; col++)
                str.append(String.format("%2d", BOARD[row][col]));

            str.append("\n");
        }

        return str.toString();
    }

    public int dimension() {
        return BOARD_DIMENSION;
    }

    public int hamming() {
        int totalDistance = 0;

        for (int row = 0; row < BOARD_DIMENSION; row++) {
            for (int col = 0; col < BOARD_DIMENSION; col++) {
                int expectedValue = calculationHamming(row, col);

                if (expectedValue != 0 && expectedValue != BOARD[row][col])
                    totalDistance++;
            }
        }

        return totalDistance;
    }

    public int manhattan() {
        int distance = 0;

        for (int row = 0; row < BOARD_DIMENSION; row++)
            for (int col = 0; col < BOARD_DIMENSION; col++)
                distance += calculationManhattan(BOARD[row][col], row, col);

        return distance;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;

        Board otherBoard = (Board) other;

        return Arrays.deepEquals(this.BOARD, otherBoard.BOARD);
    }

    public Iterable<Board> neighbors() {
        int blankRow = 0, blankCol = 0;
        boolean blankFound = false;
        ArrayList<Board> neighbors = new ArrayList<Board>();

        for (blankRow = 0; blankRow < BOARD_DIMENSION; blankRow++) {
            for (blankCol = 0; blankCol < BOARD_DIMENSION; blankCol++)
                if (BOARD[blankRow][blankCol] == 0) {
                    blankFound = true;
                    break;
                }

            if (blankFound) break;
        }

        if (blankRow > 0) neighbors.add(copyFromOriginalAndSwapTiles(blankRow, blankCol, blankRow - 1, blankCol));

        if (blankCol > 0) neighbors.add(copyFromOriginalAndSwapTiles(blankRow, blankCol, blankRow, blankCol - 1));

        if (blankCol < (BOARD_DIMENSION - 1)) neighbors.add(copyFromOriginalAndSwapTiles(blankRow, blankCol, blankRow, blankCol + 1));

        if (blankRow < (BOARD_DIMENSION - 1)) neighbors.add(copyFromOriginalAndSwapTiles(blankRow, blankCol, blankRow + 1, blankCol));

        return neighbors;
    }

    public Board twin() {
        int swappingRow, swappingCol, twinRow, twinCol;
        swappingRow = swappingCol = 0;
        twinCol = twinRow = BOARD_DIMENSION - 1;

        if (BOARD[swappingRow][swappingCol] == 0) swappingCol++;

        if (BOARD[twinRow][twinCol] == 0) twinCol--;

        return copyFromOriginalAndSwapTiles(swappingRow, swappingCol, twinRow, twinCol);
    }

    private int[][] copyBoardContent(int[][] original) {
        int[][] destination = new int[BOARD_DIMENSION][BOARD_DIMENSION];

        for (int row = 0; row < BOARD_DIMENSION; row++)
            System.arraycopy(original[row], 0, destination[row], 0, BOARD_DIMENSION);

        return destination;
    }

    private int calculationHamming(int row, int col) {
        if (row == (BOARD_DIMENSION - 1) && col == (BOARD_DIMENSION - 1)) return 0;
        return row * BOARD_DIMENSION + col + 1;
    }

    private int calculationManhattan(int boardElement, int row, int col) {
        if (boardElement == 0) return 0;

        int correctRow = (boardElement - 1) / BOARD_DIMENSION;
        int correctCol = (boardElement - 1) % BOARD_DIMENSION;

        return Math.abs(row - correctRow) + Math.abs(col - correctCol);
    }

    private Board copyFromOriginalAndSwapTiles(int originalRow, int originalCol, int newRow, int newCol) {
        int[][] boardCopy = copyBoardContent(BOARD);

        boardCopy[originalRow][originalCol] = BOARD[newRow][newCol];
        boardCopy[newRow][newCol] = BOARD[originalRow][originalCol];

        return new Board(boardCopy);
    }
}
