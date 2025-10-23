package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private final int row;
    private final int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public String toString() {
        String colString = "";
        if (col == 1) {
            colString = "A";
        }
        if (col == 2) {
            colString = "B";
        }
        if (col == 3) {
            colString = "C";
        }
        if (col == 4) {
            colString = "D";
        }
        if (col == 5) {
            colString = "E";
        }
        if (col == 6) {
            colString = "F";
        }
        if (col == 7) {
            colString = "G";
        }
        if (col == 8) {
            colString = "H";
        }
        return colString + row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
