package Service;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.ClearResult;
import Result.LoadResult;
import Result.LoginResult;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service class for clearing database and loading current data.
 */
public class Load {

    public LoadResult load (LoadRequest r) {

        boolean success = false;
        Database database = new Database();
        LoadResult loadResult = new LoadResult();
        String message = "";

        try {

            database.openConnection();

            // Clears all data from the database (just like the /clear API)
            Clear clear = new Clear();
            ClearResult clearResult = clear.clear();
            assert clearResult.isSuccess();

            // Gets the users, persons, and events arrays from the request body
            User[] usersToLoad = r.getUsers();
            Person[] personsToLoad = r.getPersons();
            Event[] eventsToLoad = r.getEvents();

            UserDao userDao = new UserDao(database.getConnection());
            PersonDao personDao = new PersonDao(database.getConnection());
            EventDao eventDao = new EventDao(database.getConnection());

            //Loads the user, person, and event data from the request body into the database.
            for (User user : usersToLoad) {
                userDao.createUser(user);
            }

            for (Person person : personsToLoad) {
                personDao.createPerson(person);
            }

            for (Event event : eventsToLoad) {
                eventDao.createEvent(event);
            }

            message = "Successfully added " + usersToLoad.length + " users, " +
                    personsToLoad.length + " persons, and " + eventsToLoad.length +
                    " events into the database.";

            // Successful result
            loadResult = new LoadResult(
                     message,
                    true
            );

            success = true;

            database.closeConnection(true);

        } catch (DataAccessException e) {
            System.out.println(e);
            e.printStackTrace();
            message = "Error: Server error prevented user from loading data.";
            try {
                database.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        // Unsuccessful result
        if (!success) {
            loadResult = new LoadResult(
                    message,
                    false
            );
        }

        return loadResult;

    }

}
