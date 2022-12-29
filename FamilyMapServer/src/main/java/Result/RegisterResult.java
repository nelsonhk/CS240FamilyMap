package Result;

public class RegisterResult {
    /**
     * AuthToken
     */
    private String authtoken;

    /**
     * username for user attempting to register
     */
    private String username;

    /**
     * personID of user attempting to register
     */
    private String personID;

    /**
     * error message if registration is unsuccessful
     */
    private String message;

    /**
     * true if successful, false if unsuccessful
     */
    private boolean success;

    /**
     * Empty constructor for RegisterResult
     */
     public RegisterResult() {}

    /**
     * Constructor for a successful registration
     * @param authToken
     * @param username
     * @param personID
     * @param success
     */
    public RegisterResult(String authToken, String username, String personID, boolean success) {
        this.authtoken = authToken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }

    /**
     * Constructor for unsuccessful registration
     * @param message
     * @param success
     */
    public RegisterResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}
