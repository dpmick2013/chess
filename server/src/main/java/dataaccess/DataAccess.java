package dataaccess;

import chess.ChessGame;
import datamodel.*;

public interface DataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    int createGame(String name) throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    void updateGame(int gameID, ChessGame game) throws DataAccessException;
    GameList getGameList() throws DataAccessException;
    String getPlayer(ChessGame.TeamColor color, GameData game) throws DataAccessException;
    void joinGame(ChessGame.TeamColor color, String username, Integer gameID) throws DataAccessException;
    void leaveGame(ChessGame.TeamColor color, Integer gameID) throws DataAccessException;
}
