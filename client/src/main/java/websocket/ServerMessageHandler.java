package websocket;

import chess.ChessGame;
import ui.ChessClient;
import ui.DrawBoard;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Objects;

public class ServerMessageHandler {
    private final ChessClient client;

    public ServerMessageHandler(ChessClient client) {
        this.client = client;
    }

    public void handle(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> {
                var load = (LoadGameMessage) message;
                client.setGame(load.game);
                var board = load.game.getBoard();
                client.setBoard(board);
                var color = client.getColor();
                if (color == ChessGame.TeamColor.WHITE) {
                    DrawBoard.printBoardWhite(board);
                }
                else {
                    DrawBoard.printBoardBlack(board);
                }
            }
            case NOTIFICATION -> {
                var note = (NotificationMessage) message;
//                showNotification(note.message);
            }
            case ERROR -> {
                var err = (ErrorMessage) message;
//                showError(err.errorMessage);
            }
        }
    }
}
