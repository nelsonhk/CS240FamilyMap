package Result;

public class LoadResult {
    /**
     * message for successful and unsuccessful load
     */
    private String message;

    /**
     * true if successful, false if unsuccessful
     */
    private boolean success;

    /**
     * Constructor may be used for both successful and unsuccessful loads
     * @param message
     * @param success
     */
    public LoadResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public LoadResult() {

    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
