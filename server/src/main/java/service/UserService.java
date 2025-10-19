package service;

import dataaccess.DataAccess;
import datamodel.AuthData;
import datamodel.UserData;

import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData user) throws Exception {
        var userCheck = user.username();
        if (dataAccess.getUser(userCheck) != null) {
            throw new AlreadyTakenException();
        }
        dataAccess.createUser(user);
        var authData = new AuthData(user.username(), generateAuthToken());
        dataAccess.createAuth(authData);
        return authData;
    }

    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
