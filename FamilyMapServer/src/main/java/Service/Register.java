package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import ProvidedJSONObjects.Location;
import Request.RegisterRequest;
import Result.RegisterResult;

import java.util.UUID;

/**
 * Service class for registering a user.
 */
public class Register extends BaseService {

    /**
     * Creates a new user account, generates 4 generations of ancestor data
     * for the new user, logs the user in, and returns an auth token.
     *
     * @param r takes in RegisterRequest object
     * @return returns RegisterResult object
     */
    public RegisterResult register (RegisterRequest r) throws DataAccessException {
        boolean success = false;
        Database database = new Database();
        String errorMessage = new String();
        RegisterResult result = new RegisterResult();
        try {
            if (!checkUsername(r.getUsername())) {
                database.openConnection();

                //Create the person, add to database
                //motherID, fatherID missing, will be added during fill
                //spouseID will remain null
                UUID personID = UUID.randomUUID();
                Person newPerson = new Person(personID.toString(), r.getUsername(), r.getFirstName(), r.getLastName(),
                        r.getGender(), null, null, null);

                //Create the user, add to database
                User newUser = new User(r.getUsername(), r.getPassword(), r.getEmailAddress(), r.getFirstName(),
                        r.getLastName(), r.getGender(), personID.toString());
                UserDao userDao = new UserDao(database.getConnection());
                userDao.createUser(newUser);

                //Create AuthToken, add to database
                UUID authTokenID = UUID.randomUUID();
                AuthToken newAuthToken = new AuthToken(authTokenID.toString(), r.getUsername());
                AuthTokenDao authTokenDao = new AuthTokenDao(database.getConnection());
                authTokenDao.createAuthToken(newAuthToken);

                //Fill Family Tree
                GenerateFamilyTree generateFamilyTree = new GenerateFamilyTree();

                //Create Person's birth event (family tree will be based off of curr user's birth year)
                Register register = new Register();
                int personBirthYear = 1998;
                Event personBirthEvent = register.createBirthEvent(generateFamilyTree, newPerson, personBirthYear);
                EventDao eventDao = new EventDao(database.getConnection());
                eventDao.createEvent(personBirthEvent);

                //close the connection before opening another connection in generateFamilyTree
                database.closeConnection(true);

                generateFamilyTree.generateFamilyTree(newPerson, 4, personBirthYear);

                success = true;

                if(success) {
                    result = new RegisterResult(newAuthToken.getAuthtoken(), newAuthToken.getUsername(),
                            newPerson.getPersonID(), true);
                }
            } else {
                errorMessage = "Error: Username already being used";
            }

        } catch (DataAccessException e) {
            System.out.println(e);
            e.printStackTrace();
            database.closeConnection(false);
        }

        if(!success) {
            result = new RegisterResult(errorMessage, false);
        }

        return result;
    }

    Event createBirthEvent(GenerateFamilyTree generateFamilyTree, Person currPerson, int birthYear) {
        String birthEventID = String.valueOf(UUID.randomUUID());
        Location birthLocation = generateFamilyTree.getRandomLocation();

        return new Event(
                birthEventID,
                currPerson.getAssociatedUsername(),
                currPerson.getPersonID(),
                birthLocation.getLatitude(),
                birthLocation.getLongitude(),
                birthLocation.getCountry(),
                birthLocation.getCity(),
                "birth",
                birthYear
        );
    }

}
