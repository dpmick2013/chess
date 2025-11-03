package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import datamodel.AuthData;
import datamodel.UserData;
import exception.AlreadyTakenException;
import exception.UnauthorizedException;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private static DataAccess da;
    private static UserData existingUser;
    private static UserService service;

    @BeforeAll
    static void init() {
        da = new MySqlDataAccess();
        service = new UserService(da);
        existingUser = new UserData("test", "test", "test");
    }

    @BeforeEach
    void setup() throws Exception {
        da.clear();
    }

    @Test
    void register() throws Exception {
        var user = new UserData("joe", "j@j", "j");
        da.clear();
        AuthData res = service.register(existingUser);
        assertNotNull(res);
        assertEquals(res.username(), existingUser.username());
        assertNotNull(res.authToken());
    }

    @Test
    void registerUserExists() throws Exception {
        var testUser = new UserData("test", "password", "test@email");
        service.register(existingUser);
        assertThrows(AlreadyTakenException.class, () -> service.register(testUser));
    }

    @Test
    void login() throws Exception {
        var loginUser = new UserData("test", "test", null);
        AuthData reg = service.register(existingUser);
        AuthData log = service.login(loginUser);
        assertNotNull(log);
        assertEquals(reg.username(), log.username());
    }

    @Test
    void loginIncorrectPassword() throws Exception {
        var loginUser = new UserData("test", "incorrect", null);
        service.register(existingUser);
        assertThrows(UnauthorizedException.class, () -> service.login(loginUser));
    }

    @Test
    void logout() throws Exception {
        var res = service.register(existingUser);
        var authToken = res.authToken();
        assertDoesNotThrow(() -> service.logout(authToken));
    }

    @Test
    void logoutAgain() throws Exception {
        var res = service.register(existingUser);
        var authToken = res.authToken();
        assertDoesNotThrow(() -> service.logout(authToken));
        assertThrows(UnauthorizedException.class, () -> service.logout(authToken));
    }
}