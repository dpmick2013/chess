package dataaccess;

import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
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
        auths.put(auth.username(), auth);
    }

    @Override
    public AuthData getAuth(String username) {
        return auths.get(username);
    }

    @Override
    public void storeGame(GameData game) {
        games.put(game.gameID(), game);
    }

    @Override
    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }
}
