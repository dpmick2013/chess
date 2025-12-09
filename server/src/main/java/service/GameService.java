package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.GameResult;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.ServerException;
import exception.UnauthorizedException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    private final DataAccess dataAccess;

    public GameService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public ArrayList<GameResult> listGames(String authToken) throws Exception {
        checkAuth(authToken);
        ArrayList<GameResult> list = new ArrayList<>();
        try {
            list = dataAccess.getGameList();
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        return list;
    }

    public int createGame(String authToken, String name) throws Exception {
        if (name == null) {
            throw new BadRequestException("Error: bad request");
        }
        checkAuth(authToken);
        int id = 0;
        try {
            id = dataAccess.createGame(name);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        return id;
    }

    public void joinGame(String authToken, String color, Integer gameID) throws Exception {
        if (!Objects.equals(color, "BLACK") && !Objects.equals(color, "WHITE")) {
            throw new BadRequestException("Error: bad request");
        }
        var auth = checkAuth(authToken);
        GameData game = getGame(gameID);
        if (game == null) {
            throw new BadRequestException("Error: game does not exist");
        }
        ChessGame.TeamColor playerColor = null;
        if (Objects.equals(color, "WHITE")) {
            playerColor = ChessGame.TeamColor.WHITE;
        }
        else if (Objects.equals(color, "BLACK")){
            playerColor = ChessGame.TeamColor.BLACK;
        }
        if (dataAccess.getPlayer(playerColor, game) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }
        try {
            dataAccess.joinGame(playerColor, auth.username(), gameID);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
    }

    public void leaveGame(String color, Integer gameID) throws Exception {
        try {
            if (color.equalsIgnoreCase("white")) {
                dataAccess.leaveGame(ChessGame.TeamColor.WHITE, gameID);
            }
            else {
                dataAccess.leaveGame(ChessGame.TeamColor.BLACK, gameID);
            }
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
    }

    public GameData getGame(int gameID) throws Exception {
        GameData game = null;
        try {
            game = dataAccess.getGame(gameID);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        return game;
    }

    public void updateGame(int gameID, GameData game) throws Exception {
        try {
            dataAccess.updateGame(gameID, game.game());
        } catch(DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
    }

    public String getPlayerColor(String username, int gameID) throws Exception {
        var game = getGame(gameID);
        String whiteUser = "";
        String blackUser = "";
        try {
            whiteUser = dataAccess.getPlayer(ChessGame.TeamColor.WHITE, game);
            blackUser = dataAccess.getPlayer(ChessGame.TeamColor.BLACK, game);
        } catch(DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        if (username.equals(whiteUser)) {
            return "white";
        }
        else if (username.equals(blackUser)) {
            return "black";
        }
        return null;
    }

    private AuthData checkAuth(String token) throws Exception {
        AuthData authData = null;
        try {
            authData = dataAccess.getAuth(token);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return authData;
    }

    private void sqlExceptionHandler(Exception ex) throws ServerException {
        if (ex.getCause() instanceof SQLException) {
            throw new ServerException("Error: Database connection failed", 500);
        }
    }
}


