package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
        if (getPieceType() == PieceType.KING) {
            kingMoves(board, myPosition, moveList, false);
        }
        if (getPieceType() == PieceType.QUEEN) {
            queenMoves(board, myPosition, moveList, false);
        }
        if (getPieceType() == PieceType.BISHOP) {
            bishopMoves(board, myPosition, moveList, false);
        }
        if (getPieceType() == PieceType.KNIGHT) {
            knightMoves(board, myPosition, moveList, false);
        }
        if (getPieceType() == PieceType.ROOK) {
            rookMoves(board, myPosition, moveList, false);
        }
        if (getPieceType() == PieceType.PAWN) {
            pawnMoves(board, myPosition, moveList, false);
        }
        return moveList;
    }

    boolean enemy;
    private void kingMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        ChessPosition end;
        // Up
        if (row + 1 <= 8) {
            end = new ChessPosition(row + 1, col);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Up Right
        if (row + 1 <= 8 && col + 1 <= 8) {
            end = new ChessPosition(row + 1, col + 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Right
        if (col + 1 <= 8) {
            end = new ChessPosition(row, col + 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Down Right
        if (row - 1 >= 1 && col + 1 <= 8) {
            end = new ChessPosition(row - 1, col + 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Down
        if (row - 1 >= 1) {
            end = new ChessPosition(row - 1, col);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Down Left
        if (row - 1 >= 1 && col - 1 >= 1) {
            end = new ChessPosition(row - 1, col - 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Left
        if (col - 1 >= 1) {
            end = new ChessPosition(row, col - 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Up Left
        if (row + 1 <= 8 && col - 1 >= 1) {
            end = new ChessPosition(row + 1, col - 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
    }

    private void queenMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        // Up
        for (int i = 1; i < 8; i++) {
            if (row + i > 8) {
                break;
            }
            boolean done = moveDirection(pos, i, 0, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Up Right
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col + i > 8) {
                break;
            }
            boolean done = moveDirection(pos, i, i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Right
        for (int i = 1; i < 8; i++) {
            if (col + i > 8) {
                break;
            }
            boolean done = moveDirection(pos, 0, i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Down Right
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col + i > 8) {
                break;
            }
            boolean done = moveDirection(pos, -i, i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Down
        for (int i = 1; i < 8; i++) {
            if (row - i < 1) {
                break;
            }
            boolean done = moveDirection(pos, -i, 0, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Down Left
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col - i < 1) {
                break;
            }
            boolean done = moveDirection(pos, -i, -i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Left
        for (int i = 1; i < 8; i++) {
            if (col - i < 1) {
                break;
            }
            boolean done = moveDirection(pos, 0, -i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Up Left
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col - i < 1) {
                break;
            }
            boolean done = moveDirection(pos, i, -i, board, moveList, test);
            if (done) {
                break;
            }
        }
    }

    private void bishopMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        // Up Right
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col + i > 8) {
                break;
            }
            boolean done = moveDirection(pos, i, i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Down Right
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col + i > 8) {
                break;
            }
            boolean done = moveDirection(pos, -i, i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Down Left
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col - i < 1) {
                break;
            }
            boolean done = moveDirection(pos, -i, -i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Up Left
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col - i < 1) {
                break;
            }
            boolean done = moveDirection(pos, i, -i, board, moveList, test);
            if (done) {
                break;
            }
        }
    }

    private void knightMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        ChessPosition end;
        ChessMove move;
        // Up Right
        if (row + 2 <= 8 && col + 1 <= 8) {
            end = new ChessPosition(row + 2, col + 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Right Up
        if (row + 1 <= 8 && col + 2 <= 8) {
            end = new ChessPosition(row + 1, col + 2);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Right Down
        if (row - 1 >= 1 && col + 2 <= 8) {
            end = new ChessPosition(row - 1, col + 2);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Down Right
        if (row - 2 >= 1 && col + 1 <= 8) {
            end = new ChessPosition(row - 2, col + 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Down Left
        if (row - 2 >= 1 && col - 1 >= 1) {
            end = new ChessPosition(row - 2, col - 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Left Down
        if (row - 1 >= 1 && col - 2 >= 1) {
            end = new ChessPosition(row - 1, col - 2);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Left Up
        if (row + 1 <= 8 && col - 2 >= 1) {
            end = new ChessPosition(row + 1, col - 2);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
        // Up Left
        if (row + 2 <= 8 && col - 1 >= 1) {
            end = new ChessPosition(row + 2, col - 1);
            addMoveIfOccupied(board, pos, end, moveList, test);
        }
    }

    private void rookMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        // Up
        for (int i = 1; i < 8; i++) {
            if (row + i > 8) {
                break;
            }
            boolean done = moveDirection(pos, i, 0, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Right
        for (int i = 1; i < 8; i++) {
            if (col + i > 8) {
                break;
            }
            boolean done = moveDirection(pos, 0, i, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Down
        for (int i = 1; i < 8; i++) {
            if (row - i < 1) {
                break;
            }
            boolean done = moveDirection(pos, -i, 0, board, moveList, test);
            if (done) {
                break;
            }
        }
        // Left
        for (int i = 1; i < 8; i++) {
            if (col - i < 1) {
                break;
            }
            boolean done = moveDirection(pos, 0, -i, board, moveList, test);
            if (done) {
                break;
            }
        }
    }

    private void pawnMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            movePawn(pos, board, moveList, 1, test);
        }
        else {
            movePawn(pos, board, moveList, -1, test);
        }
    }

    private void movePawn(ChessPosition pos, ChessBoard board, Collection<ChessMove> list, int direction, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        ChessPosition oneSpace;
        ChessPosition diagonal1;
        ChessPosition diagonal2;
        boolean promotion;
        if (direction == 1) {
             promotion = row + 1 == 8;
        }
        else {
            promotion = row - 1 == 1;
        }
        if (row + direction <= 8) {
            oneSpace = new ChessPosition(row + direction, col);
            if ((row == 2 && direction == 1) || (row == 7 && direction == -1) && !occupied(board, oneSpace)) {
                var twoSpaces = new ChessPosition(row + (2 * direction), col);
                if (!occupied(board, twoSpaces)) {
                    list.add(new ChessMove(pos, twoSpaces, null));
                }
                list.add(new ChessMove(pos, oneSpace, null));
            } else {
                if (!occupied(board, oneSpace)) {
                    if (promotion) {
                        pawnPromotion(pos, oneSpace, list);
                    } else list.add(new ChessMove(pos, oneSpace, null));
                }
            }
            if (col + 1 <= 8) {
                diagonal1 = new ChessPosition(row + direction, col + 1);
                if (occupied(board, diagonal1) && enemy) {
                    if (promotion) {
                        pawnPromotion(pos, diagonal1, list);
                    } else list.add(new ChessMove(pos, diagonal1, null));
                } else if (occupied(board, diagonal1) && test) {
                    list.add(new ChessMove(pos, diagonal1, null));
                }
            }
            if (col - 1 >= 1) {
                diagonal2 = new ChessPosition(row + direction, col - 1);
                if (occupied(board, diagonal2) && enemy) {
                    if (promotion) {
                        pawnPromotion(pos, diagonal2, list);
                    } else list.add(new ChessMove(pos, diagonal2, null));
                } else if (occupied(board, diagonal2) && test) {
                    list.add(new ChessMove(pos, diagonal2, null));
                }
            }
        }
    }

    private boolean moveDirection(ChessPosition pos, int rowChange, int colChange, ChessBoard board, Collection<ChessMove> list, boolean test) {
        var row = pos.getRow();
        var col = pos.getColumn();
        var end = new ChessPosition(row + rowChange, col + colChange);
        if (occupied(board, end) && !enemy) {
            if (test) {
                list.add(new ChessMove(pos, end, null));
            }
            return true;
        }
        list.add(new ChessMove(pos, end, null));
        return occupied(board, end);
    }

    private boolean occupied(ChessBoard board, ChessPosition pos) {
        if (board.getPiece(pos) != null) {
            enemy = board.getPiece(pos).pieceColor != getTeamColor();
            return true;
        }
        return false;
    }

    private void addMoveIfOccupied(ChessBoard board, ChessPosition pos, ChessPosition end, Collection<ChessMove> list, boolean test) {
        if (occupied(board, end)) {
            if (enemy) {
                list.add(new ChessMove(pos, end, null));
            }
            else if (test) {
                list.add(new ChessMove(pos, end, null));
            }
        }
        else {
            list.add(new ChessMove(pos, end, null));
        }
    }

    private void pawnPromotion(ChessPosition start, ChessPosition end, Collection<ChessMove> moveList) {
        moveList.add(new ChessMove(start, end, PieceType.QUEEN));
        moveList.add(new ChessMove(start, end, PieceType.BISHOP));
        moveList.add(new ChessMove(start, end, PieceType.KNIGHT));
        moveList.add(new ChessMove(start, end, PieceType.ROOK));
    }

    public Collection<ChessMove> coveredSquares(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moveList = new HashSet<>();
        if (getPieceType() == PieceType.KING) kingMoves(board, myPosition, moveList, true);
        if (getPieceType() == PieceType.QUEEN) queenMoves(board, myPosition, moveList, true);
        if (getPieceType() == PieceType.BISHOP) bishopMoves(board, myPosition, moveList, true);
        if (getPieceType() == PieceType.KNIGHT) knightMoves(board, myPosition, moveList, true);
        if (getPieceType() == PieceType.ROOK) rookMoves(board, myPosition, moveList, true);
        if (getPieceType() == PieceType.PAWN) pawnMoves(board, myPosition, moveList, true);
        return moveList;
    }

    @Override
    public String toString() {
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (getPieceType() == PieceType.PAWN) return "P";
            if (getPieceType() == PieceType.ROOK) return "R";
            if (getPieceType() == PieceType.KNIGHT) return "N";
            if (getPieceType() == PieceType.BISHOP) return "B";
            if (getPieceType() == PieceType.QUEEN) return "Q";
            if (getPieceType() == PieceType.KING) return "K";
        }
        if (getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (getPieceType() == PieceType.PAWN) return "p";
            if (getPieceType() == PieceType.ROOK) return "r";
            if (getPieceType() == PieceType.KNIGHT) return "n";
            if (getPieceType() == PieceType.BISHOP) return "b";
            if (getPieceType() == PieceType.QUEEN) return "q";
            if (getPieceType() == PieceType.KING) return "k";
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}