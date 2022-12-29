package DataAccess;

import Model.User;
import java.sql.*;

/**
 * The data access class for all User related operations.
 */
public class UserDao {
    /**
     * Connection to obtain data access for users table.
     */
    private final Connection conn;

    /**
     * Constructor constructs connection based off of URL given.
     * @param conn is the URL given when constructed.
     */
    public UserDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Executes SQL and creates a user in the database.
     *
     * @param user contains all relevant user information.
     * @throws DataAccessException if unable to insert the user.
     */
    public void createUser (User user) throws DataAccessException {
        String sql = "INSERT INTO User (username, password, email, firstName, " +
                "lastName, gender, personID) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the User table");
        }
    }


    /**
     * Finds user with given username in database, returns the User object containing all user information.
     *
     * @param username string for username to search for User.
     * @return User object with given username.
     * @throws DataAccessException if unable to find the user.
     */
    public User findUserByUsername (String username) throws DataAccessException {
        User user;
        ResultSet resultSet;
        String sql = "SELECT * FROM User WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString("username"),
                        resultSet.getString("password"), resultSet.getString("email"),
                        resultSet.getString("firstName"), resultSet.getString("lastName"),
                        resultSet.getString("gender"), resultSet.getString("personID"));
                return user;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered querying User table for User by username");
        }
        return null;
    }

    /**
     * Clears the database of all users.
     * @return false if clear is not executed, return true if executed
     * @throws DataAccessException if unable to clear the users table.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM User";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing User table");
        }
    }

}
