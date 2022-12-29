package Result;

import Model.Person;

public class PersonsResult {

    /**
     * Array of Person objects
     */
    private Person[] data;

    /**
     * message for successful and unsuccessful obtaining of personsList
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
    public PersonsResult(Person[] data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public PersonsResult() {

    }

    public Person[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    /**
     * Constructor for unsuccessful response
     * @param message
     * @param success
     */
    public PersonsResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

}
