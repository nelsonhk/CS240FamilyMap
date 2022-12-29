package Service;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDao;
import Model.AuthToken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

import java.util.Objects;
import java.util.UUID;

/**
 * Service class for logging user in.
 */
public class Login {

    /**
     * Logs in the user and returns an auth token.
     *
     * @param r LoginRequest object contains login information.
     * @return LoginResult indicates failure or success.
     */
    public LoginResult login (LoginRequest r) {

        //TODO: Each new login request must generate and return a new authtoken.
        // It must also be possible for the same user to be logged in from
        // multiple clients at the same time, which means that each client will have
        // a different authtoken but each authtoken will be associated with the same user.

        boolean success = false;
        Database database = new Database();
        LoginResult loginResult = new LoginResult();
        String message = "Error: ";

        try {

            database.openConnection();

            // Find the user in the DB using username from LoginRequest
            UserDao userDao = new UserDao(database.getConnection());
            User user = userDao.findUserByUsername(r.getUsername());

            // If the user is valid and the password is correct, create a new AuthToken, save to the DB,
            // and create successful loginResult object
            if (user != null && Objects.equals(user.getPassword(), r.getPassword())) {
                UUID authTokenID = UUID.randomUUID();
                AuthToken newAuthToken = new AuthToken(authTokenID.toString(), r.getUsername());

                //store the AuthToken in the DB?
                AuthTokenDao authTokenDao = new AuthTokenDao(database.getConnection());
                authTokenDao.createAuthToken(newAuthToken);

                loginResult = new LoginResult(
                        newAuthToken.getAuthtoken(),
                        r.getUsername(),
                        user.getPersonID(),
                        true
                        );

                success = true;

            } // Either the user does not exist or the password is incorrect
            else {
                message += "User credentials invalid.";
            }

            database.closeConnection(true);

        } catch (DataAccessException e) {
            System.out.println(e);
            e.printStackTrace();
            message += "Server error prevented user from logging in.";
            try {
                database.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
        }

        // If we weren't successful in all operations, construct an unsuccessful loginResult object
        if (!success) {
            loginResult = new LoginResult(
                    false,
                    message
            );
        }

        return loginResult;
    }

}
