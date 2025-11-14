package ui;

import chess.ChessGame;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARD_SIZE = 8;
    private static final String[] whiteBackRow =
        {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
    private static final String[] blackBackRow =
        {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
    private static final String[] whitePawnRow =
        {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN};
    private static final String[] blackPawnRow =
        {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN};
    private static final String[] emptyRow =
        {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};

    public static void printBoardWhite() {
        String[] cols = {"a", "b", "c", "d", "e", "f", "g", "h"};
        printBorder(cols);
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (row == 0) {
                drawRow(ChessGame.TeamColor.BLACK, row, blackBackRow);
            }
            else if (row == 1) {
                drawRow(ChessGame.TeamColor.BLACK, row, blackPawnRow);
            }
            else if (row == 6) {
                drawRow(ChessGame.TeamColor.WHITE, row, whitePawnRow);
            }
            else if (row == 7) {
                drawRow(ChessGame.TeamColor.WHITE, row, whiteBackRow);
            }
            else {
                drawRow(ChessGame.TeamColor.WHITE, row, emptyRow);
            }
        }
        printBorder(cols);
    }

    public static void printBoardBlack() {
        String[] cols = {"h", "g", "f", "e", "d", "c", "b", "a"};
        var temp1 = whiteBackRow[3];
        var temp2 = blackBackRow[3];
        whiteBackRow[3] = whiteBackRow[4];
        blackBackRow[3] = blackBackRow[4];
        whiteBackRow[4] = temp1;
        blackBackRow[4] = temp2;
        printBorder(cols);
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (row == 0) {
                drawRow(ChessGame.TeamColor.WHITE, row, whiteBackRow);
            }
            else if (row == 1) {
                drawRow(ChessGame.TeamColor.WHITE, row, whitePawnRow);
            }
            else if (row == 6) {
                drawRow(ChessGame.TeamColor.BLACK, row, blackPawnRow);
            }
            else if (row == 7) {
                drawRow(ChessGame.TeamColor.BLACK, row, blackBackRow);
            }
            else {
                drawRow(ChessGame.TeamColor.WHITE, row, emptyRow);
            }
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
            String pieceColor = (color == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_BLUE : SET_TEXT_COLOR_RED;
            System.out.print(bgColor + pieceColor + piece + RESET_BG_COLOR);
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
}
