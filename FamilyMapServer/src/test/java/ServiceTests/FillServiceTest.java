package ServiceTests;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.User;
import Result.FillResult;
import Service.Fill;
import org.junit.jupiter.api.*;

public class FillServiceTest {

    Database db;
    UserDao userDao;
    User user;
    Fill fill;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        db.openConnection();
        userDao = new UserDao(db.getConnection());
        fill = new Fill();

        user = new User(
                "nelsonhk",
                "password",
                "hknelson0@gmail.com",
                "Hannah",
                "Nelson",
                "f",
                "person1"
        );

    }

    @Test
    @DisplayName("Fill Service Success Test")
    public void fillServiceSuccessTest() throws DataAccessException {
        db.clearTables();

        userDao.createUser(user);

        db.closeConnection(true);

        FillResult fillResult = fill.fill(user.getUsername(), 4);

        String expectedMessage = "Successfully added 31 persons and 62 events to the database.";
        Assertions.assertEquals(expectedMessage, fillResult.getMessage());

    }

    @Test
    @DisplayName("Fill Service Fail Test")
    public void fillServiceFailTest() throws DataAccessException {

        db.clearTables();
        db.closeConnection(true);

        FillResult fillResult = fill.fill(user.getUsername(), 4);

        String expectedMessage = "Could not find username";
        Assertions.assertEquals(expectedMessage, fillResult.getMessage());

    }

}
