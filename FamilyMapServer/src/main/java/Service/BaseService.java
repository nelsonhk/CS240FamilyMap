package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;

import java.sql.Connection;

public class BaseService {
    public boolean checkUsername (String username) throws DataAccessException {
        boolean success = false;
        Database database = new Database();
        try {
            Connection conn = database.openConnection();
            UserDao userDao = new UserDao(conn);
            if (userDao.findUserByUsername(username) != null) {
                success = true;
                database.closeConnection(true);
            } else {
                database.closeConnection(false);
            }
        } catch (DataAccessException e) {
            throw new DataAccessException("DataAccessException when checking for username in Database");
        }
        return success;
    }
}
