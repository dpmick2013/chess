package dataaccess;

import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;

public interface DataAccess {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
    void createAuth(AuthData auth);
    AuthData getAuth(String username);
    void storeGame(GameData game);
    GameData getGame(Integer gameID);
}
