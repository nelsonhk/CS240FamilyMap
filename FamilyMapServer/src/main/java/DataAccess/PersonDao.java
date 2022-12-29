package DataAccess;

import Model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDao {
    /**
     * Connection to obtain data access for person table.
     */
    private final Connection conn;

    /**
     * Constructor constructs connection based off of URL given.
     * @param conn is the URL given when constructed.
     */
    public PersonDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts a person into the person table.
     * @param person Person object to be inserted into the table.
     * @throws DataAccessException if unable to insert the person.
     */
    public void createPerson (Person person) throws DataAccessException {
        String sql = "INSERT INTO Person (personID, username, firstName, lastName, gender, motherID, fatherID," +
                "spouseID) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getMotherID());
            stmt.setString(7, person.getFatherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered inserting into Person table");
        }
    }

    /**
     * Returns the Person object with the specified id.
     * @param personID specifices which Person object to pull.
     * @return Person object of person with the specified id
     * @throws DataAccessException if person is not found
     */
    public Person findByID (String personID) throws DataAccessException {
        Person person;
        ResultSet resultSet;
        String sql = "SELECT * FROM Person WHERE personID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                person = new Person(resultSet.getString("personID"), resultSet.getString("username"),
                        resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getString("gender"), resultSet.getString("motherID"),
                        resultSet.getString("fatherID"), resultSet.getString("spouseID"));
                return person;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered quering Person table for Person by PersonID");
        }
        return null;
    }

    /**
     * Returns a list of all family members of the current user.
     *
     * @param username Person object specified with username
     * @return list of all family members of person with specified username
     * @throws DataAccessException if person not found
     */
    public Person[] findFamilyByID (String username) throws DataAccessException {
        Person person;
        List<Person> familyList = new ArrayList<>();
        ResultSet resultSet;
        String sql = "SELECT * FROM Person WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                person = new Person(resultSet.getString("personID"), resultSet.getString("username"),
                        resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getString("gender"), resultSet.getString("motherID"),
                        resultSet.getString("fatherID"), resultSet.getString("spouseID"));

                familyList.add(person);
            }

            Person[] familyListArray = new Person[familyList.size()];
            familyList.toArray(familyListArray);
            return familyListArray;
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered querying Person table for all persons");
        }
    }

    /**
     * Clears all persons in the table.
     */
    public void clear () throws DataAccessException{
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing Person table");
        }
    }

    public void clearForUsername (String username) throws DataAccessException{
        String sql = "DELETE FROM Person WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing all persons " +
                    "associated with the specified user");
        }
    }

}
