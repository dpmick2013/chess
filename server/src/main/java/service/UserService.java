package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import datamodel.AuthData;
import datamodel.UserData;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.ServerException;
import exception.UnauthorizedException;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
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
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        var storedUser = new UserData(user.username(), hashedPassword, user.email());
        try {
            dataAccess.createUser(storedUser);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
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
        UserData storedUser = null;
        try {
            storedUser = dataAccess.getUser(userCheck);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        if (storedUser == null) {
            throw new UnauthorizedException("Error: user does not exist");
        }
        if (!BCrypt.checkpw(user.password(), storedUser.password())) {
            throw new UnauthorizedException("Error: incorrect password");
        }
        var authData = new AuthData(user.username(), generateAuthToken());
        try {
            dataAccess.createAuth(authData);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        return authData;
    }

    public void logout(String authToken) throws Exception {
        AuthData authData = null;
        try {
            authData = dataAccess.getAuth(authToken);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        if (authData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        try{
            dataAccess.deleteAuth(authToken);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
    }

    public String getUsernameFromAuth(String authToken) throws Exception {
        AuthData auth = null;
        try {
            auth = dataAccess.getAuth(authToken);
        } catch (DataAccessException ex) {
            sqlExceptionHandler(ex);
        }
        if (auth == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return auth.username();
    }

    private static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    private void sqlExceptionHandler(Exception ex) throws ServerException {
        if (ex.getCause() instanceof SQLException) {
            throw new ServerException("Error: Database connection failed", 500);
        }
    }
}
