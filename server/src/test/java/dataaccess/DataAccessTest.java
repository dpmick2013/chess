package dataaccess;

import datamodel.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {

    @Test
    void clear() throws Exception {
        var user = new UserData("joe", "j@j", "j");
        DataAccess da = new MySqlDataAccess();
        da.createUser(user);
        da.clear();
    }

    @Test
    void createUser() throws Exception {
        var user = new UserData("test", "test", "test");
        DataAccess da = new MySqlDataAccess();
        assertDoesNotThrow(() -> da.createUser(user));
    }
}
