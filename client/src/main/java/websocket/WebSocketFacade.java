package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ServerException;
import jakarta.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler messageHandler;

    public WebSocketFacade(String url, ServerMessageHandler messageHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messageHandler = messageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    switch (notification.getServerMessageType()) {
                        case LOAD_GAME -> notification = new Gson().fromJson(message, LoadGameMessage.class);
                        case NOTIFICATION -> notification = new Gson().fromJson(message, NotificationMessage.class);
                        case ERROR -> notification = new Gson().fromJson(message, ErrorMessage.class);
                    }
                    messageHandler.handle(notification);
                }
            });
        } catch (URISyntaxException ex) {
            throw new ServerException("Error", 500);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("opened");
    }

    @Override
    public void onError(Session session, Throwable thr) {
        System.out.println(thr.getMessage());
    }


    public void connect(String token, int gameID) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, token, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void move(String token, int gameID, ChessMove move) throws Exception {
        try {
            var command = new MakeMoveCommand(token, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void leave(String token, int gameID) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, token, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void resign(String token, int gameID) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, token, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
