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
        if (getPieceType() == PieceType.KNIGHT) {
            knightMoves(myPosition, moveList, board);
        }
        if (getPieceType() == PieceType.PAWN) {
            pawnMoves(myPosition, moveList, board);
        }
        return moveList;
    }
    boolean hasMoved = false;
    boolean enemy = false;
    private void bishopMoves(ChessPosition startPosition, Collection<ChessMove> moveList, ChessBoard board) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        // UPRIGHT
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col + i > 8) {
                continue;
            }
            ChessPosition end = new ChessPosition(row + i, col + i);
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
            ChessPosition end = new ChessPosition(row - i, col - i);
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
            ChessPosition end = new ChessPosition(row - i, col + i);
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
            ChessPosition end = new ChessPosition(row + i, col - i);
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
        ChessPosition end;
        ChessMove move;
        // UP
        if (row + 1 < 9) {
            end = new ChessPosition(row + 1, col);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // UPRIGHT
        if (row + 1 < 9 && col + 1 < 9) {
            end = new ChessPosition(row + 1, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // RIGHT
        if (col + 1 < 9) {
            end = new ChessPosition(row, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // DOWNRIGHT
        if (row - 1 > 0 && col + 1 < 9) {
            end = new ChessPosition(row - 1, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // DOWN
        if (row - 1 > 0) {
            end = new ChessPosition(row - 1, col);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // DOWNLEFT
        if (row - 1 > 0 && col - 1 > 0) {
            end = new ChessPosition(row - 1, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // LEFT
        if (col - 1 > 0) {
            end = new ChessPosition(row, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // UPLEFT
        if (row + 1 < 9 && col - 1 > 0) {
            end = new ChessPosition(row + 1, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
    }
    private void knightMoves(ChessPosition startPosition, Collection<ChessMove> moveList, ChessBoard board) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        ChessPosition end;
        ChessMove move;
        // UP RIGHT
        if (row + 2 < 9 && col + 1 < 9) {
            end = new ChessPosition(row + 2, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // RIGHT UP
        if (row + 1 < 9 && col + 2 < 9) {
            end = new ChessPosition(row + 1, col + 2);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // RIGHT DOWN
        if (row - 1 > 0 && col + 2 < 9) {
            end = new ChessPosition(row - 1, col + 2);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // DOWN RIGHT
        if (row - 2 > 0 && col + 1 < 9) {
            end = new ChessPosition(row - 2, col + 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // DOWN LEFT
        if (row - 2 > 0 && col - 1 > 0) {
            end = new ChessPosition(row - 2, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // LEFT DOWN
        if (row - 1 > 0 && col - 2 > 0) {
            end = new ChessPosition(row - 1, col - 2);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // LEFT UP
        if (row + 1 < 9 && col - 2 > 0) {
            end = new ChessPosition(row + 1, col - 2);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
        // UP LEFT
        if (row + 2 < 9 && col - 1 > 0) {
            end = new ChessPosition(row + 2, col - 1);
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                }
            }
            else {
                move = new ChessMove(startPosition, end, null);
                moveList.add(move);
            }
        }
    }
    private void pawnMoves(ChessPosition startPosition, Collection<ChessMove> moveList, ChessBoard board) {
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        ChessPosition end;
        ChessMove move;
        ChessPosition test1;
        ChessPosition test2;
        boolean promotion;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            promotion = row + 1 == 8;
            if (col + 1 < 9) {
                test1 = new ChessPosition(row + 1, col + 1);
                if (occupied(board, test1) && enemy) {
                    if (promotion) {
                        move = new ChessMove(startPosition, test1, PieceType.BISHOP);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test1, PieceType.KNIGHT);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test1, PieceType.ROOK);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test1, PieceType.QUEEN);
                        moveList.add(move);
                    }
                    else {
                        move = new ChessMove(startPosition, test1, null);
                        moveList.add(move);
                    }
                }
            }
            if (col - 1 > 0) {
                test2 = new ChessPosition(row + 1, col - 1);
                if (occupied(board, test2) && enemy) {
                    if (promotion) {
                        move = new ChessMove(startPosition, test2, PieceType.BISHOP);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test2, PieceType.KNIGHT);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test2, PieceType.ROOK);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test2, PieceType.QUEEN);
                        moveList.add(move);
                    } else {
                        move = new ChessMove(startPosition, test2, null);
                        moveList.add(move);
                    }
                }
            }
            if (row != 2) {
                end = new ChessPosition(row + 1, col);
                if (!occupied(board, end)) {
                    if (promotion) {
                        move = new ChessMove(startPosition, end, PieceType.BISHOP);
                        moveList.add(move);
                        move = new ChessMove(startPosition, end, PieceType.KNIGHT);
                        moveList.add(move);
                        move = new ChessMove(startPosition, end, PieceType.ROOK);
                        moveList.add(move);
                        move = new ChessMove(startPosition, end, PieceType.QUEEN);
                        moveList.add(move);
                    }
                    else {
                        move = new ChessMove(startPosition, end, null);
                        moveList.add(move);
                    }
                }
            }
            else {
                end = new ChessPosition(row + 1, col);
                if (!occupied(board, end)) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                    end = new ChessPosition(row + 2, col);
                    if (!occupied(board, end)) {
                        move = new ChessMove(startPosition, end, null);
                        moveList.add(move);
                    }
                }
            }
        }
        else {
            promotion = row - 1 == 1;
            if (col + 1 < 9) {
                test1 = new ChessPosition(row - 1, col + 1);
                if (occupied(board, test1) && enemy) {
                    if (promotion) {
                        move = new ChessMove(startPosition, test1, PieceType.BISHOP);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test1, PieceType.KNIGHT);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test1, PieceType.ROOK);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test1, PieceType.QUEEN);
                        moveList.add(move);
                    }
                    else {
                        move = new ChessMove(startPosition, test1, null);
                        moveList.add(move);
                    }
                }
            }
            if (col - 1 > 0) {
                test2 = new ChessPosition(row - 1, col - 1);
                if (occupied(board, test2) && enemy) {
                    if (promotion) {
                        move = new ChessMove(startPosition, test2, PieceType.BISHOP);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test2, PieceType.KNIGHT);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test2, PieceType.ROOK);
                        moveList.add(move);
                        move = new ChessMove(startPosition, test2, PieceType.QUEEN);
                        moveList.add(move);
                    } else {
                        move = new ChessMove(startPosition, test2, null);
                        moveList.add(move);
                    }
                }
            }
            if (row != 7) {
                end = new ChessPosition(row - 1, col);
                if(!occupied(board, end)) {
                    if (promotion) {
                        move = new ChessMove(startPosition, end, PieceType.BISHOP);
                        moveList.add(move);
                        move = new ChessMove(startPosition, end, PieceType.KNIGHT);
                        moveList.add(move);
                        move = new ChessMove(startPosition, end, PieceType.ROOK);
                        moveList.add(move);
                        move = new ChessMove(startPosition, end, PieceType.QUEEN);
                        moveList.add(move);
                    }
                    else {
                        move = new ChessMove(startPosition, end, null);
                        moveList.add(move);
                    }
                }
            }
            else {
                end = new ChessPosition(row - 1, col);
                if (!occupied(board, end)) {
                    move = new ChessMove(startPosition, end, null);
                    moveList.add(move);
                    end = new ChessPosition(row - 2, col);
                    if (!occupied(board, end)) {
                        move = new ChessMove(startPosition, end, null);
                        moveList.add(move);
                    }
                }
            }
        }
    }
    private boolean occupied(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position) == null) {
            return false;
        }
        else {
            enemy = board.getPiece(position).pieceColor != pieceColor;
            return true;
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
