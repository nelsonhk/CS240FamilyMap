package Service;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDao;
import Model.Person;
import Result.PersonIDResult;

/**
 * Service class for find single person object.
 */
public class PersonID {

    /**
     * Either finds person object with specified id or returns unsuccessful
     * @return PersonIDResult object
     */
    public PersonIDResult person (String personID, String authToken) {

        boolean success = false;
        String message = "Error: ";
        Database database = new Database();
        PersonIDResult result = new PersonIDResult();

        try {
            database.openConnection();

            // Find the person based on personID from PersonIDRequest
            PersonDao personDao = new PersonDao(database.getConnection());
            Person person = personDao.findByID(personID);

            // Find the username associated with the passed in AuthToken
            AuthTokenDao authTokenDao = new AuthTokenDao(database.getConnection());
            String username = authTokenDao.getUsername(authToken);

            // If the requested person exists and the AuthToken is valid, create new PersonIDResult
            if (person != null) {
                if (person.getAssociatedUsername().equals(username)) {
                    result = new PersonIDResult(
                            person.getAssociatedUsername(),
                            person.getPersonID(),
                            person.getFirstName(),
                            person.getLastName(),
                            person.getGender(),
                            person.getFatherID(),
                            person.getMotherID(),
                            person.getSpouseID(),
                            true
                    );

                    success = true;

                } // AuthToken is invalid
                else {
                    message += "Requested person does not belong to this user";
                }
            } // Person with requested id does not exist
            else {
                message += "Invalid PersonID. Person does not exist.";
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

        // If unable to complete all operations, create unsuccessful PersonIDResult object
        if (!success) {
            result = new PersonIDResult(
                    message,
                    false
            );
        }

        return result;

    }

}
