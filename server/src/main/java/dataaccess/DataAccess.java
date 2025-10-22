package dataaccess;

import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;

import java.util.ArrayList;

public interface DataAccess {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
    void createAuth(AuthData auth);
    AuthData getAuth(String username);
    void deleteAuth(String authToken);
    void createGame(GameData game);
    GameData getGame(Integer gameID);
    ArrayList<GameData> getGameList();
}
