package Result;

public class LoginResult {
    /**
     * authToken for current user
     */
    private String authtoken;

    /**
     * username of current user
     */
    private String username;

    /**
     * personID of current user
     */
    private String personID;

    /**
     * true if successful, false if unsuccessful
     */
    private boolean success;

    /**
     * error message when error occurs
     */
    private String message;

    /**
     * Constructor for a successful login
     * @param authtoken
     * @param username
     * @param personID
     * @param success
     */
    public LoginResult(String authtoken, String username, String personID, boolean success) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }

    /**
     * Constructor for an unsuccessful login
     * @param success
     * @param message
     */
    public LoginResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResult() {}

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
