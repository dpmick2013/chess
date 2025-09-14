package chess;

import java.util.Collection;
import java.util.HashSet;

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
        if (getPieceType() == PieceType.KING) kingMoves(board, myPosition, moveList);
        if (getPieceType() == PieceType.QUEEN) queenMoves(board, myPosition, moveList);
        if (getPieceType() == PieceType.BISHOP) bishopMoves(board, myPosition, moveList);
        if (getPieceType() == PieceType.KNIGHT) knightMoves(board, myPosition, moveList);
        if (getPieceType() == PieceType.ROOK) rookMoves(board, myPosition, moveList);
        if (getPieceType() == PieceType.PAWN) pawnMoves(board, myPosition, moveList);
        return moveList;
    }

    boolean enemy;
    private void kingMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
        ChessPosition end;
        ChessMove move;
        // Up
        if (row + 1 <= 8) {
            end = new ChessPosition(row + 1, col);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Up Right
        if (row + 1 <= 8 && col + 1 <= 8) {
            end = new ChessPosition(row + 1, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Right
        if (col + 1 <= 8) {
            end = new ChessPosition(row, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Down Right
        if (row - 1 >= 1 && col + 1 <= 8) {
            end = new ChessPosition(row - 1, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Down
        if (row - 1 >= 1) {
            end = new ChessPosition(row - 1, col);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Down Left
        if (row - 1 >= 1 && col - 1 >= 1) {
            end = new ChessPosition(row - 1, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Left
        if (col - 1 >= 1) {
            end = new ChessPosition(row, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Up Left
        if (row + 1 <= 8 && col - 1 >= 1) {
            end = new ChessPosition(row + 1, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
    }

    private void queenMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
    }

    private void bishopMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
        // Up Right
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col + i > 8) break;
            var end = new ChessPosition(row + i, col + i);
            ChessMove move;
            if (occupied(board, end) && !enemy) break;
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Down Right
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col + i > 8) break;
            var end = new ChessPosition(row - i, col + i);
            ChessMove move;
            if (occupied(board, end) && !enemy) break;
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Down Left
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col - i < 1) break;
            var end = new ChessPosition(row - i, col - i);
            ChessMove move;
            if (occupied(board, end) && !enemy) break;
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Up Left
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col - i < 1) break;
            var end = new ChessPosition(row + i, col - i);
            ChessMove move;
            if (occupied(board, end) && !enemy) break;
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
    }

    private void knightMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
        ChessPosition end;
        ChessMove move;
        // Up Right
        if (row + 2 <= 8 && col + 1 <= 8) {
            end = new ChessPosition(row + 2, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Right Up
        if (row + 1 <= 8 && col + 2 <= 8) {
            end = new ChessPosition(row + 1, col + 2);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Right Down
        if (row - 1 >= 1 && col + 2 <= 8) {
            end = new ChessPosition(row - 1, col + 2);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Down Right
        if (row - 2 >= 1 && col + 1 <= 8) {
            end = new ChessPosition(row - 2, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Down Left
        if (row - 2 >= 1 && col - 1 >= 1) {
            end = new ChessPosition(row - 2, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Left Down
        if (row - 1 >= 1 && col - 2 >= 1) {
            end = new ChessPosition(row - 1, col - 2);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Left Up
        if (row + 1 <= 8 && col - 2 >= 1) {
            end = new ChessPosition(row + 1, col - 2);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
        // Up Left
        if (row + 2 <= 8 && col - 1 >= 1) {
            end = new ChessPosition(row + 2, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
    }

    private void rookMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
    }

    private void pawnMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
    }

    private boolean occupied(ChessBoard board, ChessPosition pos) {
        if (board.getPiece(pos) != null) {
            enemy = board.getPiece(pos).pieceColor != getTeamColor();
            return true;
        }
        return false;
    }
}
