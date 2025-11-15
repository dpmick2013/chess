package ui;

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
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
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
            if (Objects.equals(color, "WHITE")) {
                DrawBoard.printBoardWhite();
            }
            else {
                DrawBoard.printBoardBlack();
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
            DrawBoard.printBoardWhite();
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

    private String leave() {
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
}
