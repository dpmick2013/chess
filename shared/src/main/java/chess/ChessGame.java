package chess;

import java.util.Collection;
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
        if (gameBoard.getPiece(move.startPosition()) == null)
            throw new InvalidMoveException("No piece");
        if (getTeamTurn() != gameBoard.getPiece(move.startPosition()).getTeamColor())
            throw new InvalidMoveException("Not your turn");
        Collection<ChessMove> valid;
        valid = validMoves(move.startPosition());
        if (!valid.contains(move))
            throw new InvalidMoveException("Invalid Move");
        movePiece(move, gameBoard);
        if (getTeamTurn() == TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);
        else setTeamTurn(TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = findKing(gameBoard, teamColor);
        return checkIfAttacked(gameBoard, kingPos, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean noMoves = false;
        ChessPosition kingPos = findKing(gameBoard, teamColor);
        ChessPosition movePos;
        assert kingPos != null;
        ChessPiece king = gameBoard.getPiece(kingPos);
        Collection<ChessMove> moves = king.pieceMoves(gameBoard, kingPos);
        for (ChessMove move : moves) {
            movePos = move.endPosition();
            if (checkIfAttacked(gameBoard, movePos, teamColor)) {
                noMoves = true;
                continue;
            }
            noMoves = false;
            break;
        }
        if (noMoves && checkIfStuck(gameBoard, teamColor)) {
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
        boolean noMoves = false;
        ChessPosition kingPos = findKing(gameBoard, teamColor);
        ChessPosition movePos;
        assert kingPos != null;
        ChessPiece king = gameBoard.getPiece(kingPos);
        Collection<ChessMove> moves = king.pieceMoves(gameBoard, kingPos);
        for (ChessMove move : moves) {
            movePos = move.endPosition();
            if (checkIfAttacked(gameBoard, movePos, teamColor)) {
                noMoves = true;
                continue;
            }
            noMoves = false;
            break;
        }
        if (noMoves && checkIfStuck(gameBoard, teamColor)) {
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
        ChessPiece tempPiece;
        ChessPosition tempPos;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                tempPos = new ChessPosition(i, j);
                tempPiece = board.getPiece(tempPos);
                gameBoard.addPiece(tempPos, tempPiece);
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

    private ChessPosition findKing(ChessBoard board, TeamColor team) {
        ChessPiece testPiece;
        ChessPosition testPos;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                testPos = new ChessPosition(i, j);
                testPiece = board.getPiece(testPos);
                if (testPiece == null) continue;
                if (testPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (testPiece.getTeamColor() == team) {
                        return testPos;
                    }
                }
            }
        }
        return null;
    }

    private boolean checkIfAttacked(ChessBoard board, ChessPosition pos, TeamColor teamColor) {
        ChessPosition testPos;
        ChessPosition movePos;
        ChessPiece testPiece;
        Collection<ChessMove> testList;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                testPos = new ChessPosition(i, j);
                testPiece = board.getPiece(testPos);
                if (testPiece == null) continue;
                if (testPiece.getTeamColor() != teamColor) {
                    testList = testPiece.coveredSquares(board, testPos);
                    if (testPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        if (testPiece.getTeamColor() == TeamColor.WHITE) {
                            testList.add(new ChessMove(testPos, new ChessPosition(i + 1, j + 1), null));
                            testList.add(new ChessMove(testPos, new ChessPosition(i + 1, j - 1), null));
                        }
                        else {
                            testList.add(new ChessMove(testPos, new ChessPosition(i - 1, j + 1), null));
                            testList.add(new ChessMove(testPos, new ChessPosition(i - 1, j - 1), null));
                        }
                    }
                    for (ChessMove move : testList) {
                        movePos = move.endPosition();
                        if (movePos.getRow() == pos.getRow() && movePos.getColumn() == pos.getColumn()) return true;
                    }
                }
            }
        }
        return false;
    }

    private void movePiece(ChessMove move, ChessBoard board) {
        ChessPosition start = move.startPosition();
        ChessPosition end = move.endPosition();
        ChessPiece promotion;
        if (move.promotionPiece() == null) {
            if (board.getPiece(end) != null) board.removePiece(end);
            board.addPiece(end, board.getPiece(start));
        }
        else {
            if (board.getPiece(end) != null) board.removePiece(end);
            promotion = new ChessPiece(board.getPiece(start).getTeamColor(), move.promotionPiece());
            board.addPiece(end, promotion);
        }
        board.removePiece(start);
    }

    private void testMove(ChessMove move) throws InvalidMoveException {
        ChessBoard testBoard = new ChessBoard(gameBoard);
        TeamColor color = testBoard.getPiece(move.startPosition()).getTeamColor();
        movePiece(move, testBoard);
        if (checkIfAttacked(testBoard, findKing(testBoard, color), color)) {
            throw new InvalidMoveException();
        }
    }

    private boolean checkIfStuck(ChessBoard board, TeamColor color) {
        ChessPosition testPos;
        ChessPiece testPiece;
        Collection<ChessMove> testList;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                testPos = new ChessPosition(i, j);
                testPiece = board.getPiece(testPos);
                if (testPiece == null) continue;
                if (testPiece.getTeamColor() == color) {
                    testList = validMoves(testPos);
                    if (!testList.isEmpty()) return false;
                }
            }
        }
        return true;
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
