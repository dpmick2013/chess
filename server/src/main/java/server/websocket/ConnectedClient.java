package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

public record ConnectedClient(Session session, String username, int gameID) {
}
