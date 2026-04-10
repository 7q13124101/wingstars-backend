package com.wingstars.core.common;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
    private boolean success;
    private int errorCode;
    private Object error;
    private T data;

    public ApiResponse (boolean success, int errorCode, Object error, T data) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
        this.error = error;
    }
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, HttpStatus.OK.value(), null, data);
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, HttpStatus.OK.value(), message, data);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), message, null);
    }

    public static <T> ApiResponse<T> badRequest(String message){
        return new ApiResponse<>(false, HttpStatus.BAD_REQUEST.value(), message, null);
    }

    public static <T> ApiResponse<T> error (String message){
        return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }
}
