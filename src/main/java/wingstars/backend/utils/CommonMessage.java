package wingstars.backend.utils;

/**
 * A list of domain-specific status codes (along with human-readable messages)
 * to send back to user as part of a response.
 */
public enum CommonMessage {
    SUCCESS(0, "Success"),
    FAILED(-1, "Failure"),
    CANNOT_DELETE_RECORD_DUE_TO_DATA_CONSTRAINTS(1, "Cannot delete due to data constraint violation");


    public final int code;
    public final String message;

    CommonMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
