package Result;

import Model.Event;

/**
 * Result class for returning all events for all family members of the current user.
 */
public class EventsResult {

    /**
     * Array of Event objects
     */
    private Event[] data;

    /**
     * message for successful and unsuccessful obtaining of data
     */
    private String message;

    /**
     * true if successful, false if unsuccessful
     */
    private boolean success;

    /**
     * Constructor for successful response
     * @param data
     * @param success
     */
    public EventsResult(Event[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    /**
     * Constructor for unsuccessful response
     * @param message
     * @param success
     */
    public EventsResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * Empty constructor
     */
    public EventsResult() {}

    public boolean isSuccess() {
        return success;
    }

    public Event[] getData() {
        return data;
    }
}
