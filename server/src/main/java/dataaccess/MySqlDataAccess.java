package dataaccess;

import chess.ChessGame;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlDataAccess implements DataAccess{

    public MySqlDataAccess() {
        try {
            configureDatabase();
        } catch (Exception ex) {
            System.out.println("Error");
        }
    }



    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """
//            """
//            CREATE TABLE IF NOT EXISTS  auth (
//              `authToken` varchar(256) NOT NULL,
//              `username` varchar(256) NOT NULL,
//              PRIMARY KEY (`authToken`)
//            )
//            """,
//            """
//            CREATE TABLE IF NOT EXISTS  user (
//              `gameID` int NOT NULL AUTO_INCREMENT,
//              `whiteUsername` varchar(256),
//              `blackUsername` varchar(256),
//              `gameName' varchar(256) NOT NULL,
//              'game' longtext,
//              PRIMARY KEY (`gameID`)
//            )
//            """
    };


    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure database");
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData user) {
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        try {
            executeUpdate(statement, user.username(), user.password(), user.email());
        } catch(DataAccessException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createAuth(AuthData auth) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public int createGame(String name) {
        return 0;
    }

    @Override
    public GameData getGame(Integer gameID) {
        return null;
    }

    @Override
    public ArrayList<GameData> getGameList() {
        return null;
    }

    @Override
    public String getPlayer(ChessGame.TeamColor color, GameData game) {
        return null;
    }

    @Override
    public void joinGame(ChessGame.TeamColor color, String username, Integer gameID) {

    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
    }
}
