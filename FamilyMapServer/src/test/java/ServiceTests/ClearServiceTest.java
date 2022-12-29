package ServiceTests;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Result.ClearResult;
import Service.Clear;
import org.junit.jupiter.api.*;

public class ClearServiceTest {

    Database db = new Database();
    Clear clear = new Clear();

    UserDao userDao;
    PersonDao personDao;
    EventDao eventDao;
    AuthTokenDao authTokenDao;

    User user;
    Person person;
    Event event;
    AuthToken authToken;

    @BeforeEach
    public void setup() throws DataAccessException {
        userDao = new UserDao(db.getConnection());
        personDao = new PersonDao(db.getConnection());
        eventDao = new EventDao(db.getConnection());
        authTokenDao = new AuthTokenDao(db.getConnection());

        user = new User(
                "nelsonhk",
                "password",
                "hknelson0@gmail.com",
                "Hannah",
                "Nelson",
                "f",
                "TestPerson1"
        );

        person = new Person(
                "TestPerson",
                "nelsonhk",
                "Hannah",
                "Nelson",
                "f",
                "motherID",
                "fatherID",
                "TestPerson1"
        );

        event = new Event("TestEvent1",
                "nelsonhk",
                "hannah123",
                35.9f,
                140.1f,
                "USA",
                "Olympia",
                "Testing_Event",
                2021
        );

        authToken = new AuthToken(
                "12345",
                "nelsonhk"
        );

    }

    @Test
    @DisplayName("Clear Service Success Test")
    public void clearServiceSuccessTest() throws DataAccessException {

        db.clearTables();

        userDao.createUser(user);
        personDao.createPerson(person);
        eventDao.createEvent(event);
        authTokenDao.createAuthToken(authToken);

        db.closeConnection(true);

        ClearResult clearResult = clear.clear();

        String expectedMessage = "Clear succeeded";

        Assertions.assertEquals(expectedMessage, clearResult.getMessage());
        Assertions.assertEquals(true, clearResult.isSuccess());
    }

    @Test
    @DisplayName("Clear Service Fail Test")
    public void clearServiceFailTest() {}

}
