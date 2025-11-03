package dataaccess;

import chess.ChessGame;
import datamodel.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    private int gameID = 1;
    private final HashMap<String, UserData> users = new HashMap<>();
    private final HashMap<String, AuthData> auths = new HashMap<>();
    private final HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
        auths.clear();
        games.clear();
    }
    @Override
    public void createUser(UserData user) {
        users.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public void createAuth(AuthData auth) {
        auths.put(auth.authToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public int createGame(String name) {
        var game = new GameData(gameID++, null, null, name, new ChessGame());
        games.put(game.gameID(), game);
        return game.gameID();
    }

    @Override
    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    @Override
    public GameList getGameList() {
        ArrayList<GameResult> list = new ArrayList<>();
        for (GameData game : games.values()) {
            list.add(new GameResult(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return new GameList(list);
    }

    @Override
    public String getPlayer(ChessGame.TeamColor color, GameData game) {
        if (color == ChessGame.TeamColor.WHITE) {
            return game.whiteUsername();
        }
        else {
            return game.blackUsername();
        }
    }

    @Override
    public void joinGame(ChessGame.TeamColor color, String username, Integer gameID) {
        var game = games.get(gameID);
        if (color == ChessGame.TeamColor.WHITE) {
            games.replace(gameID, new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game()));
        }
        else {
            games.replace(gameID, new GameData(gameID, game.whiteUsername(), username, game.gameName(), game.game()));
        }
    }
}
