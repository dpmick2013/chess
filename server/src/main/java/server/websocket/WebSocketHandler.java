package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.BadRequestException;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler,  WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserService userService;
    private final GameService gameService;

    public WebSocketHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
                command = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
            }
            switch(command.getCommandType()) {
                case CONNECT -> handleConnectCommand(ctx, command);
                case MAKE_MOVE -> handleMakeMoveCommand(ctx, (MakeMoveCommand) command);
//                case LEAVE -> handleLeaveCommand(ctx, command);
//                case RESIGN -> handleResignCommand(ctx, command);
            }
        } catch(Exception ex) {
            sendError(ctx.session, "Server error: " + ex.getMessage());
        }

    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void handleConnectCommand(WsMessageContext ctx, UserGameCommand command) throws Exception {
        try {
            validateCommand(command);
        } catch(Exception ex) {
            sendError(ctx.session, ex.getMessage());
            return;
        }
        var token = command.getAuthToken();
        var username = userService.getUsernameFromAuth(token);
        var gameID = command.getGameID();
        connections.add(new ConnectedClient(ctx.session, username, gameID));
        ServerMessage loadGame = new LoadGameMessage(gameService.getGame(gameID).game());
        connections.sendToClient(ctx.session, loadGame);
        var color = gameService.getPlayerColor(username, gameID);
        String notifyText = username + " joined as " + color;
        if (color == null) {
            notifyText = username + " joined as an observer";
        }
        connections.broadcast(command.getGameID(), ctx.session, new NotificationMessage(notifyText));
    }

    private void handleMakeMoveCommand(WsMessageContext ctx, MakeMoveCommand command) throws Exception {
        var gameID = command.getGameID();
        var game = gameService.getGame(gameID);
        if (!validateMove(command, game.game())) {
            sendError(ctx.session, "Error: illegal move");
            return;
        }
        gameService.updateGame(gameID, game);
        var username = userService.getUsernameFromAuth(command.getAuthToken());
        ServerMessage loadGameMsg = new LoadGameMessage(game.game());
        connections.broadcast(gameID, null, loadGameMsg);
        String notifyText = username + " moved " + command.move.toString();
        connections.broadcast(gameID, ctx.session, new NotificationMessage(notifyText));
        var status = game.game().getGameStatus();
        if (status != ChessGame.GameStatus.PLAYING) {
            notifyText = "Game has ended in" + status.toString();
            connections.broadcast(gameID, null, new NotificationMessage(notifyText));
        }
    }

//    private void handleLeaveCommand(WsMessageContext ctx, UserGameCommand command) throws Exception {
//        connections.remove(ctx.session);
//        String notifyText = command + " left the game";
//        connections.broadcast(1, null, new NotificationMessage(notifyText));
//    }

//    private void handleResignCommand(WsMessageContext ctx, UserGameCommand command) throws Exception {
//        markGameOver();
//        String notifyText = command.username + " resigned";
//        connections.broadcast(1, null, new NotificationMessage(notifyText));
//    }

    private void sendError(Session session, String errorMsg) {
        ServerMessage error = new ErrorMessage(errorMsg);
        connections.sendToClient(session, error);
    }

    private void validateCommand(UserGameCommand command) throws Exception {
        userService.getUsernameFromAuth(command.getAuthToken());
        var gameData = gameService.getGame(command.getGameID());
        if (gameData == null) {
            throw new BadRequestException("Error: game does not exist");
        }
    }

    private boolean validateMove(MakeMoveCommand command, ChessGame game) throws Exception {
        try {
            validateCommand(command);
        } catch(Exception ex) {
            return false;
        }
        var username = userService.getUsernameFromAuth(command.getAuthToken());
        var color = gameService.getPlayerColor(username, command.getGameID());
        var turn = game.getTeamTurn();
        var turnString = (turn == ChessGame.TeamColor.WHITE) ? "white" : "black";
        if (!color.equals(turnString)) {
            throw new Exception("Error: not your turn");
        }
        try {
            game.makeMove(command.move);
        } catch(InvalidMoveException ex) {
            return false;
        }
        return true;
    }
}
