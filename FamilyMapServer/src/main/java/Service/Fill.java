package Service;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Result.FillResult;

import java.sql.Connection;
import java.util.UUID;

/**
 * Service class for filling database for specified username.
 */
public class Fill extends BaseService {

    /**
     * Fills the family tree for a specified username with numGenerations of family members (generations
     * may be selected by user or default of 4, this is handled in Handler class)
     * @param username
     * @param numGenerations
     * @return FillResult object with message and indication of function success
     */
    public FillResult fill (String username, int numGenerations) throws DataAccessException {
        boolean success = false;
        Database database = new Database();
        String errorMessage = new String();
        try {
            if (checkUsername(username)) {
                database.openConnection();

                //clear all existing data for the user (just the persons/events associated)
                EventDao eventDao = new EventDao(database.getConnection());
                eventDao.clearForUsername(username);

                PersonDao personDao = new PersonDao(database.getConnection());
                personDao.clearForUsername(username);

                //fill with numGenerations of people
                GenerateFamilyTree generateFamilyTree = new GenerateFamilyTree();

                //Get the user for that username
                UserDao userDao = new UserDao(database.getConnection());
                Model.User userToFill = userDao.findUserByUsername(username);

                //Create the person, add to database
                //motherID, fatherID missing, will be added during fill
                //spouseID will remain null
                UUID personID = UUID.randomUUID();
                Person newPerson = new Person(
                        personID.toString(),
                        username,
                        userToFill.getFirstName(),
                        userToFill.getLastName(),
                        userToFill.getGender(),
                        null,
                        null,
                        null);

                //Create Person's birth event (family tree will be based off of curr user's birth year)
                Register register = new Register();
                int personBirthYear = 1998;
                Event personBirthEvent = register.createBirthEvent(generateFamilyTree, newPerson, personBirthYear);
                eventDao = new EventDao(database.getConnection());
                eventDao.createEvent(personBirthEvent);

                //close the connection before opening another connection in generateFamilyTree
                database.closeConnection(true);

                generateFamilyTree.generateFamilyTree(newPerson, numGenerations, personBirthYear);

                success = true;
            } else {
                errorMessage = "Could not find username";
            }
        } catch (DataAccessException e) {
            System.out.println(e);
            e.printStackTrace();
            database.closeConnection(false);
        }

        FillResult fillResult;
        if (success) {
            int minimumPeople = (int) Math.pow(2, numGenerations + 1) - 1;
            int minEvents = minimumPeople * 2;
            fillResult = new FillResult("Successfully added " + minimumPeople +
                    " persons and " + minEvents + " events to the database.", true);
        } else {
            fillResult = new FillResult(errorMessage, false);
        }
        return fillResult;
    }
}
