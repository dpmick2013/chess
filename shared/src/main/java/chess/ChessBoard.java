package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
    }
    public ChessBoard(ChessBoard copy) {
        board = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            board[row] = Arrays.copyOf(copy.board[row], copy.board.length);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param pos where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition pos, ChessPiece piece) {
        board[pos.getRow() - 1][pos.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param pos The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition pos) {
        return board[pos.getRow() - 1][pos.getColumn() - 1];
    }

    public void removePiece(ChessPosition pos) {
        board[pos.getRow() - 1][pos.getColumn() - 1] = null;
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPosition pos;
        ChessPiece white;
        ChessPiece black;
        // Pawns
        white = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        black = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int i = 1; i < 9; i++) {
            pos = new ChessPosition(2, i);
            addPiece(pos, white);
            pos = new ChessPosition(7, i);
            addPiece(pos, black);

        }
        // Rooks
        white = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        black = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 1; i < 9; i += 7) {
            pos = new ChessPosition(1, i);
            addPiece(pos, white);
            pos = new ChessPosition(8, i);
            addPiece(pos, black);
        }
        // Knights
        white = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        black = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        for (int i = 2; i < 8; i += 5) {
            pos = new ChessPosition(1, i);
            addPiece(pos, white);
            pos = new ChessPosition(8, i);
            addPiece(pos, black);
        }
        // Bishops
        white = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        black = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        for (int i = 3; i < 7; i += 3) {
            pos = new ChessPosition(1, i);
            addPiece(pos, white);
            pos = new ChessPosition(8, i);
            addPiece(pos, black);
        }
        // Queens
        white = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        black = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        pos = new ChessPosition(1, 4);
        addPiece(pos, white);
        pos = new ChessPosition(8, 4);
        addPiece(pos, black);
        // Kings
        white = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        black = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        pos = new ChessPosition(1, 5);
        addPiece(pos, white);
        pos = new ChessPosition(8, 5);
        addPiece(pos, black);
    }

    @Override
    public String toString() {
        var boardString = "";
        var pieceString = "";
        ChessPiece piece;
        for (int i = 8; i > 0; i--) {
            for (int j = 1; j < 9; j++) {
                piece = getPiece(new ChessPosition(i, j));
                if (j == 8) {
                    if (piece == null) {
                        boardString = boardString + "| |\n";
                    }
                    else {
                        pieceString = piece.toString();
                        boardString = boardString + "|" + pieceString + "|\n";
                    }
                }
                else {
                    if (piece == null) {
                        boardString = boardString + "| ";
                    }
                    else {
                        pieceString = piece.toString();
                        boardString = boardString + "|" + pieceString;
                    }
                }
            }
        }
        return boardString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
