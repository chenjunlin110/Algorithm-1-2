import java.util.ArrayList;
import java.util.List;

public class Board {
    private int dimension;
    private int hamming;
    private int manhattan;
    private int[][] blockCache;
    private int zeroI;
    private int zeroJ;

    public Board(int[][] tiles) {
        this(tiles, tiles.length);
    }

    private Board(int[][] tiles, int boardDimension) {
        if (tiles == null) {
            throw new IllegalArgumentException("tiles must not be null");
        }
        this.dimension = boardDimension;
        this.blockCache = boardcopy(tiles);
        calculateDistance(tiles);
    }

    private int[][] boardcopy(int[][] tiles) {
        int[][] copy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(tiles[i], 0, copy[i], 0, dimension);
        }
        return copy;
    }

    private void calculateDistance(int[][] tiles) {
        int manhattanCalc = 0;
        int hammingCalc = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != 0 && tiles[i][j] != ((dimension * i) + j + 1)) {
                    hammingCalc++;
                }
                if (tiles[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                    continue;
                }
                int calI = (tiles[i][j] - 1) / dimension;
                int calJ = (tiles[i][j] - 1) % dimension;
                if (calI != i || calJ != j) {
                    int distanceI = calI - i;

                    manhattanCalc += distanceI < 0 ? distanceI * -1 : distanceI;

                    int distanceJ = calJ - j;

                    manhattanCalc += distanceJ < 0 ? distanceJ * -1 : distanceJ;
                }
            }
        }
        this.hamming = hammingCalc;
        this.manhattan = manhattanCalc;
    }


    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", blockCache[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y == this) {
            return true;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;

        if (that.dimension != dimension ||
                that.manhattan != manhattan ||
                that.hamming != hamming) {
            return false;
        }

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blockCache[i][j] != that.blockCache[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        addNeighbor(neighborSwap(zeroI, zeroJ, zeroI - 1, zeroJ), neighbors);
        addNeighbor(neighborSwap(zeroI, zeroJ, zeroI + 1, zeroJ), neighbors);
        addNeighbor(neighborSwap(zeroI, zeroJ, zeroI, zeroJ - 1), neighbors);
        addNeighbor(neighborSwap(zeroI, zeroJ, zeroI, zeroJ + 1), neighbors);
        return neighbors;
    }

    private Board neighborSwap(int fromI, int fromJ, int toI, int toJ) {
        if (toI < 0 || toI >= dimension || toJ < 0 || toJ >= dimension ||
                fromI < 0 || fromI >= dimension || fromJ < 0 || fromJ >= dimension) {
            return null;
        }

        int[][] blocks = boardcopy(this.blockCache);

        int tmp = blocks[fromI][fromJ];
        blocks[fromI][fromJ] = blocks[toI][toJ];
        blocks[toI][toJ] = tmp;
        return new Board(blocks, dimension);
    }

    private void addNeighbor(Board b, List<Board> neighbors) {
        if (b != null) {
            neighbors.add(b);
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int swapToI;
        if (zeroI == 0) {
            swapToI = zeroI + 1;
        }
        else {
            swapToI = zeroI - 1;
        }

        int swapToJ;
        if (zeroJ == 0) {
            swapToJ = zeroJ + 1;
        }
        else {
            swapToJ = zeroJ - 1;
        }
        return neighborSwap(swapToI, zeroJ, swapToI, swapToJ);
    }

    // unit testing (not graded)
    public static void main(String[] args) {

    }
}
