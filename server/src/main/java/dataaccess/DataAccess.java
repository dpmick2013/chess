package dataaccess;

import chess.ChessGame;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;

import java.util.ArrayList;

public interface DataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username);
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
    int createGame(String name);
    GameData getGame(Integer gameID);
    ArrayList<GameData> getGameList();
    String getPlayer(ChessGame.TeamColor color, GameData game);
    void joinGame(ChessGame.TeamColor color, String username, Integer gameID);
}
