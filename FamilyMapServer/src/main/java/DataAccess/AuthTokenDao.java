package DataAccess;

import Model.AuthToken;

import java.sql.*;

public class AuthTokenDao {
    /**
     * Connection to obtain data access for authToken table.
     */
    private final Connection conn;

    /**
     * Constructor constructs connection based off of URL given.
     * @param conn is the URL given when constructed.
     */
    public AuthTokenDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts an AuthToken into the database
     * @param authToken AuthToken to insert into the database
     */
    public void createAuthToken (AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (authtoken, username) VALUES (?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthtoken());
            stmt.setString(2, authToken.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered creating AuthToken");
        }
    }

    /**
     * Returns associated username with the authToken.
     * @param AuthToken the authToken to search for the in the database.
     * @return String username associated with the specified authToken.
     */
    public String getUsername (String AuthToken) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs;
        String sql = "SELECT * FROM AuthToken WHERE authtoken = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, AuthToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authtoken"), rs.getString("username"));
                return authToken.getUsername();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered in getting username for AuthToken");
        }
        return null;
    }

    /**
     * Clears all the authTokens in the table.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM AuthToken";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing AuthToken table");
        }
    }
}
