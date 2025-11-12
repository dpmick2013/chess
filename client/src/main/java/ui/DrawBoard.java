package ui;

import chess.ChessGame;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARD_SIZE = 8;

//    private static final String[][] board = {
//            {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK},
//            {BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN},
//            {"    ", "    ", "    ", "    ", "    ", "    ", "    ", "    "},
//            {"    ", "    ", "    ", "    ", "    ", "    ", "    ", "    "},
//            {"    ", "    ", "    ", "    ", "    ", "    ", "    ", "    "},
//            {"    ", "    ", "    ", "    ", "    ", "    ", "    ", "    "},
//            {WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN},
//            {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK}
//    };
    private static final String[][] board = {
        {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "},
        {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
        {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "},
        {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "},
        {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "},
        {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "},
        {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "},
        {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "},
    };

    public static void main(String[] args) {
        printBoardWhite();
        printBoardBlack();
    }

    public static void printBoardWhite() {
        String[] cols = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] backRow = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
        String[] pawnRow = {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "};
        String[] emptyRow = {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "};
        printBorder(cols);
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (row == 0) {
                drawRow(ChessGame.TeamColor.BLACK, row, backRow);
            }
            else if (row == 1) {
                drawRow(ChessGame.TeamColor.BLACK, row, pawnRow);
            }
            else if (row == 6) {
                drawRow(ChessGame.TeamColor.WHITE, row, pawnRow);
            }
            else if (row == 7) {
                drawRow(ChessGame.TeamColor.WHITE, row, backRow);
            }
            else {
                drawRow(ChessGame.TeamColor.WHITE, row, emptyRow);
            }
        }
        printBorder(cols);
    }

    public static void printBoardBlack() {
        String[] cols = {"h", "g", "f", "e", "d", "c", "b", "a"};
        String[] backRow = {" R ", " N ", " B ", " K ", " Q ", " B ", " N ", " R "};
        String[] pawnRow = {" P ", " P ", " P ", " P ", " P ", " P ", " P ", " P "};
        String[] emptyRow = {"   ", "   ", "   ", "   ", "   ", "   ", "   ", "   "};
        printBorder(cols);
        for (int row = 0; row < BOARD_SIZE; row++) {
            if (row == 0) {
                drawRow(ChessGame.TeamColor.WHITE, row, backRow);
            }
            else if (row == 1) {
                drawRow(ChessGame.TeamColor.WHITE, row, pawnRow);
            }
            else if (row == 6) {
                drawRow(ChessGame.TeamColor.BLACK, row, pawnRow);
            }
            else if (row == 7) {
                drawRow(ChessGame.TeamColor.BLACK, row, backRow);
            }
            else {
                drawRow(ChessGame.TeamColor.WHITE, row, emptyRow);
            }
        }
        printBorder(cols);
    }

    public static void drawRow(ChessGame.TeamColor color, int row, String[] pieces) {
        int rank = (color == ChessGame.TeamColor.WHITE) ? 8 - row : row + 1;
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + rank + " " + RESET_BG_COLOR);
        for (int col = 0; col < BOARD_SIZE; col++) {
            boolean isDark = (row + col) % 2 == 1;
            String bgColor = isDark ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE;
            String piece = pieces[col];
            String pieceColor = (color == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE;
            System.out.print(bgColor + pieceColor + piece + RESET_BG_COLOR + RESET_TEXT_COLOR);
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + rank + " " + RESET_BG_COLOR);
    }

    public static void printBorder(String[] cols) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        for (String col : cols) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + " " + SET_TEXT_COLOR_BLACK + col + " ");
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + "   " + RESET_BG_COLOR);
    }
}
