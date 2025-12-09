package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import datamodel.*;

import java.sql.*;
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
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS  auths (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `game` longtext NOT NULL,
              PRIMARY KEY (`gameID`)
            )
            """
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
    public void clear() throws DataAccessException {
        var statement1 = "TRUNCATE TABLE users";
        var statement2 = "TRUNCATE TABLE auths";
        var statement3 = "TRUNCATE TABLE games";
        executeUpdate(statement1);
        executeUpdate(statement2);
        executeUpdate(statement3);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, user.username(), user.password(), user.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String uname = rs.getString(1);
                        String password = rs.getString(2);
                        String email = rs.getString(3);
                        return new UserData(uname, password, email);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
        return null;
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auths WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String token = rs.getString(1);
                        String uname = rs.getString(2);
                        return new AuthData(uname, token);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    @Override
    public int createGame(String name) throws DataAccessException {
        var statement = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        var game = new ChessGame();
        var gameJson = new Gson().toJson(game);
        return executeUpdate(statement, name, gameJson);
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var id = rs.getInt(1);
                        var wUser = rs.getString(2);
                        var bUser = rs.getString(3);
                        var name = rs.getString(4);
                        var gameString = rs.getString(5);
                        var game = new Gson().fromJson(gameString, ChessGame.class);
                        return new GameData(id, wUser, bUser, name, game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error");
        }
        return null;
    }

    @Override
    public void updateGame(int gameID, ChessGame game) throws DataAccessException {
        String statement = "UPDATE games SET game=? WHERE gameID=?";
        var gameJson = new Gson().toJson(game);
        executeUpdate(statement, gameJson, gameID);
    }

    @Override
    public GameList getGameList() throws DataAccessException {
        ArrayList<GameResult> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var id = rs.getInt(1);
                        var wUser = rs.getString(2);
                        var bUser = rs.getString(3);
                        var name = rs.getString(4);
                        list.add(new GameResult(id, wUser, bUser, name));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error");
        }
        return new GameList(list);
    }

    @Override
    public String getPlayer(ChessGame.TeamColor color, GameData game) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement;
            if (color == ChessGame.TeamColor.WHITE) {
                statement = "SELECT whiteUsername FROM games WHERE gameID=?";
            }
            else {
                statement = "SELECT blackUsername FROM games WHERE gameID=?";
            }
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, game.gameID());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString(1);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("Error");
        }
        return null;
    }

    @Override
    public void joinGame(ChessGame.TeamColor color, String username, Integer gameID) throws DataAccessException {
        String statement;
        if (color == ChessGame.TeamColor.WHITE) {
            statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
        }
        else {
            statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
        }
        executeUpdate(statement, username, gameID);
    }

    @Override
    public void leaveGame(ChessGame.TeamColor color, Integer gameID) throws DataAccessException {
        String statement;
        if (color == ChessGame.TeamColor.WHITE) {
            statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
        }
        else {
            statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
        }
        executeUpdate(statement, null, gameID);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    }
                    else if (param == null) {
                        ps.setNull(i + 1, Types.VARCHAR);
                    }
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
