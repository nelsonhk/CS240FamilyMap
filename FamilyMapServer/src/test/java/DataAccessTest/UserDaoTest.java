package DataAccessTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.sql.Connection;

public class UserDaoTest {
    private UserDao userDao;
    private User testUser;
    private Database db;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        testUser = new User("nelsonhk", "heyROCKSTAR", "nelson@byu.edu",
                "Hannah", "Nelson", "f", "hannah123");
        Connection conn = db.getConnection();
        db.clearTables();
        userDao = new UserDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    @DisplayName("Successful User Creation")
    public void userCreationSuccessTest () throws DataAccessException {
        User actualUser;
        userDao.createUser(testUser);
        actualUser = userDao.findUserByUsername(testUser.getUsername());
        Assertions.assertEquals(testUser, actualUser);
    }

    @Test
    @DisplayName("Failed User Creation")
    public void userCreationFailTest () throws DataAccessException {
        userDao.createUser(testUser);
        Assertions.assertThrows(DataAccessException.class, ()-> userDao.createUser(testUser));
    }

    @Test
    @DisplayName("Successful Find User by Username")
    public void findByUsernameSuccessTest () throws DataAccessException {
        User actualUser;
        userDao.createUser(testUser);
        actualUser = userDao.findUserByUsername(testUser.getUsername());
        Assertions.assertEquals(testUser, actualUser);
    }

    @Test
    @DisplayName("Fail Find User by Username")
    public void findByUsernameFailTest () throws DataAccessException {
        Assertions.assertNull(userDao.findUserByUsername(testUser.getUsername()));
    }

    @Test
    @DisplayName("Clear User Table Success")
    public void clearUserSuccessTest () throws DataAccessException {
        userDao.createUser(testUser);
        userDao.clear();
        Assertions.assertNull(userDao.findUserByUsername(testUser.getUsername()));
    }

}
