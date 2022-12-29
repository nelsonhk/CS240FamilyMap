package Result;

public class ClearResult {

    /**
     * message for successful and unsuccessful clear
     */
    private String message;

    /**
     * true if successful, false if unsuccessful
     */
    private boolean success;

    /**
     * Constructor may be used for both successful and unsuccessful clears
     * @param message
     * @param success
     */
    public ClearResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

}
