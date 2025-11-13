package client;

import datamodel.UserData;
import exception.ServerException;
import org.junit.jupiter.api.*;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String serverURL;
    private static UserData user;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverURL = "http://localhost:" + port;
        System.out.println("Started test HTTP server on " + port);
        user = new UserData("test", "test", "test@gmail.com");
    }

    @BeforeEach
    public void setup() throws Exception {
        facade = new ServerFacade(serverURL);
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerSuccess() throws Exception {
        var result = facade.register(user);
        assertNotNull(result);
        assertNotNull(result.authToken());
        assertEquals(user.username(), result.username());
    }

    @Test
    public void registerTwice() throws Exception {
        facade.register(user);
        var testUser = new UserData("test", "duplicate", "email@gmail.com");
        ServerException ex = assertThrows(ServerException.class, () -> facade.register(testUser));
        assertEquals(ex.getCode(), 403);
    }

    @Test
    public void loginSuccess() throws Exception {
        facade.register(user);
        var loginUser = new UserData("test", "test", null);
        var result = facade.login(loginUser);
        assertNotNull(result);
        assertNotNull(result.authToken());
        assertEquals(loginUser.username(), result.username());
    }

    @Test
    public void loginBadPassword() throws Exception {
        facade.register(user);
        var badUser = new UserData("test", "badPassword", null);
        ServerException ex = assertThrows(ServerException.class, () -> facade.login(badUser));
        assertEquals(ex.getCode(), 401);
    }
}
