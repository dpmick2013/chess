package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private final ChessBoard gameBoard = new ChessBoard();
    private TeamColor teamTurn = TeamColor.WHITE;
    public ChessGame() {
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        if (piece == null) return null;
        Collection<ChessMove> moves = piece.pieceMoves(gameBoard, startPosition),
                valid = piece.pieceMoves(gameBoard, startPosition);
        for (ChessMove move : moves) {
            try {
                testMove(move);
            } catch (InvalidMoveException ex) {
                valid.remove(move);
            }
        }
        return valid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (getTeamTurn() != gameBoard.getPiece(move.getStartPosition()).getTeamColor())
            throw new InvalidMoveException("Not your turn");
        Collection<ChessMove> valid;
        valid = validMoves(move.getStartPosition());
        if (!valid.contains(move))
            throw new InvalidMoveException("Invalid Move");

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king_pos = findKing(teamColor);
        return isAttacked(gameBoard, king_pos, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean no_moves = false;
        ChessPosition king_pos = findKing(teamColor);
        ChessPosition move_pos;
        ChessPiece king = gameBoard.getPiece(king_pos);
        Collection<ChessMove> moves = king.pieceMoves(gameBoard, king_pos);
        for (ChessMove move : moves) {
            move_pos = move.getEndPosition();
            if (isAttacked(gameBoard, move_pos, teamColor)) {
                no_moves = true;
                continue;
            }
            no_moves = false;
            break;
        }
        if (no_moves) {
            return isInCheck(teamColor);
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean no_moves = false;
        ChessPosition king_pos = findKing(teamColor);
        ChessPosition move_pos;
        ChessPiece king = gameBoard.getPiece(king_pos);
        Collection<ChessMove> moves = king.pieceMoves(gameBoard, king_pos);
        for (ChessMove move : moves) {
            move_pos = move.getEndPosition();
            if (isAttacked(gameBoard, move_pos, teamColor)) {
                no_moves = true;
                continue;
            }
            no_moves = false;
            break;
        }
        if (no_moves) {
            return !isInCheck(teamColor);
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        ChessPiece temp_piece;
        ChessPosition temp_pos;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                temp_pos = new ChessPosition(i, j);
                temp_piece = board.getPiece(temp_pos);
                gameBoard.addPiece(temp_pos, temp_piece);
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    private ChessPosition findKing(TeamColor team) {
        ChessPiece test_piece;
        ChessPosition test_pos;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                test_pos = new ChessPosition(i, j);
                test_piece = gameBoard.getPiece(test_pos);
                if (test_piece == null) continue;
                if (test_piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (test_piece.getTeamColor() == team) {
                        return test_pos;
                    }
                }
            }
        }
        return null;
    }

    private boolean isAttacked (ChessBoard board, ChessPosition pos, TeamColor teamColor) {
        ChessPosition test_pos;
        ChessPosition move_pos;
        ChessPiece test_piece;
        Collection<ChessMove> test_list;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                test_pos = new ChessPosition(i, j);
                test_piece = board.getPiece(test_pos);
                if (test_piece == null) continue;
                if (test_piece.getTeamColor() != teamColor) {
                    test_list = test_piece.coveredSquares(board, test_pos);
                    if (test_piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        if (test_piece.getTeamColor() == TeamColor.WHITE) {
                            test_list.add(new ChessMove(test_pos, new ChessPosition(i + 1, j + 1), null));
                            test_list.add(new ChessMove(test_pos, new ChessPosition(i + 1, j - 1), null));
                        }
                        else {
                            test_list.add(new ChessMove(test_pos, new ChessPosition(i - 1, j + 1), null));
                            test_list.add(new ChessMove(test_pos, new ChessPosition(i - 1, j - 1), null));
                        }
                    }
                    for (ChessMove move : test_list) {
                        move_pos = move.getEndPosition();
                        if (move_pos.getRow() == pos.getRow() && move_pos.getColumn() == pos.getColumn()) return true;
                    }
                }
            }
        }
        return false;
    }

    private void movePiece(ChessMove move, ChessBoard board) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        if (board.getPiece(end) != null) board.removePiece(end);
        board.addPiece(end, board.getPiece(start));
        board.removePiece(start);
    }

    private void testMove(ChessMove move) throws InvalidMoveException {
        ChessBoard test_board = new ChessBoard(gameBoard);
        TeamColor color = test_board.getPiece(move.getStartPosition()).getTeamColor();
        movePiece(move, test_board);
        if (isAttacked(test_board, findKing(color), color)) {
            throw new InvalidMoveException();
        }
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "gameBoard=" + gameBoard +
                ", teamTurn=" + teamTurn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }
}
