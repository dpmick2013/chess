package service;

import dataaccess.DataAccess;

public class AdminService {
    private DataAccess dataAccess;

    public AdminService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
        dataAccess.clear();
    }
}
