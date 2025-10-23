package service;

import dataaccess.MemoryDataAccess;
import datamodel.AuthData;
import datamodel.UserData;
import exception.AlreadyTakenException;
import exception.UnauthorizedException;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @BeforeEach


    @Test
    void register() throws Exception {
        var user = new UserData("joe", "j@j", "j");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        AuthData res = service.register(user);
        assertNotNull(res);
        assertEquals(res.username(), user.username());
        assertNotNull(res.authToken());
    }

    @Test
    void registerUserExists() throws Exception {
        var existingUser = new UserData("existing", "password", "email@email");
        var testUser = new UserData("existing", "test", "test@email");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        service.register(existingUser);
        assertThrows(AlreadyTakenException.class, () -> service.register(testUser));
    }

    @Test
    void login() throws Exception {
        var user = new UserData("joe", "j", "j@j");
        var loginUser = new UserData("joe", "j", null);
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        AuthData reg = service.register(user);
        AuthData log = service.login(loginUser);
        assertNotNull(log);
        assertEquals(reg.username(), log.username());
    }

    @Test
    void loginIncorrectPassword() throws Exception {
        var user = new UserData("joe", "j@j", "j");
        var loginUser = new UserData("joe", "incorrect", null);
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        service.register(user);
        assertThrows(UnauthorizedException.class, () -> service.login(loginUser));
    }

    @Test
    void logout() throws Exception {
        var existingUser = new UserData("existing", "password", "email@email");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        var res = service.register(existingUser);
        var authToken = res.authToken();
        assertDoesNotThrow(() -> service.logout(authToken));
    }

    @Test
    void logoutAgain() throws Exception {
        var existingUser = new UserData("existing", "password", "email@email");
        var da = new MemoryDataAccess();
        var service = new UserService(da);
        var res = service.register(existingUser);
        var authToken = res.authToken();
        assertDoesNotThrow(() -> service.logout(authToken));
        assertThrows(UnauthorizedException.class, () -> service.logout(authToken));
    }
}