package ui;

import client.ServerFacade;
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
        System.out.println("Welcome to Chess. Type help to get started");
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
                case "quit" -> "quit";
                default -> help();
            };
        } catch(Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (state == State.LOGGEDIN) {
            return "You are already logged in\n";
        }
        if (params.length >= 3) {
            state = State.LOGGEDIN;
            var user = new UserData(params[0], params[1], params[2]);
            var auth = server.register(user);
            authToken = auth.authToken();
            return String.format("You registered as %s\n", auth.username());
        }
        else {
            return "Missing inputs\n";
        }
    }

    public String login(String... params) throws Exception {
        if (params.length >= 2) {
            state = State.LOGGEDIN;
            var user = new UserData(params[0], params[1], null);
            var auth = server.login(user);
            authToken = auth.authToken();
            return String.format("logged in as %s\n", auth.username());
        }
        else {
            return "Expecting <USERNAME> <PASSWORD>";
        }
    }

    private String create(String... params) throws Exception {
        assertLoggedIn();
        return String.format("game created with name %s\n", params[0]);
    }

    private String list() throws Exception {
        assertLoggedIn();
        return "All of the games:\ngame1\ngame2\n";
    }

    private String join(String... params) throws Exception {
        assertLoggedIn();
        state = State.INGAME;
        if (Objects.equals(params[1], "WHITE")) {
            DrawBoard.printBoardWhite();
        }
        else if (Objects.equals(params[1], "BLACK")){
            DrawBoard.printBoardBlack();
        }
        return String.format("Joined game %s\n", params[0]);
    }

    private String observe(String... params) throws Exception {
        assertLoggedIn();
        DrawBoard.printBoardWhite();
        return String.format("Observing game %s\n", params[0]);
    }

    private String logout() throws Exception {
        assertLoggedIn();
        state = State.LOGGEDOUT;
        return "Logged out\n";
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
        else {
            return """
                   \u001b[35mcreate \u001B[36m<NAME>\u001B[0m - creates a game
                   \u001b[35mlist\u001B[0m - lists all games
                   \u001b[35mjoin \u001B[36m<ID> \u001B[0m[WHITE|\u001B[37mBLACK\u001B[0m] - joins a game as a color
                   \u001b[35mobserve \u001B[36m<ID>\u001B[0m - join game as observer
                   \u001b[35mlogout\u001B[0m - leave chess
                   \u001b[35mquit\u001B[0m - leaves program
                   \u001b[35mhelp\u001B[0m - lists commands
                   """;
        }
    }

    private void assertLoggedIn() throws Exception {
        if (state == State.LOGGEDOUT) {
            throw new Exception("Must login to perform command\n");
        }
        else if (state == State.INGAME) {
            throw new Exception("Unavailable while in a game\n");
        }
    }
}
