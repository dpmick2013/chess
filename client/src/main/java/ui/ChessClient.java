package ui;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

    private State state = State.LOGGEDOUT;

    public ChessClient() {

    }

    public enum State {
        LOGGEDOUT,
        LOGGEDIN
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
        if (state == State.LOGGEDOUT) {
            System.out.print("[LOGGED_OUT]" + " >>> ");
        }
        else {
            System.out.print("[LOGGED_IN]" + " >>> ");
        }
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch(Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) {
        if (params.length >= 3) {
            state = State.LOGGEDIN;
            return String.format("registered as %s\n", params[0]);
        }
        else {
            return "Missing inputs\n";
        }
    }

    public String login(String... params) {
        state = State.LOGGEDIN;
        return String.format("logged in as %s\n", params[0]);
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
                   register <USERNAME> <PASSWORD> <EMAIL>
                   login <USERNAME> <PASSWORD>
                   quit
                   help
                   """;
        }
        else {
            return """
                   create <NAME>
                   list
                   join <ID> [WHITE|BLACK]
                   observe <ID>
                   logout
                   quit
                   help
                   """;
        }
    }
}
