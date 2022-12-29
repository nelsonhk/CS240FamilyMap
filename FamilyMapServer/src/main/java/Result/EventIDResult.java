package Result;

/**
 * Result class for finding specific event based on EventID
 */
public class EventIDResult {

    /**
     * Username of user account this event belongs to
     */
    private String associatedUsername;

    /**
     * Event’s unique ID (non-empty string)
     */
    private String eventID;

    /**
     * ID of the person this event belongs to (non-empty string)
     */
    private String personID;

    /**
     * Latitude of the event’s location (number)
     */
    private float latitude;

    /**
     * Longitude of the event’s location (number)
     */
    private float longitude;

    /**
     * Name of country where event occurred (non-empty string)
     */
    private String country;

    /**
     * Name of city where event occurred (non-empty string)
     */
    private String city;

    /**
     * Type of event (“birth”, “baptism”, etc.) (non-empty string)
     */
    private String eventType;

    /**
     * Year the event occurred (integer)
     */
    private int year;

    /**
     * message for successful and unsuccessful find of event
     */
    private String message;

    /**
     * true if successful, false if unsuccessful
     */
    private boolean success;

    /**
     * Constructor for successful event response
     * @param associatedUsername
     * @param eventID
     * @param personID
     * @param latitude
     * @param longitude
     * @param country
     * @param city
     * @param eventType
     * @param year
     * @param success
     */
    public EventIDResult(String associatedUsername, String eventID, String personID,
                         float latitude, float longitude, String country, String city,
                         String eventType, int year, boolean success) {
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.success = success;
    }

    /**
     * Constructor for unsuccessful event response
     * @param message
     * @param success
     */
    public EventIDResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * Empty constructor
     */
    public EventIDResult() {}

    public boolean isSuccess() {
        return success;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
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

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
