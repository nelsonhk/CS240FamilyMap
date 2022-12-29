package Model;

public class AuthToken {
    /**
     * Unique id for a given authToken.
     */
    private String authtoken;

    /**
     * The user associated with the given authToken.
     */
    private String username;

    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Checks to see if Object o and this are equal.
     *
     * @param o is an object of unknown type, likely an AuthToken object.
     * @return false if objects are not equal; returns true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthToken) {
            AuthToken oAuthToken = (AuthToken) o;
            return oAuthToken.getAuthtoken().equals(getAuthtoken()) &&
                    oAuthToken.getUsername().equals(getUsername());
        } else {
            return false;
        }
    }
}
