package ui;

import chess.*;
import client.ServerFacade;
import datamodel.GameResult;
import datamodel.UserData;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ChessClient {

    private State state = State.LOGGEDOUT;
    private final ServerFacade server;
    private String authToken;
    private ChessGame gameObject;
    private ChessBoard board;
    private ChessGame.TeamColor teamColor;
    boolean waiting;
    boolean confirmed;
    boolean promoting;

    public ChessClient(String serverURL) {
        server = new ServerFacade(serverURL);
    }

    public enum State {
        LOGGEDOUT,
        LOGGEDIN,
        INGAME
    }

    public void run() {
        System.out.print("Welcome to Chess. Type help to get started");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = eval(line);
                System.out.print(result);
            } catch (Throwable ex) {
                var msg = ex.toString();
                System.out.print(msg);
            }

        }
    }

    public void printPrompt() {
        switch (state) {
            case LOGGEDOUT -> System.out.print("\n[LOGGED_OUT]" + " >>> ");
            case LOGGEDIN -> System.out.print("\n[LOGGED_IN]" + " >>> ");
            case INGAME -> System.out.print("\n[IN_GAME]" + " >>> ");
        }
    }

    public String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            String str = "";
            if (waiting) {
                switch (cmd) {
                    case "y" -> {
                        waiting = false;
                        confirmed = true;
                        str = resign();
                    }
                    case "n" -> {
                        waiting = false;
                        confirmed = false;
                        str = "Resign canceled";
                    }
                    default -> str = "Invalid input";
                }
                return str;
            }
            if (promoting) {
                switch (cmd) {
                    case "Q" -> {
                        str = move(ChessPiece.PieceType.QUEEN, params);
                    }
                    case "R" -> {
                        str = move(ChessPiece.PieceType.ROOK, params);
                    }
                    case "B" -> {
                        str = move(ChessPiece.PieceType.BISHOP, params);
                    }
                    case "N" -> {
                        str = move(ChessPiece.PieceType.KNIGHT, params);
                    }
                }
                return str;
            }
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                case "redraw" -> redraw();
                case "highlight" -> highlight(params);
                case "move" -> move(null, params);
                case "resign" -> resign();
                case "leave" -> leave();
                case "quit" -> "quit";
                default -> help();
            };
        } catch(Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (state == State.LOGGEDIN) {
            return "You are already logged in";
        }
        if (params.length >= 3) {
            var user = new UserData(params[0], params[1], params[2]);
            var auth = server.register(user);
            authToken = auth.authToken();
            state = State.LOGGEDIN;
            return String.format("You registered as %s", auth.username());
        }
        else {
            return "Missing inputs";
        }
    }

    public String login(String... params) throws Exception {
        if (params.length >= 2) {
            var user = new UserData(params[0], params[1], null);
            var auth = server.login(user);
            authToken = auth.authToken();
            state = State.LOGGEDIN;
            return String.format("logged in as %s", auth.username());
        }
        else {
            return "Expecting <USERNAME> <PASSWORD>";
        }
    }

    private String create(String... params) throws Exception {
        assertLoggedIn();
        if (params.length == 1) {
            var id = server.createGame(params[0], authToken);
            return String.format("Game created: \"%s\"; id: %d", params[0], id);
        }
        else {
            return "Expecting <NAME>";
        }
    }

    private String list() throws Exception {
        assertLoggedIn();
        var result = new StringBuilder();
        result.append("List of all of the games -\n\n");
        var games = server.listGames(authToken);
        int count = 0;
        for (GameResult game : games) {
            count++;
            var name = game.gameName();
            var white = game.whiteUsername();
            var black = game.blackUsername();
            result.append(String.format("Game %d: \"%s\" - [White: %s, Black: %s]\n", count, name, white, black));
        }
        return result.toString();
    }

    private String join(String... params) throws Exception {
        assertLoggedIn();
        if (params.length == 2) {
            int id;
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                return "Expected a number for <ID>";
            }
            String color = params[1];
            color = color.toUpperCase();
            var list = server.listGames(authToken);
            if (id > list.size() || id < 1) {
                return "Game does not exist";
            }
            var game = list.get(id - 1);
            server.joinGame(color, game.gameID(), authToken);
            gameObject = new ChessGame();
            board = gameObject.getBoard();
            if (Objects.equals(color, "WHITE")) {
                teamColor = ChessGame.TeamColor.WHITE;
                DrawBoard.printBoardWhite(board);
            }
            else {
                teamColor = ChessGame.TeamColor.BLACK;
                DrawBoard.printBoardBlack(board);
            }
            state = State.INGAME;
            return String.format("Joined game %s as %s player", params[0], params[1]);
        }
        else {
            return "Need <ID> [COLOR]";
        }
    }

    private String observe(String... params) throws Exception {
        assertLoggedIn();
        if (params.length == 1) {
            int id;
            try {
                id = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                return "Expected a number for <ID>";
            }
            var list = server.listGames(authToken);
            if (id > list.size() || id < 1) {
                return "Game does not exist";
            }
            state = State.INGAME;
            DrawBoard.printBoardWhite(board);
            return String.format("Observing game %s", params[0]);
        }
        else {
            return "Expected <ID>";
        }
    }

    private String logout() throws Exception {
        assertLoggedIn();
        server.logout(authToken);
        state = State.LOGGEDOUT;
        return "Logged out";
    }

    private String redraw() throws Exception {
        assertInGame();
        if (teamColor == ChessGame.TeamColor.WHITE) {
            DrawBoard.printBoardWhite(board);
        }
        else if (teamColor == ChessGame.TeamColor.BLACK){
            DrawBoard.printBoardBlack(board);
        }
        else {
            throw new Exception("How'd you get here");
        }
        return "";
    }

    private String move(ChessPiece.PieceType promotion, String... params) throws Exception {
        if (gameObject.getTeamTurn() != teamColor) {
            throw new Exception("It is not your turn");
        }
        var startSquare = params[0];
        var endSquare = params[1];
        var start = getPosFromInput(startSquare);
        var end = getPosFromInput(endSquare);
        var piece = board.getPiece(start);
        if (piece.getTeamColor() != teamColor) {
            throw new Exception("Not your piece");
        }
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && !promoting) {
            if (teamColor == ChessGame.TeamColor.WHITE) {
                if (end.getRow() == 8) {
                    promoting = true;
                    return "Pick a promotion piece (Q/R/B/N)";
                }
            }
        }
        var move = new ChessMove(start, end, promotion);
        promoting = false;
        gameObject.makeMove(move);
        return String.format("Move made from %s to %s", params[0], params[1]);
    }

    private String highlight(String... params) throws Exception {
        var place = params[0];
        var position = getPosFromInput(place);
        var moves = gameObject.validMoves(position);
        if (moves == null) {
            throw new Exception("No piece here");
        }
        if (moves.isEmpty()) {
            throw new Exception("No valid moves");
        }
        if (teamColor == ChessGame.TeamColor.WHITE) {
            DrawBoard.printHighlightsWhite(board, moves);
        }
        else {
            DrawBoard.printHighlightsBlack(board, moves);
        }
        return "";
    }

    private String resign() {
        if (!confirmed) {
            waiting = true;
            return "Are you sure you want to resign (y/n)";
        }
        else {
            confirmed = false;
            return "You have resigned, game over";
        }
    }

    private String leave() throws Exception {
        assertInGame();
        state = State.LOGGEDIN;
        return "You left the game";
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                   \u001b[35mregister \u001B[36m<USERNAME> <PASSWORD> <EMAIL>\u001B[0m - to create an account
                   \u001b[35mlogin \u001B[36m<USERNAME> <PASSWORD>\u001B[0m - to play chess
                   \u001b[35mquit\u001B[0m - leaves program
                   \u001b[35mhelp\u001B[0m - lists commands
                   """;
        }
        else if (state == State.LOGGEDIN) {
            return """
                   \u001b[35mcreate \u001B[36m<NAME>\u001B[0m - creates a game
                   \u001b[35mlist\u001B[0m - lists all games
                   \u001b[35mjoin \u001B[36m<ID> \u001B[0m[WHITE|\u001B[37mBLACK\u001B[0m] - joins a game as a color
                   \u001b[35mobserve \u001B[36m<ID>\u001B[0m - join game as an observer
                   \u001b[35mlogout\u001B[0m - logout of server
                   \u001b[35mquit\u001B[0m - leaves program
                   \u001b[35mhelp\u001B[0m - lists commands
                   """;
        }
        else {
            return """
                   \u001b[35mredraw\u001b[0m - redraws the chess board
                   \u001b[35mmove \u001B[36m<SQUARE>\u001b[0m - move the indicated piece
                   \u001b[35mhighlight \u001B[36m<START> <END>\u001b[0m - show valid moves for indicated piece
                   \u001b[35mresign\u001b[0m - quit the game
                   \u001b[35mleave\u001b[0m - leave the current game
                   \u001b[35mquit\u001B[0m - leaves program
                   \u001b[35mhelp\u001B[0m - lists commands
                   """;
        }
    }

    private void assertLoggedIn() throws Exception {
        if (state == State.LOGGEDOUT) {
            throw new Exception("Must login to perform command");
        }
        else if (state == State.INGAME) {
            throw new Exception("Unavailable while in a game");
        }
    }

    private void assertInGame() throws Exception {
        if (state != State.INGAME) {
            throw new Exception("Must be in a game");
        }
    }

    private int getColFromLetter(String letter) {
        return (Objects.equals("A", letter)) ? 1 :
               (Objects.equals("B", letter)) ? 2 :
               (Objects.equals("C", letter)) ? 3 :
               (Objects.equals("D", letter)) ? 4 :
               (Objects.equals("E", letter)) ? 5 :
               (Objects.equals("F", letter)) ? 6 :
               (Objects.equals("G", letter)) ? 7 :
               8;
    }

    private ChessPosition getPosFromInput(String input) throws Exception {
        if (input.length() != 2) {
            throw new Exception("Invalid input");
        }
        String[] tokens = input.split("");
        if (!Character.isLetter(tokens[0].charAt(0))) {
            throw new Exception("Invalid input");
        }
        int col = getColFromLetter(tokens[0].toUpperCase());
        int row = Integer.parseInt(tokens[1]);
        return new ChessPosition(row, col);
    }
}
