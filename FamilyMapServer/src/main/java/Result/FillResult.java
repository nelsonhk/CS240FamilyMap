package Result;

public class FillResult {

    /**
     * message for successful and unsuccessful fill
     */
    private String message;

    /**
     * true if successful, false if unsuccessful
     */
    private boolean success;

    /**
     * Constructor may be used for both successful and unsuccessful fills
     * @param message
     * @param success
     */
    public FillResult(String message, boolean success) {
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
