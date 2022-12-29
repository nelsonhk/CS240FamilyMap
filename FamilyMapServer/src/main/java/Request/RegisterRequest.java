package Request;

public class RegisterRequest {
    /**
     * The username for the requested user.
     */
    private String username;

    /**
     * The password for the request user.
     */
    private String password;

    /**
     * The email address of the requested user.
     */
    private String email;

    /**
     * The given name.
     */
    private String firstName;

    /**
     * The surname.
     */
    private String lastName;

    /**
     * The gender of the requested user
     */
    private String gender;

    /**
     * Constructor constructs RegisterRequest object with specified values.
     * @param username specifies the username for the user
     * @param password specifies the password for the new user
     * @param email specifies the email address of the new user
     * @param firstName specifies the first name of the user
     * @param lastName specifies the last name of the user
     * @param gender specifies the gender of the user
     */
    public RegisterRequest(String username, String password, String email,
                           String firstName, String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmailAddress() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

}
