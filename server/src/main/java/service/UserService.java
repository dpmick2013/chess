package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
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
        var authData = new AuthData(generateAuthToken(), user.username());
        dataAccess.createAuth(authData);
        return authData;
    }

    public AuthData login(UserData user) throws UnauthorizedException {
        var userCheck = user.username();
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

    public Integer createGame(String authToken, String name) throws UnauthorizedException {
        Integer gameID = 1;
        if (dataAccess.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        var newGame = new GameData(gameID, null, null, name);
        dataAccess.createGame(newGame);
        return newGame.gameID();
    }

    public void joinGame(ChessGame.TeamColor color, Integer gameID) {

    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
