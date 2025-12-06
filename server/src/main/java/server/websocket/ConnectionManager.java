package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Session, ConnectedClient> connections = new ConcurrentHashMap<>();

    public void add(ConnectedClient client) {
        connections.put(client.session(), client);
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (ConnectedClient c : connections.values()) {
            Session session = c.session();
            if (c.gameID() == gameID && !session.equals(excludeSession) && session.isOpen()) {
                c.session().getRemote().sendString(msg);
                try {
                    session.getRemote().sendString(msg);
                } catch (IOException e) {
                    System.err.println("Failed to send message to " + c.username() + ": " + e.getMessage());
                }
            }
        }
    }

    public void sendToClient(Session session, ServerMessage message) {
        if (session != null && session.isOpen()) {
            try {
                session.getRemote().sendString(new Gson().toJson(message));
            } catch (IOException e) {
                System.err.println("Failed to send message to client: " + e.getMessage());
            }
        }
    }
}
