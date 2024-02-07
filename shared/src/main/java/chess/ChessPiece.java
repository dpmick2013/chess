package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moveList = new HashSet<>();
        if (getPieceType() == PieceType.BISHOP) {
            bishopMoves(myPosition, moveList, board);
        }
        if (getPieceType() == PieceType.KING) {
            kingMoves(myPosition, moveList, board);
        }
        return moveList;
    }
    private void bishopMoves(ChessPosition startPosition, Collection<ChessMove> moveList, ChessBoard board) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        // UPRIGHT
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col + i > 8) {
                continue;
            }
            row += i;
            col += i;
            ChessPosition end = new ChessPosition(row, col);
            row -= i;
            col -= i;
            if (board.getPiece(end) != null && board.getPiece(end).pieceColor == getTeamColor()) {
                break;
            }
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
            if (board.getPiece(end) != null) {
                break;
            }
        }
        // DOWNLEFT
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col - i < 1) {
                continue;
            }
            row -= i;
            col -= i;
            ChessPosition end = new ChessPosition(row, col);
            row += i;
            col += i;
            if (board.getPiece(end) != null && board.getPiece(end).pieceColor == getTeamColor()) {
                break;
            }
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
            if (board.getPiece(end) != null) {
                break;
            }
        }
        // DOWNRIGHT
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col + i > 8) {
                continue;
            }
            row -= i;
            col += i;
            ChessPosition end = new ChessPosition(row, col);
            row += i;
            col -= i;
            if (board.getPiece(end) != null && board.getPiece(end).pieceColor == getTeamColor()) {
                break;
            }
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
            if (board.getPiece(end) != null) {
                break;
            }
        }
        // UPLEFT
        for (int i = 8; i > 0; i--) {
            ChessPosition test = new ChessPosition(row + 1, col - 1);
            if (board.getPiece(test) != null) {
                if (board.getPiece(test).pieceColor == getTeamColor()) {
                    break;
                }
                else {
                    ChessMove move = new ChessMove(startPosition, test, null);
                    moveList.add(move);
                }
            }
            if (row + i > 8 || col - i < 1) {
                continue;
            }
            row += i;
            col -= i;
            ChessPosition end = new ChessPosition(row, col);
            row -= i;
            col += i;
            if (board.getPiece(end) != null && board.getPiece(end).pieceColor == getTeamColor()) {
                break;
            }
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
            if (board.getPiece(end) != null) {
                break;
            }
        }
    }
    private void kingMoves(ChessPosition startPosition, Collection<ChessMove> moveList, ChessBoard board) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        // UP
        if (row + 1 < 9) {
            ChessPosition end = new ChessPosition(row + 1, col);
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
        }
        // UPRIGHT
        if (row + 1 < 9 && col + 1 < 9) {
            ChessPosition end = new ChessPosition(row + 1, col + 1);
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
        }
        // RIGHT
        if (col + 1 < 9) {
            ChessPosition end = new ChessPosition(row, col + 1);
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
        }
        // DOWNRIGHT
        if (row - 1 > 0 && col + 1 < 9) {
            ChessPosition end = new ChessPosition(row - 1, col + 1);
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
        }
        // DOWN
        if (row - 1 > 0) {
            ChessPosition end = new ChessPosition(row - 1, col);
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
        }
        // DOWNLEFT
        if (row - 1 > 0 && col - 1 > 0) {
            ChessPosition end = new ChessPosition(row - 1, col - 1);
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
        }
        // LEFT
        if (col - 1 > 0) {
            ChessPosition end = new ChessPosition(row, col - 1);
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
        }
        // UPLEFT
        if (row + 1 < 9 && col - 1 > 0) {
            ChessPosition end = new ChessPosition(row + 1, col - 1);
            ChessMove move = new ChessMove(startPosition, end, null);
            moveList.add(move);
        }
    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
