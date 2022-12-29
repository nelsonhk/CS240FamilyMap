package android.bignerdranch.familymapapplication.datacache;

import android.bignerdranch.familymapapplication.data.DataCache.DataCache;
import android.bignerdranch.familymapapplication.data.ServerProxy.ServerProxy;

import org.junit.*;

import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;
import Request.LoginRequest;
import Result.LoginResult;

public class DataCacheTests {

    private String serverHost = "localhost";
    private String serverPort = "8080";
    private String[] args = new String[]{serverHost, serverPort};
    private ServerProxy mServerProxy = new ServerProxy(args);

    private DataCache mDataCache = DataCache.getInstance();

    @Before
    public void setup() {
        LoginRequest successLoginRequest = new LoginRequest("sheila", "parker");
        LoginResult loginResult = mServerProxy.login(successLoginRequest);

        Event[] events = mServerProxy.getEvents(loginResult.getAuthtoken()).getData();
        mDataCache.loadEvents(events);

        Person[] persons = mServerProxy.getPersons(loginResult.getAuthtoken()).getData();
        mDataCache.loadPeople(persons);

        for (Person person : persons) {
            mDataCache.loadEventsForPerson(person, events);
            mDataCache.loadFamilyForPerson(person, persons);
        }

        mDataCache.loadFamilySides(loginResult.getPersonID());
        mDataCache.loadByGender();
    }

    @Test
    public void familyRelationshipSuccess() {
        Map<String, List<Person>> familyForPerson = mDataCache.getFamilyForPerson();
        List<Person> familyForSheila = familyForPerson.get("Sheila_Parker");
        Assert.assertEquals(3, familyForSheila.size());
    }

    @Test
    public void familyRelationshipFail() {
        Map<String, List<Person>> familyForPerson = mDataCache.getFamilyForPerson();
        List<Person> familyForPatrick = familyForPerson.get("Patrick_Spencer");
        Assert.assertEquals(null, familyForPatrick);
    }
    
    @Test
    public void currentFilterSettingsSuccess1() {
        Map<String, Event> eventMap = mDataCache.getEvents();

        mDataCache.setShowMaleEvents(false);
        Map<String, Event> femaleEvents = mDataCache.filterEvents(eventMap);
        Assert.assertEquals(10, femaleEvents.size());
        mDataCache.setShowMaleEvents(true);

        mDataCache.setShowFemaleEvents(false);
        Map<String, Event> maleEvents = mDataCache.filterEvents(eventMap);
        Assert.assertEquals(6, maleEvents.size());
        mDataCache.setShowFemaleEvents(true);
    }

    @Test
    public void currentFilterSettingsSuccess2() {
        Map<String, Event> eventMap = mDataCache.getEvents();

        mDataCache.setShowMothersSide(false);
        Map<String, Event> fathersSideEvents = mDataCache.filterEvents(eventMap);
        Assert.assertEquals(11, fathersSideEvents.size());
        mDataCache.setShowMothersSide(true);

        mDataCache.setShowFathersSide(false);
        Map<String, Event> mothersSideEvents = mDataCache.filterEvents(eventMap);
        Assert.assertEquals(11, mothersSideEvents.size());
        mDataCache.setShowFathersSide(true);
    }

    @Test
    public void individualEventsChronologicalSuccess1() {
        List<Event> sheilaEvents = mDataCache.getEventsForPerson().get("Sheila_Parker");
        Event currEvent = sheilaEvents.get(0);
        boolean inOrder = true;
        for (Event event : sheilaEvents) {
            if (event.getYear() < currEvent.getYear()) {
                inOrder = false;
                break;
            }
        }
        Assert.assertTrue(inOrder);
    }

    @Test
    public void individualEventsChronologicalSuccess2() {
        List<Event> mrsEvents = mDataCache.getEventsForPerson().get("Ken_Rodham");
        Event currEvent = mrsEvents.get(0);
        boolean inOrder = true;
        for (Event event : mrsEvents) {
            if (event.getYear() < currEvent.getYear()) {
                inOrder = false;
                break;
            }
        }
        Assert.assertTrue(inOrder);
    }

    @Test
    public void searchPeopleEventsSuccess() {}

    @Test
    public void searchPeopleEventsFail() {}

}
