package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
        dataAccess.clear();
    }

    public AuthData register(UserData user) throws Exception {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Error: bad request");
        }
        var userCheck = user.username();
        if (dataAccess.getUser(userCheck) != null) {
            throw new AlreadyTakenException("Error: username already taken");
        }
        dataAccess.createUser(user);
        var authData = new AuthData(user.username(), generateAuthToken());
        dataAccess.createAuth(authData);
        return authData;
    }

    public AuthData login(UserData user) throws Exception {
        var userCheck = user.username();
        if (user.username() == null|| user.password() == null) {
            throw new BadRequestException("Error: bad request");
        }
        var storedUser = dataAccess.getUser(userCheck);
        if (storedUser == null) {
            throw new UnauthorizedException("Error: user does not exist");
        }
        if (!Objects.equals(storedUser.password(), user.password())) {
            throw new UnauthorizedException("Error: incorrect password");
        }
        dataAccess.createUser(user);
        var authData = new AuthData(user.username(), generateAuthToken());
        dataAccess.createAuth(authData);
        return authData;
    }

    public void logout(String authToken) throws UnauthorizedException {
        var authData = dataAccess.getAuth(authToken);
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        dataAccess.deleteAuth(authToken);
    }

    public ArrayList<GameData> listGames(String authToken) throws UnauthorizedException {
        if (dataAccess.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return dataAccess.getGameList();
    }

    public int createGame(String authToken, String name) throws UnauthorizedException, BadRequestException {
        if (name == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (dataAccess.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        var gameID = dataAccess.createGame(name);
        return gameID;
    }

    public void joinGame(String authToken, String color, Integer gameID) throws UnauthorizedException, AlreadyTakenException, BadRequestException {
        if (!Objects.equals(color, "BLACK") && !Objects.equals(color, "WHITE")) {
            throw new BadRequestException("Error: bad request");
        }
        var auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        var game = dataAccess.getGame(gameID);
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
        dataAccess.joinGame(playerColor, auth.username(), gameID);
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
