package Request;

public class LoginRequest {

    /**
     * Username requesting to be logged in
     */
    String username;

    /**
     * Password for logging in
     */
    String password;

    /**
     * Constructs new LoginRequest object with username and password
     * @param username requesting to be logged in
     * @param password user's password for logging in
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
