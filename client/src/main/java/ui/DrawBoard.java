package ui;

import chess.*;

import java.util.Collection;

import static utilities.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARD_SIZE = 8;

    public static void printBoardWhite(ChessBoard board) {
        String[] cols = {"a", "b", "c", "d", "e", "f", "g", "h"};
        printBorder(cols);
        var boardString = convertBoardWhite(board);
        for (int row = 7; row >= 0; row--) {
            drawRow(row, boardString[row], null);
        }
        printBorder(cols);
    }

    public static void printHighlightsWhite(ChessBoard board, Collection<ChessMove> moves) {
        int[][] highlights = new int[8][8];
        ChessPosition end;
        ChessPosition start = moves.iterator().next().getStartPosition();
        highlights[start.getRow() - 1][start.getColumn() - 1] = 2;
        String[] cols = {"a", "b", "c", "d", "e", "f", "g", "h"};
        printBorder(cols);
        for (ChessMove move : moves) {
            end = move.getEndPosition();
            highlights[end.getRow() - 1][end.getColumn() - 1] = 1;
        }
        var boardString = convertBoardWhite(board);
        for (int row = 7; row >= 0; row--) {
            drawRow(row, boardString[row], highlights[row]);
        }
        printBorder(cols);
    }

    public static void printBoardBlack(ChessBoard board) {
        String[] cols = {"h", "g", "f", "e", "d", "c", "b", "a"};
        printBorder(cols);
        var boardString = convertBoardBlack(board);
        for (int row = 0; row < BOARD_SIZE; row++) {
            drawRow(row, boardString[row], null);
        }
        printBorder(cols);
    }

    public static void printHighlightsBlack(ChessBoard board, Collection<ChessMove> moves) {
        int[][] highlights = new int[8][8];
        ChessPosition end;
        ChessPosition start = moves.iterator().next().getStartPosition();
        highlights[start.getRow() - 1][8 - start.getColumn()] = 2;
        String[] cols = {"h", "g", "f", "e", "d", "c", "b", "a"};
        printBorder(cols);
        for (ChessMove move : moves) {
            end = move.getEndPosition();
            highlights[end.getRow() - 1][8 - end.getColumn()] = 1;
        }
        var boardString = convertBoardBlack(board);
        for (int row = 0; row < 8; row++) {
            drawRow(row, boardString[row], highlights[row]);
        }
        printBorder(cols);
    }

    private static void drawRow(int row, String[] pieces, int[] highlights) {
        int rank = row + 1;
        String bgColor;
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "\u2004\u2002" + rank + "\u2004\u2002" + RESET_BG_COLOR);
        if (highlights == null) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean isDark = (row + col) % 2 == 1;
                bgColor = isDark ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE;
                String piece = pieces[col];
                System.out.print(bgColor + piece + RESET_BG_COLOR);
            }
        }
        else {
            for (int col = 0; col < BOARD_SIZE; col++) {
                boolean isDark = (row + col) % 2 == 1;
                if (highlights[col] == 1) {
                    bgColor = isDark ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN;
                }
                else if (highlights[col] == 2) {
                    bgColor = SET_BG_COLOR_YELLOW;
                }
                else {
                    bgColor = isDark ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE;
                }
                String piece = pieces[col];
                System.out.print(bgColor + piece + RESET_BG_COLOR);
            }
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + "\u2004\u2002" + rank + "\u2004\u2002" + RESET_BG_COLOR);
    }

    private static void printBorder(String[] cols) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + EMPTY);
        for (String col : cols) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + "\u2004\u2002" + SET_TEXT_COLOR_BLACK + col + "\u2004\u2002");
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + EMPTY + RESET_BG_COLOR + RESET_TEXT_COLOR);
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
