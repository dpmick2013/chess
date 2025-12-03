package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARD_SIZE = 8;

    public static void printBoardWhite(ChessBoard board) {
        String[] cols = {"a", "b", "c", "d", "e", "f", "g", "h"};
        printBorder(cols);
        var boardString = convertBoardWhite(board);
        for (int row = 7; row >= 0; row--) {
            drawRow(ChessGame.TeamColor.WHITE, row, boardString[row]);
        }
        printBorder(cols);
    }

    public static void printBoardBlack(ChessBoard board) {
        String[] cols = {"h", "g", "f", "e", "d", "c", "b", "a"};
        printBorder(cols);
        var boardString = convertBoardBlack(board);
        for (int row = 0; row < BOARD_SIZE; row++) {
            drawRow(ChessGame.TeamColor.BLACK, row, boardString[row]);
        }
        printBorder(cols);
    }

    private static void drawRow(ChessGame.TeamColor color, int row, String[] pieces) {
        int rank = (color == ChessGame.TeamColor.WHITE) ? 8 - row : row + 1;
        System.out.print(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + "\u2004\u2002" + rank + "\u2004\u2002" + RESET_BG_COLOR);
        for (int col = 0; col < BOARD_SIZE; col++) {
            boolean isDark = (row + col) % 2 == 1;
            String bgColor = isDark ? SET_BG_COLOR_LIGHT_GREY : SET_BG_COLOR_WHITE;
            String piece = pieces[col];
            System.out.print(bgColor + piece + RESET_BG_COLOR);
        }
        System.out.println(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE + "\u2004\u2002" + rank + "\u2004\u2002" + RESET_BG_COLOR);
    }

    private static void printBorder(String[] cols) {
        System.out.print(SET_BG_COLOR_BLACK + EMPTY);
        for (String col : cols) {
            System.out.print(SET_BG_COLOR_BLACK + "\u2004\u2002" + SET_TEXT_COLOR_WHITE + col + "\u2004\u2002");
        }
        System.out.println(SET_BG_COLOR_BLACK + EMPTY + RESET_BG_COLOR + RESET_TEXT_COLOR);
    }

    private static String[][] convertBoardWhite(ChessBoard board) {
        String[][] boardString = new String[8][8];
        var pieceString = "";
        ChessPiece piece;
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    boardString[row - 1][col - 1] = " \u2003 ";
                }
                else {
                    pieceString = piece.toString();
                    boardString[row - 1][col - 1] = pieceString;
                }
            }
        }
        return boardString;
    }

    private static String[][] convertBoardBlack(ChessBoard board) {
        String[][] boardString = new String[8][8];
        var pieceString = "";
        ChessPiece piece;
        for (int row = 1; row < 9; row++) {
            for (int col = 8; col > 0; col--) {
                piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    boardString[row - 1][8 - col] = " \u2003 ";
                }
                else {
                    pieceString = piece.toString();
                    boardString[row - 1][8 - col] = pieceString;
                }
            }
        }
        return boardString;
    }
}
