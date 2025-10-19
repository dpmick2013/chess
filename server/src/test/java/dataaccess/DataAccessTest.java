package dataaccess;

import datamodel.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTest {

    @Test
    void clear() {
        var user = new UserData("joe", "j@j", "j");
        DataAccess da = new MemoryDataAccess();
        assertNull(da.getUser(user.username()));
        da.createUser(user);
        assertNotNull(da.getUser(user.username()));
        da.clear();
        assertNull(da.getUser(user.username()));
    }

    @Test
    void createUser() {

    }
}
