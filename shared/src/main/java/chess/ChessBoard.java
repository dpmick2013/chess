package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow()][position.getColumn()] = null;
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int j = 0; j < 2; j++) {
            ChessGame.TeamColor color;
            ChessPiece piece;
            ChessPosition position;
            int row;
            if (j == 0) {
                color = ChessGame.TeamColor.WHITE;
                row = 1;
            }
            else {
                color = ChessGame.TeamColor.BLACK;
                row = 6;
            }
            piece = new ChessPiece(color, ChessPiece.PieceType.PAWN);
            for (int i = 0; i < 8; i++) {
                position = new ChessPosition(row, i);
                addPiece(position, piece);
            }
            if (j == 0) {
                row = 0;
            }
            else {
                row = 7;
            }
            piece = new ChessPiece(color, ChessPiece.PieceType.ROOK);
            for (int i = 0; i < 8; i++) {
                position = new ChessPosition(row, i);
                addPiece(position, piece);
            }
            piece = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
            for (int i = 1; i < 7; i += 5) {
                position = new ChessPosition(row, i);
                addPiece(position, piece);
            }
            piece = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
            for (int i = 2; i < 6; i += 3) {
                position = new ChessPosition(row, i);
                addPiece(position, piece);
            }
            piece = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
            position = new ChessPosition(row, 3);
            addPiece(position, piece);
            piece = new ChessPiece(color, ChessPiece.PieceType.KING);
            position = new ChessPosition(row, 4);
            addPiece(position, piece);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}