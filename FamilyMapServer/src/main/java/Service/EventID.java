package Service;

import DataAccess.*;
import Model.Event;
import Result.EventIDResult;

/**
 * Service class for finding a specific event based on eventID.
 */
public class EventID {

    /**
     * Finds specific event based on eventID.
     * @param eventID
     * @return EventResult object.
     */
    public EventIDResult event (String eventID, String authToken) {

        boolean success = false;
        String message = "Error: ";
        Database database = new Database();
        EventIDResult result = new EventIDResult();

        try {

            database.openConnection();

            // Locate the requested event (if null, eventID is invalid)
            EventDao eventDao = new EventDao(database.getConnection());
            Event event = eventDao.findEventByID(eventID);

            // Locate the associatedUser (if null, AuthToken is invalid)
            AuthTokenDao authTokenDao = new AuthTokenDao(database.getConnection());
            String username = authTokenDao.getUsername(authToken);

            // Verify requested eventID
            if (event != null) {
                // Verify AuthToken
                if (event.getUsername().equals(username)) {

                    // Successful result
                    result = new EventIDResult(
                            username,
                            eventID,
                            event.getPersonID(),
                            event.getLatitude(),
                            event.getLongitude(),
                            event.getCountry(),
                            event.getCity(),
                            event.getEventType(),
                            event.getYear(),
                            true
                    );

                    success = true;

                } else {
                    message += "Requested event does not belong to this user";
                }
            } else {
                message += "Invalid EventID. Event does not exist.";
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
            result = new EventIDResult(
                    message,
                    false
            );
        }

        return result;

    }

}
