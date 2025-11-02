package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import datamodel.AuthData;
import datamodel.UserData;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import org.mindrot.jbcrypt.BCrypt;

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
//        var userCheck = user.username();
//        if (dataAccess.getUser(userCheck) != null) {
//            throw new AlreadyTakenException("Error: username already taken");
//        }
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        var storedUser = new UserData(user.username(), hashedPassword, user.email());
        try {
            dataAccess.createUser(storedUser);
        } catch (DataAccessException ex) {
            throw new AlreadyTakenException("Error: username already taken");
        }
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
        if (!BCrypt.checkpw(user.password(), storedUser.password())) {
            throw new UnauthorizedException("Error: incorrect password");
        }
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
//    boolean verifyUser(String username, String providedClearTextPassword) {
//        // read the previously hashed password from the database
//        var hashedPassword = readHashedPasswordFromDatabase(username);
//
//        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
//    }
}
