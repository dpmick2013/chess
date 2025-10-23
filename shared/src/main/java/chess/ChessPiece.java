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
        if (getPieceType() == PieceType.KING) kingMoves(board, myPosition, moveList, false);
        if (getPieceType() == PieceType.QUEEN) queenMoves(board, myPosition, moveList, false);
        if (getPieceType() == PieceType.BISHOP) bishopMoves(board, myPosition, moveList, false);
        if (getPieceType() == PieceType.KNIGHT) knightMoves(board, myPosition, moveList, false);
        if (getPieceType() == PieceType.ROOK) rookMoves(board, myPosition, moveList, false);
        if (getPieceType() == PieceType.PAWN) pawnMoves(board, myPosition, moveList, false);
        return moveList;
    }

    boolean enemy;
    private void kingMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));

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
                else if (test) moveList.add(new ChessMove(pos, end, null));
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
    }

    private void queenMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        // Up
        for (int i = 1; i < 8; i++) {
            if (row + i > 8) break;
            var end = new ChessPosition(row + i, col);
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            moveList.add(new ChessMove(pos, end, null));
            if (occupied(board, end)) break;
        }
        // Up Right
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col + i > 8) break;
            var end = new ChessPosition(row + i, col + i);
            ChessMove move;
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Right
        for (int i = 1; i < 8; i++) {
            if (col + i > 8) break;
            var end = new ChessPosition(row, col + i);
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            moveList.add(new ChessMove(pos, end, null));
            if (occupied(board, end)) break;
        }
        // Down Right
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col + i > 8) break;
            var end = new ChessPosition(row - i, col + i);
            ChessMove move;
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Down
        for (int i = 1; i < 8; i++) {
            if (row - i < 1) break;
            var end = new ChessPosition(row - i, col);
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            moveList.add(new ChessMove(pos, end, null));
            if (occupied(board, end)) break;
        }
        // Down Left
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col - i < 1) break;
            var end = new ChessPosition(row - i, col - i);
            ChessMove move;
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Left
        for (int i = 1; i < 8; i++) {
            if (col - i < 1) break;
            var end = new ChessPosition(row, col - i);
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            moveList.add(new ChessMove(pos, end, null));
            if (occupied(board, end)) break;
        }
        // Up Left
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col - i < 1) break;
            var end = new ChessPosition(row + i, col - i);
            ChessMove move;
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
    }

    private void bishopMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        // Up Right
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col + i > 8) break;
            var end = new ChessPosition(row + i, col + i);
            ChessMove move;
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Down Right
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col + i > 8) break;
            var end = new ChessPosition(row - i, col + i);
            ChessMove move;
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Down Left
        for (int i = 1; i < 8; i++) {
            if (row - i < 1 || col - i < 1) break;
            var end = new ChessPosition(row - i, col - i);
            ChessMove move;
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
        }
        // Up Left
        for (int i = 1; i < 8; i++) {
            if (row + i > 8 || col - i < 1) break;
            var end = new ChessPosition(row + i, col - i);
            ChessMove move;
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            move = new ChessMove(pos, end, null);
            moveList.add(move);
            if (occupied(board, end)) break;
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
            if (occupied(board, end)) {
                if (enemy) {
                    move = new ChessMove(pos, end, null);
                    moveList.add(move);
                }
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
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
                else if (test) moveList.add(new ChessMove(pos, end, null));
            }
            else {
                move = new ChessMove(pos, end, null);
                moveList.add(move);
            }
        }
    }

    private void rookMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        // Up
        for (int i = 1; i < 8; i++) {
            if (row + i > 8) break;
            var end = new ChessPosition(row + i, col);
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            moveList.add(new ChessMove(pos, end, null));
            if (occupied(board, end)) break;
        }
        // Right
        for (int i = 1; i < 8; i++) {
            if (col + i > 8) break;
            var end = new ChessPosition(row, col + i);
            if (occupied(board, end) && !enemy) break;
            moveList.add(new ChessMove(pos, end, null));
            if (occupied(board, end)) break;
        }
        // Down
        for (int i = 1; i < 8; i++) {
            if (row - i < 1) break;
            var end = new ChessPosition(row - i, col);
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            moveList.add(new ChessMove(pos, end, null));
            if (occupied(board, end)) break;
        }
        // Left
        for (int i = 1; i < 8; i++) {
            if (col - i < 1) break;
            var end = new ChessPosition(row, col - i);
            if (occupied(board, end) && !enemy) {
                if (test) moveList.add(new ChessMove(pos, end, null));
                break;
            }
            moveList.add(new ChessMove(pos, end, null));
            if (occupied(board, end)) break;
        }
    }

    private void pawnMoves(ChessBoard board, ChessPosition pos, Collection<ChessMove> moveList, boolean test) {
        int row = pos.getRow();
        int col = pos.getColumn();
        ChessPosition oneSpace;
        ChessPosition diagonal1;
        ChessPosition diagonal2;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            var promotion = row + 1 == 8;
            if (row + 1 <= 8) {
                oneSpace = new ChessPosition(row + 1, col);
                if (row == 2 && !occupied(board, oneSpace)) {
                    var twoSpaces = new ChessPosition(row + 2, col);
                    if (!occupied(board, twoSpaces)) {
                        moveList.add(new ChessMove(pos, twoSpaces, null));
                    }
                    moveList.add(new ChessMove(pos, oneSpace, null));
                }
                else if (row != 2){
                    if (!occupied(board, oneSpace)) {
                        if (promotion) {
                            pawnPromotion(pos, oneSpace, moveList);
                        }
                        else moveList.add(new ChessMove(pos, oneSpace, null));
                    }
                }
                if (col + 1 <= 8) {
                    diagonal1 = new ChessPosition(row + 1, col + 1);
                    if (occupied(board, diagonal1) && enemy) {
                        if (promotion) {
                            pawnPromotion(pos, diagonal1, moveList);
                        } else moveList.add(new ChessMove(pos, diagonal1, null));
                    }
                    else if (occupied(board, diagonal1) && test) {
                        moveList.add(new ChessMove(pos, diagonal1, null));
                    }
                }
                if (col - 1 >= 1) {
                    diagonal2 = new ChessPosition(row + 1, col - 1);
                    if (occupied(board, diagonal2) && enemy) {
                        if (promotion) {
                            pawnPromotion(pos, diagonal2, moveList);
                        } else moveList.add(new ChessMove(pos, diagonal2, null));
                    }
                    else if (occupied(board, diagonal2) && test) {
                        moveList.add(new ChessMove(pos, diagonal2, null));
                    }
                }
            }
        }
        else {
            var promotion = row - 1 == 1;
            if (row - 1 >= 1) {
                oneSpace = new ChessPosition(row - 1, col);
                if (row == 7 && !occupied(board, oneSpace)) {
                    var twoSpaces = new ChessPosition(row - 2, col);
                    if (!occupied(board, twoSpaces)) {
                        moveList.add(new ChessMove(pos, twoSpaces, null));
                    }
                    moveList.add(new ChessMove(pos, oneSpace, null));
                }
                else if (row != 7) {
                    if (!occupied(board, oneSpace)) {
                        if (promotion) {
                            pawnPromotion(pos, oneSpace, moveList);
                        } else moveList.add(new ChessMove(pos, oneSpace, null));
                    }
                }
                if (col + 1 <= 8) {
                    diagonal1 = new ChessPosition(row - 1, col + 1);
                    if (occupied(board, diagonal1) && enemy) {
                        if (promotion) {
                            pawnPromotion(pos, diagonal1, moveList);
                        } else moveList.add(new ChessMove(pos, diagonal1, null));
                    }
                    else if (occupied(board, diagonal1) && test) {
                        moveList.add(new ChessMove(pos, diagonal1, null));
                    }
                }
                if (col - 1 >= 1) {
                    diagonal2 = new ChessPosition(row - 1, col - 1);
                    if (occupied(board, diagonal2) && enemy) {
                        if (promotion) {
                            pawnPromotion(pos, diagonal2, moveList);
                        } else moveList.add(new ChessMove(pos, diagonal2, null));
                    }
                    else if (occupied(board, diagonal2) && test) {
                        moveList.add(new ChessMove(pos, diagonal2, null));
                    }
                }
            }
        }
    }

    private boolean occupied(ChessBoard board, ChessPosition pos) {
        if (board.getPiece(pos) != null) {
            enemy = board.getPiece(pos).pieceColor != getTeamColor();
            return true;
        }
        return false;
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
