package android.bignerdranch.familymapapplication.data.DataCache;

import android.bignerdranch.familymapapplication.data.EventActivity;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Event;
import Model.Person;

public class DataCache {

    private static DataCache instance;

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    /**
     * PEOPLE/EVENTS - used upon register/login, these functions cache the people/events
     *                  applicable for the logged in user
     */

    // Key personID or eventID, value Person or Event
    Map<String, Person> people = new HashMap<>();
    public void loadPeople(Person[] personArray) {
        for (Person person : personArray) {
            people.put(person.getPersonID(), person);
        }
    }

    static final Map<String, Event> events = new HashMap<>();
    public void loadEvents(Event[] eventArray) {
        for (Event event : eventArray) {
            events.put(event.getEventID(), event);
        }
    }

    // Key personID, value List of Events in that person's life
    Map<String, List<Event>> eventsForPerson = new HashMap<>();
    public void loadEventsForPerson(Person person, Event[] events) {
        List<Event> eventsList = new ArrayList<>();
        for (Event event : events) {
            if (event.getPersonID().equals(person.getPersonID())) {
                List<Event> eventList = eventsForPerson.get(person.getPersonID());
                if (eventList != null) {
                    eventsList = eventsForPerson.get(person.getPersonID());
                }
                eventsList.add(event);
            }
        }
        eventsForPerson.put(person.getPersonID(), eventsList);
    }

    // Key personID, value List of Persons that are related to the person (family members)
    Map<String, List<Person>> familyForPerson = new HashMap<>();
    public void loadFamilyForPerson(Person person, Person[] persons) {
        List<Person> familyMembers = new ArrayList<>();
        for (Person person1 : persons) {
            String personID = person.getPersonID();
            String person1ID = person1.getPersonID();
            if (person1ID.equals(person.getMotherID())
                    || person1ID.equals(person.getFatherID())
                    || person1ID.equals(person.getSpouseID())
                    || personID.equals(person1.getMotherID())
                    || personID.equals(person1.getFatherID())
                    || personID.equals(person1.getSpouseID())
                    && !person1ID.equals(person.getPersonID())) {
                familyMembers.add(person1);
            }
        }
        familyForPerson.put(person.getPersonID(), familyMembers);

    }

    public List<Person> searchPersons(String query) {
        List<Person> matchPersons = new ArrayList<>();

        for (String personID : people.keySet()) {
            if (people.get(personID).getFirstName().toLowerCase().contains(query) ||
                    people.get(personID).getLastName().toLowerCase().contains(query)) {
                matchPersons.add(people.get(personID));
            }
        }

        return matchPersons;
    }

    public List<Event> searchEvents(String query) {
        List<Event> matchEvents = new ArrayList<>();

        for (String eventID : events.keySet()) {
            if (events.get(eventID).getEventType().toLowerCase().contains(query) ||
                    events.get(eventID).getCity().toLowerCase().contains(query) ||
                    events.get(eventID).getCountry().toLowerCase().contains(query) ||
                    String.valueOf(events.get(eventID).getYear()).contains(query) ||
                    people.get(events.get(eventID).getPersonID())
                            .getFirstName().toLowerCase().contains(query) ||
                    people.get(events.get(eventID).getPersonID())
                            .getLastName().toLowerCase().contains(query)
            ) {
                matchEvents.add(events.get(eventID));
            }
        }

        return matchEvents;

    }

    /**
     * MARKERS - these values and methods are used to cache the map markers
     */

    // Store all markers in the map - key EventID, value Marker object
    Map<String, Marker> mMarkerMap = new HashMap<>();
    public void loadMarkers(Event event, Marker marker) {
        marker.setTag(event.getEventID());
        mMarkerMap.put(event.getEventID(), marker);
    }


    // Keep track of event colors
    float birthColor= BitmapDescriptorFactory.HUE_RED;
    float marriageColor = BitmapDescriptorFactory.HUE_GREEN;
    float deathColor = BitmapDescriptorFactory.HUE_AZURE;

    /**
     * SETTINGS - find boolean values for the settings, as well as methods called in login
     *              to help populate valuable lists for filters
     */

    // Each setting has a getter and setter (see below)
    boolean showLifeStoryLines = true;
    boolean showFamilyTreeLines = true;
    boolean showSpouseLines = true;
    boolean showFathersSide = true;
    boolean showMothersSide = true;
    boolean showMaleEvents = true;
    boolean showFemaleEvents = true;

    int lifeStoryLine = Color.CYAN;
    int familyTreeLine = Color.MAGENTA;
    int spouseLine = Color.DKGRAY;

    float lineWidth = 10.0F;

    // Store the people on the father's side of the person logged in
    List<Person> fathersSide = new ArrayList<>();
    List<Person> mothersSide = new ArrayList<>();

    public void loadFamilySides(String rootPersonID) {
        // rootPerson is the logged in user (ex: Sheila_Parker)
        Person rootPerson = people.get(rootPersonID);

        Person father = people.get(rootPerson.getFatherID());
        Person mother = people.get(rootPerson.getMotherID());

        //call recursive helper functions - once for father and once for mother
        familySideHelper(father, fathersSide);
        familySideHelper(mother, mothersSide);
    }

    //takes a person in as a parameter, add them to the list, and calls recursively on their parents
    private void familySideHelper(Person person, List sideToAdd) {
        sideToAdd.add(person);
        if (person.getFatherID() != null) {
            familySideHelper(people.get(person.getFatherID()), sideToAdd);
            familySideHelper(people.get(person.getMotherID()), sideToAdd);
        }
    }

    // Store the people according to their gender
    List<Person> females = new ArrayList<>();
    List<Person> males = new ArrayList<>();
    public void loadByGender(){
        for (String personID : people.keySet()) {
            if (people.get(personID).getGender().equals("f")) {
                females.add(people.get(personID));
            } else {
                males.add(people.get(personID));
            }
        }
    }

    // Create custom map of events based on filters set
    public Map<String, Event> filterEvents(Map<String, Event> eventMap) {

        Map<String, Event> filteredEventMap = new HashMap(eventMap);

        // Take the events out of eventsMap if settings are false
        boolean filterByFathersSide = isShowFathersSide();
        boolean filterByMothersSide = isShowMothersSide();
        boolean filterByMaleGender = isShowMaleEvents();
        boolean filterByFemaleGender = isShowFemaleEvents();

        if (filterByFathersSide == false) {

            Map<String, List<Event>> eventsForPeopleMap = getEventsForPerson();
            List<Person> fathersSide = getFathersSide();

            for (Person person : fathersSide) {
                List<Event> eventsForPerson = eventsForPeopleMap.get(person.getPersonID());
                for (Event event : eventsForPerson) {
                    filteredEventMap.remove(event.getEventID());
                }
            }
        }

        if (filterByMothersSide == false) {

            Map<String, List<Event>> eventsForPeopleMap = getEventsForPerson();
            List<Person> mothersSide = getMothersSide();

            for (Person person : mothersSide) {
                List<Event> eventsForPerson = eventsForPeopleMap.get(person.getPersonID());
                for (Event event : eventsForPerson) {
                    filteredEventMap.remove(event.getEventID());
                }
            }
        }

        if (filterByMaleGender == false) {

            Map<String, List<Event>> eventsForPeopleMap = getEventsForPerson();
            List<Person> males = getMales();

            for (Person person : males) {
                List<Event> eventsForPerson = eventsForPeopleMap.get(person.getPersonID());
                for (Event event : eventsForPerson) {
                    filteredEventMap.remove(event.getEventID());
                }
            }
        }

        if (filterByFemaleGender == false) {
            Map<String, List<Event>> eventsForPeopleMap = getEventsForPerson();
            List<Person> females = getFemales();

            for (Person person : females) {
                List<Event> eventsForPerson = eventsForPeopleMap.get(person.getPersonID());
                for (Event event : eventsForPerson) {
                    filteredEventMap.remove(event.getEventID());
                }
            }
        }

        return filteredEventMap;
    }

    /**
     * Getters and Setters
     */

    public float getBirthColor() {
        return birthColor;
    }

    public float getMarriageColor() {
        return marriageColor;
    }

    public float getDeathColor() {
        return deathColor;
    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public Map<String, List<Event>> getEventsForPerson() {
        return eventsForPerson;
    }

    public Map<String, List<Person>> getFamilyForPerson() { return familyForPerson; }

    public Map<String, Marker> getMarkerMap() {
        return mMarkerMap;
    }

    public boolean isShowLifeStoryLines() {
        return showLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        this.showLifeStoryLines = showLifeStoryLines;
    }

    public boolean isShowFamilyTreeLines() {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public boolean isShowSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public boolean isShowFathersSide() {
        return showFathersSide;
    }

    public void setShowFathersSide(boolean showFathersSide) {
        this.showFathersSide = showFathersSide;
    }

    public boolean isShowMothersSide() {
        return showMothersSide;
    }

    public void setShowMothersSide(boolean showMothersSide) {
        this.showMothersSide = showMothersSide;
    }

    public boolean isShowMaleEvents() {
        return showMaleEvents;
    }

    public void setShowMaleEvents(boolean showMaleEvents) {
        this.showMaleEvents = showMaleEvents;
    }

    public boolean isShowFemaleEvents() {
        return showFemaleEvents;
    }

    public void setShowFemaleEvents(boolean showFemaleEvents) {
        this.showFemaleEvents = showFemaleEvents;
    }

    public List<Person> getFathersSide() {
        return fathersSide;
    }

    public List<Person> getMothersSide() {
        return mothersSide;
    }

    public List<Person> getFemales() {
        return females;
    }

    public List<Person> getMales() {
        return males;
    }

    public float getLifeStoryLine() {
        return lifeStoryLine;
    }

    public float getFamilyTreeLine() {
        return familyTreeLine;
    }

    public float getSpouseLine() {
        return spouseLine;
    }

    public float getLineWidth() {
        return lineWidth;
    }

}
