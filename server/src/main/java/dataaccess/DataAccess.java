package dataaccess;

import chess.ChessGame;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.GameList;
import datamodel.UserData;

import java.util.ArrayList;

public interface DataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    int createGame(String name) throws DataAccessException;
    GameData getGame(Integer gameID);
    GameList getGameList() throws DataAccessException;
    String getPlayer(ChessGame.TeamColor color, GameData game);
    void joinGame(ChessGame.TeamColor color, String username, Integer gameID);
}
