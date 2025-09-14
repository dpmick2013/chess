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

    private void kingMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
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
            if (board.getPiece(end) != null && board.getPiece(end).pieceColor == getTeamColor()) break;
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (board.getPiece(end) != null) break;
        }
        // Down Right
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col + i > 8) break;
            var end = new ChessPosition(row - i, col + i);
            ChessMove move;
            if (board.getPiece(end) != null && board.getPiece(end).pieceColor == getTeamColor()) break;
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (board.getPiece(end) != null) break;
        }
        // Down Left
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col - i < 1) break;
            var end = new ChessPosition(row - i, col - i);
            ChessMove move;
            if (board.getPiece(end) != null && board.getPiece(end).pieceColor == getTeamColor()) break;
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (board.getPiece(end) != null) break;
        }
        // Up Left
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col - i < 1) break;
            var end = new ChessPosition(row + i, col - i);
            ChessMove move;
            if (board.getPiece(end) != null && board.getPiece(end).pieceColor == getTeamColor()) break;
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (board.getPiece(end) != null) break;
        }
    }

    private void knightMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
    }

    private void rookMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
    }

    private void pawnMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList) {
        int row = pos.getRow();
        int col = pos.getColumn();
    }
}
