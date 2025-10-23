package service;

import dataaccess.DataAccess;
import dataaccess.MemoryDataAccess;
import datamodel.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {
    @Test
    void clear() throws Exception {
        var test1 = new UserData("joe", "j@j", "j");
        var test2 = new UserData("test", "test", "test");
        DataAccess da = new MemoryDataAccess();
        var adminService = new AdminService(da);
        var userService = new UserService(da);
        var gameService = new GameService(da);
        var test = userService.register(test1);
        userService.register(test2);
        int gameID = gameService.createGame(test.authToken(), "test");
        assertNotNull(da.getUser(test1.username()));
        assertNotNull(da.getAuth(test.authToken()));
        assertNotNull(da.getGame(gameID));
        adminService.clear();
        assertNull(da.getUser(test1.username()));
        assertNull(da.getAuth(test.authToken()));
        assertNull(da.getGame(gameID));
    }
}