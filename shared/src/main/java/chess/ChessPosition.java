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
        String col_string = "";
        if (col == 1) {
            col_string = "A";
        }
        if (col == 2) {
            col_string = "B";
        }
        if (col == 3) {
            col_string = "C";
        }
        if (col == 4) {
            col_string = "D";
        }
        if (col == 5) {
            col_string = "E";
        }
        if (col == 6) {
            col_string = "F";
        }
        if (col == 7) {
            col_string = "G";
        }
        if (col == 8) {
            col_string = "H";
        }
        return col_string + row;
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
