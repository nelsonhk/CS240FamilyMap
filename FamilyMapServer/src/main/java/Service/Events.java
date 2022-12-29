package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Result.EventsResult;
import Result.PersonsResult;

import java.util.List;

/**
 * Service class for returning all events for all family members of the current user.
 */
public class Events {

    /**
     * Returns all events for all family members of the current user.
     * @return EventsResult object
     */
    public EventsResult events (String authToken) {

        boolean success = false;
        String message = "Error: ";
        Database database = new Database();
        EventsResult result = new EventsResult();

        try {

            database.openConnection();

            // Verify the AuthToken is valid
            AuthTokenDao authTokenDao = new AuthTokenDao(database.getConnection());
            String username = authTokenDao.getUsername(authToken);

            if (username != null) {

                // Get the array of events associated with the authenticated user
                EventDao eventDao = new EventDao(database.getConnection());
                Event[] eventList = eventDao.getEventsForUser(username);

                // Successful result
                result = new EventsResult(
                        eventList,
                        true
                );
                success = true;

            } else {
                message += "Invalid AuthToken.";
            }

            database.closeConnection(true);

        } catch (DataAccessException e) {
            System.out.println(e);
            e.printStackTrace();
            try {
                database.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        // Unsuccessful result
        if (!success) {
            result = new EventsResult(
                    message,
                    false
            );
        }

        return result;
    }

}
