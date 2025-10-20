package service;

import dataaccess.DataAccess;
import datamodel.AuthData;
import datamodel.UserData;
import exception.AlreadyTakenException;
import exception.UnauthorizedException;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws AlreadyTakenException {
        var userCheck = user.username();
        if (dataAccess.getUser(userCheck) != null) {
            throw new AlreadyTakenException("Error: username already taken");
        }
        dataAccess.createUser(user);
        var authData = new AuthData(user.username(), generateAuthToken());
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

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
