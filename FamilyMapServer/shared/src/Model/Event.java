package Model;

/**
 * Event that takes place during a person's life. Features such details as the location of the event,
 * the type of event, and tags the event to a person and the user who created it.
 */
public class Event {
    /**
     * The unique id for the event.
     */
    private String eventID;

    /**
     * The username of the user who has created the event.
     */
    private String associatedUsername;

    /**
     * The person during whose life the event occurred.
     */
    private String personID;

    /**
     * The latitude of the event's location.
     */
    private float latitude;

    /**
     * The longitude of the event's location.
     */
    private float longitude;

    /**
     * The country in which the event occurred.
     */
    private String country;

    /**
     * The city in which the event occurred.
     */
    private String city;

    /**
     * The type of life event (example: birth, baptism, marriage, death).
     */
    private String eventType;

    /**
     * The year in which the event occurred.
     */
    private int year;

    /**
     * Constructor constructs new Event object using values passed in.
     *
     * @param eventID defines value for the eventID of the new Event object.
     * @param username defines value for the username of the new Event object.
     * @param personID defines value for the personID of the new Event object.
     * @param latitude defines value for the latitude of the new Event object.
     * @param longitude defines value for the longitude of the new Event object.
     * @param country defines value for the country of the new Event object.
     * @param city defines value for the city of the new Event object.
     * @param eventType defines value for the eventType of the new Event object.
     * @param year defines value for the year of the new Event object.
     */
    public Event(String eventID, String username, String personID, float latitude, float longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public Event(String eventID, String username, float latitude, float longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUsername() {
        return associatedUsername;
    }

    public void setUsername(String username) {
        this.associatedUsername = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Checks to see if Object o and this are equal.
     *
     * @param o is an object of unknown type, likely an Event object.
     * @return false if objects are not equal; returns true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getUsername().equals(getUsername()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear());
        } else {
            return false;
        }
    }
}
