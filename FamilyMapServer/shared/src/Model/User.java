package Model;

/**
 * User created upon registration, represents a person using the application. Holds user information
 * including username/password, as well as the user's name and gender.
 */
public class User {

    /**
     * The username for the user.
     */
    private String username;

    /**
     * The password for the user.
     */
    private String password;

    /**
     * The user's email address.
     */
    private String email;

    /**
     * The user's given name.
     */
    private String firstName;

    /**
     * The user's surname.
     */
    private String lastName;

    /**
     * The user's gender. Either 'f' for female or 'm' for male.
     */
    private String gender;

    /**
     * The person id of the person a user represents.
     */
    private String personID;

    /**
     * Constructor constructs a User object using values passed in.
     *
     * @param username defines value for the username of the new User object.
     * @param password defines value for the password of the new User Object.
     * @param email defines value for the emailAddress
     * @param firstName defines value for the firstName
     * @param lastName defines value for the lastName
     * @param gender defines value for the gender
     * @param personID defines value for the personID
     */
    public User (String username, String password, String email, String firstName,
                 String lastName, String gender, String personID) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * Another constructor, used when registering a new user.
     * @param username
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param gender
     */
    public User(String username, String password, String email, String firstName,
                String lastName, String gender) {
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Checks to see if Object o and this are equal.
     *
     * @param o is an object of unknown type, likely a User object.
     * @return false if objects are not equal; returns true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof User) {
            User oUser = (User) o;
            return oUser.getUsername().equals(getUsername()) &&
                    oUser.getPassword().equals(getPassword()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals(getLastName()) &&
                    oUser.getGender().equals(getGender()) &&
                    oUser.getPersonID().equals(getPersonID());
        } else {
            return false;
        }
    }
}
