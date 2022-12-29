package ServiceTests;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.User;
import Result.EventsResult;
import Result.FillResult;
import Service.EventID;
import Service.Events;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


public class EventsServiceTest {

    Database db = new Database();
    EventID eventID;
    EventDao eventDao;
    Event event1;
    Event event2;
    Event event3;
    Events events;
    AuthToken authToken;
    AuthTokenDao authTokenDao;
    UserDao userDao;
    User user;

    @BeforeEach
    public void setup() throws DataAccessException {
        db.openConnection();
        eventDao = new EventDao(db.getConnection());
        eventID = new EventID();
        events = new Events();

        event1 = new Event("TestEvent1",
                "nelsonhk",
                "hannah456",
                35.9f,
                140.1f,
                "USA",
                "Olympia",
                "Testing_Event",
                2021
        );

        event2 = new Event("TestEvent2",
                "nelsonhk",
                "hannah789",
                35.9f,
                140.1f,
                "USA",
                "Olympia",
                "Testing_Event",
                2021
        );

        event3 = new Event("TestEvent3",
                "nelsonhk",
                "hannah101",
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

        userDao = new UserDao(db.getConnection());

    }

    @Test
    @DisplayName("Events Service Success Test")
    public void eventsServiceSuccessTest() throws DataAccessException {
        db.clearTables();

        userDao.createUser(user);

        eventDao.createEvent(event1);
        eventDao.createEvent(event2);
        eventDao.createEvent(event3);

        List<Event> eventsList = new ArrayList<Event>();
        eventsList.add(event1);
        eventsList.add(event2);
        eventsList.add(event3);

        authTokenDao.createAuthToken(authToken);

        db.closeConnection(true);

        EventsResult eventsResult = events.events(authToken.getAuthtoken());

        for (int i = 0; i < eventsList.size(); i++) {
            Assertions.assertEquals(eventsList.get(i), eventsResult.getData()[i]);
        }
    }

    @Test
    @DisplayName("Events Service Fail Test")
    public void eventsServiceFailTest() throws DataAccessException {

        db.clearTables();
        db.closeConnection(true);

        EventsResult eventsResult = events.events(authToken.getAuthtoken());
        Assertions.assertEquals(false, eventsResult.isSuccess());

    }

}
