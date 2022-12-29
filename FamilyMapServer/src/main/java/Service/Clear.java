package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Result.ClearResult;

/**
 * Service class for clearing the database.
 */
public class Clear {

    /**
     * Takes no parameters, clears all the data from the database
     * @return ClearResult object containing message and whether operation was successful
     */
    public ClearResult clear () throws DataAccessException {

        boolean success = false;

        Database database = new Database();
        try {
            database.openConnection();
            database.clearTables();
            database.closeConnection(true);
            success = true;
        } catch (DataAccessException e) {
            System.out.println(e);
            e.printStackTrace();
        }

        ClearResult clearResult;
        if (success) {
            clearResult = new ClearResult("Clear succeeded", true);
        } else {
            clearResult = new ClearResult("Error: Failed to clear all tables", false);
        }

        return clearResult;
    }
}
