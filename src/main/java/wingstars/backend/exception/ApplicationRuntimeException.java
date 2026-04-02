package wingstars.backend.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApplicationRuntimeException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final Object data;

    public ApplicationRuntimeException(String message) {
        super(message);
        this.data = null;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApplicationRuntimeException(String message, Object data) {
        super(message);
        this.data = data;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApplicationRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.data = null;
        this.httpStatus = httpStatus;
    }

    public ApplicationRuntimeException(String message, Object data, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.data = data;
    }

    public ApplicationRuntimeException(Exception cause, Object data) {
        super(cause);
        this.data = data;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApplicationRuntimeException(String message, Throwable cause, Object data) {
        super(message, cause);
        this.data = data;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
