package dataaccess;

import datamodel.AuthData;
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
        existingUser = new UserData("test", "test", "test");
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
    void createGame() throws DataAccessException {
        int id = da.createGame("test");
        assertEquals(1, id);
    }
}
