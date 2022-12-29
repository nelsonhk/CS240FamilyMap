package Model;

/**
 * A person in the family tree. This person has a unique personID and the Person object holds
 * personal information, as well as information to connect their family tree.
 */
public class Person {

    /**
     * A unique id for every person with a family tree.
     */
    private String personID;

    /**
     * The username associated with this person (may be null).
     */
    private String associatedUsername;

    /**
     * The given name of the person.
     */
    private String firstName;

    /**
     * The surname of the person.
     */
    private String lastName;

    /**
     * The gender of the person. Either 'f' for female or 'm' for male.
     */
    private String gender;

    /**
     * The person id of the person's mother. May be null.
     */
    private String motherID;

    /**
     * The person id of the person's father. May be null.
     */
    private String fatherID;

    /**
     * The person id of the person's spouse. May be null.
     */
    private String spouseID;

    /**
     * Constructor constructs a Person object using the values passed in.
     *
     * @param personID defines value for the personID of the new Person object.
     * @param associatedUsername defines value for the username of the new Person object.
     * @param firstName defines value for the firstName of the new Person object.
     * @param lastName defines value for the lastName of the new Person object.
     * @param gender defines value for the gender of the new Person object.
     * @param motherID defines value for the motherID of the new Person object.
     * @param fatherID defines value for the fatherID of the new Person object.
     * @param spouseID defines value for the spouseID of the new Person object.
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName,
                  String gender, String motherID, String fatherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.motherID = motherID;
        this.fatherID = fatherID;
        this.spouseID = spouseID;
    }

    public Person() {}

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
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

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    /**
     * Checks to see if Object o and this are equal.
     *
     * @param o is an object of unknown type, likely an Person object.
     * @return false if objects are not equal; returns true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    oPerson.getFatherID().equals(getFatherID()) &&
                    oPerson.getMotherID().equals(getMotherID()) &&
                    oPerson.getSpouseID().equals(getSpouseID());
        } else {
            return false;
        }
    }
}
