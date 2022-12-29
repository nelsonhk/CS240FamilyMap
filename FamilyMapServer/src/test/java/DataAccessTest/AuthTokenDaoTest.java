package DataAccessTest;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import Model.AuthToken;
import org.junit.jupiter.api.*;

import java.sql.Connection;

public class AuthTokenDaoTest {
    private Database db;
    private AuthToken testAuthToken;
    private AuthTokenDao authTokenDao;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        testAuthToken = new AuthToken("12345", "nelsonhk");
        Connection conn = db.getConnection();
        db.clearTables();
        authTokenDao = new AuthTokenDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    @DisplayName("Success Create AuthToken")
    public void createAuthTokenSuccessTest() throws DataAccessException {
        String actualUsername;
        authTokenDao.createAuthToken(testAuthToken);
        actualUsername = authTokenDao.getUsername(testAuthToken.getAuthtoken());
        Assertions.assertEquals(testAuthToken.getUsername(), actualUsername);
    }

    @Test
    @DisplayName("Fail Create AuthToken")
    public void createAuthTokenFailTest() throws DataAccessException {
        Assertions.assertNull(authTokenDao.getUsername(testAuthToken.getUsername()));
    }

    @Test
    @DisplayName("Success Get Username")
    public void getUsernameSuccessTest () throws DataAccessException {
        String actualUsername;
        authTokenDao.createAuthToken(testAuthToken);
        actualUsername = authTokenDao.getUsername(testAuthToken.getAuthtoken());
        Assertions.assertEquals(testAuthToken.getUsername(), actualUsername);
    }

    @Test
    @DisplayName("Fail Get Username")
    public void getUsernameFailTest () throws DataAccessException {
        Assertions.assertNull(authTokenDao.getUsername(testAuthToken.getAuthtoken()));
    }

    @Test
    @DisplayName("Success Clear AuthToken Table")
    public void clearAuthTokenTest () throws DataAccessException {
        authTokenDao.createAuthToken(testAuthToken);
        authTokenDao.clear();
        Assertions.assertNull(authTokenDao.getUsername(testAuthToken.getAuthtoken()));
    }

}
