package wingstars.backend.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.jsonwebtoken.lang.Strings;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    @JsonIgnore
    CommonMessage commonMessage;
    long code;
    String message;
    T data;

    public BaseResponse() {
        this.commonMessage = CommonMessage.SUCCESS;
        this.code = this.commonMessage.code;
        this.message = this.commonMessage.message;
    }

    public BaseResponse(T data) {
        this();
        this.data = data;
    }

    /**
     * Freely pass response code & message without conforming to CommonMessage enum
     */
    public BaseResponse(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(CommonMessage commonMessage) {
        this.commonMessage = commonMessage;
        this.code = commonMessage.code;
        this.message = commonMessage.message;
    }

    public BaseResponse(CommonMessage commonMessage, String replaceStr) {
        this.commonMessage = commonMessage;
        this.code = commonMessage.code;
        this.message = Strings.replace(commonMessage.message, "{}", replaceStr);
    }

    public BaseResponse(CommonMessage commonMessage, T data) {
        this.commonMessage = commonMessage;
        this.code = commonMessage.code;
        this.message = commonMessage.message;
        this.data = data;
    }

    public static <T> BaseResponse<T> succeeded(T data) {
        return new BaseResponse<T>(data);
    }
    public static <T> BaseResponse<T> failed(T data) {
        return new BaseResponse<T>(CommonMessage.FAILED, data);
    }
}
