package ServiceTests;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.AuthToken;
import Model.Event;
import Result.EventIDResult;
import Service.EventID;
import org.junit.jupiter.api.*;

public class EventIDServiceTest {

    Database db = new Database();
    EventID eventID;
    EventDao eventDao;
    Event event;
    AuthToken authToken;

    @BeforeEach
    public void setup() throws DataAccessException {
        db.openConnection();
        eventDao = new EventDao(db.getConnection());
        eventID = new EventID();

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

    @AfterEach
    public void teardown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    @DisplayName("Event Successful Test")
    public void eventSuccessTest() throws DataAccessException {

        eventDao.createEvent(event);

        Event actualEvent = eventDao.findEventByID(event.getEventID());

        Assertions.assertEquals(event.getEventType(), actualEvent.getEventType());
        Assertions.assertEquals(event.getUsername(), actualEvent.getUsername());
        Assertions.assertEquals(event.getLatitude(), actualEvent.getLatitude());

    }

    @Test
    @DisplayName("Event Fail Test")
    public void eventFailTest() {

        EventIDResult actualEventIDResult = eventID.event(event.getEventID(), "123");

        Assertions.assertFalse(actualEventIDResult.isSuccess());

    }

}
