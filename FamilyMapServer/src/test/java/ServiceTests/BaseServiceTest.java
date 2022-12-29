package ServiceTests;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import Service.BaseService;
import org.junit.jupiter.api.*;

import java.sql.Connection;

public class BaseServiceTest {

    private String username = "nelsonhk";
    Database db = new Database();
    BaseService baseService = new BaseService();
    UserDao userDao;
    User user;

    @BeforeEach
    public void setup() throws DataAccessException {
        Connection conn = db.getConnection();
        userDao = new UserDao(conn);
        user = new User(
                username,
                "password",
                "hknelson0@gmail.com",
                "Hannah",
                "Nelson",
                "f",
                "person1"
        );
    }


    @Test
    @DisplayName("Check Username Success Test")
    public void checkUsernameSuccessTest () throws DataAccessException {
        userDao.createUser(user);

        db.closeConnection(true);

        Assertions.assertTrue(baseService.checkUsername(username));
    }

    @Test
    @DisplayName("Check Username Fail Test")
    public void checkUsernameFailTest() throws DataAccessException {
        db.clearTables();

        db.closeConnection(true);

        Assertions.assertFalse(baseService.checkUsername(username));
    }

}
