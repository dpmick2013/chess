package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import exception.ServerException;

import java.sql.SQLException;

public class AdminService {
    private final DataAccess dataAccess;

    public AdminService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() throws Exception {
        try {
            dataAccess.clear();
        } catch (DataAccessException ex) {
            if (ex.getCause() instanceof SQLException) {
                throw new ServerException("Error: Database connection failed", 500);
            }
        }
    }
}
