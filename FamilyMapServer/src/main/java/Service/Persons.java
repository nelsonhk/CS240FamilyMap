package Service;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.AuthToken;
import Model.Person;
import Result.PersonIDResult;
import Result.PersonsResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for returning all family members of current user.
 */
public class Persons {

    /**
     * Returns ALL family members of the current user.
     * The current user is determined from the provided auth token.
     * @return PersonsResult object
     */
    public PersonsResult persons (String authToken) {

        boolean success = false;
        String message = "Error: ";
        Database database = new Database();
        PersonsResult result = new PersonsResult();

        try {

            database.openConnection();

            // Verify the AuthToken is valid
            AuthTokenDao authTokenDao = new AuthTokenDao(database.getConnection());
            String username = authTokenDao.getUsername(authToken);

            if (username != null) {

                // Get array of persons associated with the authenticated user
                PersonDao personDao = new PersonDao(database.getConnection());
                Person[] familyList = personDao.findFamilyByID(username);

                // Successful result
                result = new PersonsResult(
                        familyList,
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
            result = new PersonsResult(
                    message,
                    false
            );
        }

        return result;

    }

}
