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
    void createUser() {
        assertDoesNotThrow(() -> da.createUser(existingUser));
    }

    @Test
    void createUserAgain() throws Exception {
        var testUser = new UserData("test", "duplicate", "test@dupe");
        da.createUser(existingUser);
        assertThrows(DataAccessException.class, () -> da.createUser(testUser));
    }

    @Test
    void createAuth() {
        assertDoesNotThrow(() -> da.createAuth(existingAuth));
    }

    @Test
    void createAuthAgain() throws Exception {
        var testAuth = new AuthData("duplicate", "test");
        da.createAuth(existingAuth);
        assertThrows(DataAccessException.class, () -> da.createAuth(testAuth));
    }

    @Test
    void getUser() throws Exception {
        da.createUser(existingUser);
        var result = da.getUser(existingUser.username());
        assertNotNull(result);
        assertEquals(existingUser, result);
    }

    @Test
    void getUserNonexistent() throws Exception {
        da.createUser(existingUser);
        var result = da.getUser(existingUser.username());
        assertNotNull(result);
        var badResult = da.getUser("bad_name");
        assertNull(badResult);
    }

    @Test
    void getAuth() throws Exception {
        da.createAuth(existingAuth);
        var result = da.getAuth(existingAuth.authToken());
        assertNotNull(result);
        assertEquals(existingAuth, result);
    }

    @Test
    void getAuthNonexistent() throws Exception {
        da.createAuth(existingAuth);
        var result = da.getAuth(existingAuth.authToken());
        assertNotNull(result);
        var badResult = da.getAuth("bad_auth");
        assertNull(badResult);
    }

    @Test
    void deleteAuth() throws Exception {
        da.createAuth(existingAuth);
        assertNotNull(da.getAuth(existingAuth.authToken()));
        da.deleteAuth(existingAuth.authToken());
        assertNull(da.getAuth(existingAuth.authToken()));
    }

    @Test
    void deleteWrongAuth() throws Exception {
        da.createAuth(existingAuth);
        da.deleteAuth("bad");
        var result = da.getAuth(existingAuth.authToken());
        assertNotNull(result);
    }


    @Test
    void createGame() throws Exception {
        int id = da.createGame("test");
        assertEquals(1, id);
    }

    @Test
    void createGameNullName() {
        assertThrows(DataAccessException.class, () -> da.createGame(null));
    }

    @Test
    void getGameList() throws Exception {
        da.createGame("test1");
        da.createGame("test2");
        var list = da.getGameList();
        assertNotNull(list);
    }

    @Test
    void getEmptyList() throws DataAccessException {
        var list = da.getGameList();
        assertTrue(list.isEmpty());
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
    void getGameBadID() throws Exception {
        da.createGame("test1");
        var game = da.getGame(100);
        assertNull(game);
    }

    @Test
    void joinGame() throws Exception {
        var gameID = da.createGame("test1");
        da.joinGame(ChessGame.TeamColor.WHITE, "test", gameID);
        var game = da.getGame(gameID);
        assertNotNull(game.whiteUsername());
        assertEquals("test", game.whiteUsername());
    }

    @Test
    void joinGameBadID() throws Exception {
        var gameID = da.createGame("test1");
        da.joinGame(ChessGame.TeamColor.WHITE, "test", 100);
        var game = da.getGame(gameID);
        assertNull(game.whiteUsername());
    }
}
