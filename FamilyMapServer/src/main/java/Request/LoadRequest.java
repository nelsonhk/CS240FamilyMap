package Request;

import Model.Event;
import Model.Person;
import Model.User;
import java.util.List;

public class LoadRequest {

    /**
     * users to be created
     */
    private User[] users;

    /**
     * people connected to users
     */
    private Person[] persons;

    /**
     * events connected to users
     */
    private Event[] events;

    /**
     * Constructs load request.
     * @param userList
     * @param personList
     * @param eventList
     */
    public LoadRequest(User[] userList, Person[] personList, Event[] eventList) {
        this.users = userList;
        this.persons = personList;
        this.events = eventList;
    }

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }
}
