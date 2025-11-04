package dataaccess;

import chess.ChessGame;
import datamodel.AuthData;
import datamodel.GameData;
import datamodel.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {

    private static DataAccess da;
    private static UserData existingUser;
    private static AuthData existingAuth;

    @BeforeAll
    static void init() {
        da = new MySqlDataAccess();
        existingUser = new UserData("test", "test", "test@test");
        existingAuth = new AuthData("test", "test");
    }

    @BeforeEach
    void setup() throws Exception {
        da.clear();
    }

    @Test
    void clear() throws Exception {
        da.createUser(existingUser);
        da.clear();
        var user = da.getUser(existingUser.username());
        assertNull(user);
    }

    @Test
    void createUser() throws Exception {
        assertDoesNotThrow(() -> da.createUser(existingUser));
    }

    @Test
    void createAuth() throws Exception {
        assertDoesNotThrow(() -> da.createAuth(existingAuth));
    }

    @Test
    void getUser() throws Exception {
        da.createUser(existingUser);
        var result = da.getUser(existingUser.username());
        assertNotNull(result);
        assertEquals(existingUser, result);
    }

    @Test
    void getAuth() throws Exception {
        da.createAuth(existingAuth);
        var result = da.getAuth(existingAuth.authToken());
        assertNotNull(result);
        assertEquals(existingAuth, result);
    }

    @Test
    void deleteAuth() throws Exception {
        da.createAuth(existingAuth);
        assertNotNull(da.getAuth(existingAuth.authToken()));
        da.deleteAuth(existingAuth.authToken());
        assertNull(da.getAuth(existingAuth.authToken()));
    }

    @Test
    void createGame() throws Exception {
        int id = da.createGame("test");
        assertEquals(1, id);
    }

    @Test
    void getGameList() throws Exception {
        da.createGame("test1");
        da.createGame("test2");
        var list = da.getGameList();
        assertNotNull(list);
    }

    @Test
    void getGame() throws Exception {
        var gameID = da.createGame("test1");
        var game = da.getGame(gameID);
        assertNotNull(game);
        assertInstanceOf(GameData.class, game);
        assertEquals(game.gameID(), gameID);
    }

    @Test
    void joinGame() throws Exception {
        var gameID = da.createGame("test1");
        da.joinGame(ChessGame.TeamColor.WHITE, "test", gameID);
        var game = da.getGame(gameID);
        assertNotNull(game.whiteUsername());
        assertEquals("test", game.whiteUsername());
    }
}
