package DataAccess;

import Model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDao {
    /**
     * Connection to obtain data access for events table.
     */
    private final Connection conn;

    /**
     * Constructor constructs connection based off of URL given.
     * @param conn is the URL given when constructed.
     */
    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts new Event into the events table.
     * @param event Event object to be inserted.
     * @throws DataAccessException if unable to insert the event.
     */
    public void createEvent (Event event) throws DataAccessException {
        String sql = "INSERT INTO Event (eventID, username, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5,  event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the Event table");
        }
    }

    /**
     * Finds a single event by the eventID and returns the Event object.
     * @param eventID the id to find the event object in the table.
     * @return Event object with given eventID.
     * @throws DataAccessException if unable to find the event.
     */
    public Event findEventByID (String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("username"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Lists all the events for all the family members of the current user.
     *
     * @param username of current user, determined by the authToken.
     * @return list of all events for all family members of current user.
     * @throws DataAccessException if unable to retrieve the data.
     */
    public Event[] getEventsForUser (String username) throws DataAccessException {
        List<Event> eventList = new ArrayList<>();
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE username = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("username"),
                        rs.getString("personID"), rs.getFloat("latitude"),
                        rs.getFloat("longitude"), rs.getString("country"),
                        rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                eventList.add(event);
            }
            Event[] eventListArray = new Event[eventList.size()];
            eventList.toArray(eventListArray);
            return eventListArray;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered obtaining list of events for user");
        }
    }

    /**
     * Clears all events in the table.
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Event";
            char c = 'c';
            Character character = c;
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing Event table");
        }
    }

    public void clearForUsername (String username) throws DataAccessException{
        String sql = "DELETE FROM Event WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing all events for specified user");
        }
    }

}
