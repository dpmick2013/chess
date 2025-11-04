package service;

import dataaccess.DataAccess;
import dataaccess.MySqlDataAccess;
import datamodel.GameResult;
import datamodel.UserData;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private static DataAccess da;
    private static UserData existingUser;
    private static UserService userService;
    private static GameService gameService;

    @BeforeAll
    static void init() {
        da = new MySqlDataAccess();
        userService = new UserService(da);
        gameService = new GameService(da);
        existingUser = new UserData("existing", "password", "existing@email");
    }

    @BeforeEach
    void setup() throws Exception {
        da.clear();
    }
    @Test
    void listGames() throws Exception {
        var res = userService.register(existingUser);
        var authToken = res.authToken();
        da.createGame("test");
        ArrayList<GameResult> gameList = gameService.listGames(authToken);
        assertNotNull(gameList);
    }

    @Test
    void listGamesUnauthorized() throws Exception {
        userService.register(existingUser);
        var authToken = "bad";
        assertThrows(UnauthorizedException.class, () -> gameService.listGames(authToken));
    }

    @Test
    void createGame() throws Exception {
        var res = userService.register(existingUser);
        var authToken = res.authToken();
        assertDoesNotThrow(() -> gameService.createGame(authToken, "test1"));
        var gameID = gameService.createGame(authToken, "test2");
        var game = da.getGame(gameID);
        assertNull(game.whiteUsername());
        assertNull(game.blackUsername());
        assertEquals("test2", game.gameName());
    }

    @Test
    void createGameInvalid() throws Exception {
        var res = userService.register(existingUser);
        var badAuthToken = "bad";
        var goodAuthToken = res.authToken();
        assertThrows(UnauthorizedException.class, () -> gameService.createGame(badAuthToken, "test"));
        assertThrows(BadRequestException.class, () -> gameService.createGame(goodAuthToken, null));
    }

    @Test
    void joinGame() throws Exception {
        var res = userService.register(existingUser);
        var authToken = res.authToken();
        var gameID = gameService.createGame(authToken, "test");
        var game = da.getGame(gameID);
        gameService.joinGame(authToken, "WHITE", gameID);
        var updatedGame = da.getGame(gameID);
        assertNotEquals(updatedGame.whiteUsername(), game.whiteUsername());
        assertEquals(updatedGame.gameID(), game.gameID());
        assertEquals(updatedGame.blackUsername(), game.blackUsername());
        assertEquals(updatedGame.gameName(), game.gameName());
        assertEquals(updatedGame.whiteUsername(), existingUser.username());
    }

    @Test
    void joinGameInvalid() throws Exception {
        var testUser = new UserData("test", "test", "test@test");
        var res = userService.register(existingUser);
        var testReg = userService.register(testUser);
        var authToken = res.authToken();
        var testAuth = testReg.authToken();
        var gameID = gameService.createGame(authToken, "test");
        gameService.joinGame(authToken, "WHITE", gameID);
        assertThrows(BadRequestException.class, () -> gameService.joinGame(authToken, "WHITE", 10));
        assertThrows(BadRequestException.class, () -> gameService.joinGame(authToken, "bad", gameID));
        assertThrows(UnauthorizedException.class, () -> gameService.joinGame("bad", "BLACK", gameID));
        assertThrows(AlreadyTakenException.class, () -> gameService.joinGame(testAuth, "WHITE", gameID));
    }
}