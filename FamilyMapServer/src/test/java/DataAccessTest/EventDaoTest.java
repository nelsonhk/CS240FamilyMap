package DataAccessTest;

import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.EventDao;
import Model.Event;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class EventDaoTest {
    private Database db;
    private EventDao eventDao;
    private Event testEvent;
    private Event testEvent1;
    private Event testEvent2;
    private List<Event> eventList;

    @BeforeEach
    public void setup() throws DataAccessException {
        db = new Database();
        testEvent = new Event("TestEvent", "nelsonhk", "hannah123",
                35.9f, 140.1f, "USA", "Olympia",
                "Testing_Event", 2021);
        Connection conn = db.getConnection();
        db.clearTables();
        eventDao = new EventDao(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    @DisplayName("Successful Event Creation")
    public void createEventSuccess () throws DataAccessException {
        Event actualEvent;
        eventDao.createEvent(testEvent);
        actualEvent = eventDao.findEventByID(testEvent.getEventID());
        Assertions.assertEquals(testEvent, actualEvent);
    }

    @Test
    @DisplayName("Failed Event Creation")
    public void createEventFail () throws DataAccessException {
        eventDao.createEvent(testEvent);
        Assertions.assertThrows(DataAccessException.class, ()-> eventDao.createEvent(testEvent));
    }

    @Test
    @DisplayName("Success Find Event by ID")
    public void findByIDSuccess () throws DataAccessException {
        Event actualEvent;
        eventDao.createEvent(testEvent);
        actualEvent = eventDao.findEventByID(testEvent.getEventID());
        Assertions.assertEquals(testEvent, actualEvent);
    }

    @Test
    @DisplayName("Fail Find Event by ID")
    public void findByIDFail () throws DataAccessException {
        Assertions.assertNull(eventDao.findEventByID(testEvent.getEventID()));
    }

    @Test
    @DisplayName("Success List of Events for User")
    public void eventListSuccessTest () throws DataAccessException {
        testEvent1 = new Event("TestEvent1", "nelsonhk", "hannah123",
                35.9f, 140.1f, "USA", "Olympia",
                "Testing_Event", 2021);
        testEvent2 = new Event("TestEvent2", "nelsonhk", "hannah123",
                35.9f, 140.1f, "USA", "Olympia",
                "Testing_Event", 2021);

        eventList = new ArrayList<Event>();
        eventList.add(testEvent);
        eventList.add(testEvent1);
        eventList.add(testEvent2);

        Event[] eventListArray = new Event[eventList.size()];
        eventList.toArray(eventListArray);

        eventDao.createEvent(testEvent);
        eventDao.createEvent(testEvent1);
        eventDao.createEvent(testEvent2);
        Event[] actualList = eventDao.getEventsForUser(testEvent.getUsername());

        for (int i = 0; i < eventListArray.length; i++) {
            Assertions.assertEquals(eventListArray[i], actualList[i]);
        }
    }

    @Test
    @DisplayName("Fail List of Events for User")
    public void eventListFailTest () throws DataAccessException {
        testEvent1 = new Event("TestEvent1", "nelsonhk", "hannah123",
                35.9f, 140.1f, "USA", "Olympia",
                "Testing_Event", 2021);
        testEvent2 = new Event("TestEvent2", "nelsonhk", "hannah123",
                35.9f, 140.1f, "USA", "Olympia",
                "Testing_Event", 2021);

        eventList = new ArrayList<>();
        eventList.add(testEvent);
        eventList.add(testEvent1);
        eventList.add(testEvent2);

        Event[] actualList = eventDao.getEventsForUser(testEvent.getUsername());
        Assertions.assertNotEquals(eventList, actualList);
    }

    @Test
    @DisplayName("Success Clear Events Table")
    public void clearEventSuccessTest () throws DataAccessException {
        eventDao.createEvent(testEvent);
        eventDao.clear();
        Assertions.assertNull(eventDao.findEventByID(testEvent.getEventID()));
    }

    @Test
    @DisplayName("Clear Events for User Success")
    public void clearEventsForUserSuccessTest () throws DataAccessException {
        testEvent1 = new Event("TestEvent1", "nelsonhk", "hannah123",
                35.9f, 140.1f, "USA", "Olympia",
                "Testing_Event", 2021);
        testEvent2 = new Event("TestEvent2", "hannahKnelson", "hannah123",
                35.9f, 140.1f, "USA", "Olympia",
                "Testing_Event", 2021);

        eventDao.createEvent(testEvent);
        eventDao.createEvent(testEvent1);
        eventDao.createEvent(testEvent2);

        eventDao.clearForUsername("nelsonhk");
        Event [] nelsonhkEvents = eventDao.getEventsForUser("nelsonhk");
        Event [] hannahKnelsonEvents = eventDao.getEventsForUser("hannahKnelson");

        Assertions.assertEquals(nelsonhkEvents.length, 0);
        Assertions.assertNotEquals(hannahKnelsonEvents.length, 0);
    }

}
