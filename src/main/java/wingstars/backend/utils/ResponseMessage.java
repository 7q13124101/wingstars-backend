package wingstars.backend.utils;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseMessage<T> {
    private String message;
    private HttpStatus status;
    private T data;

    public ResponseMessage(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public ResponseMessage(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }
}
