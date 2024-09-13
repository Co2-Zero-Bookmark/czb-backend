package com.carbonhater.co2zerobookmark.common.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Data
public class CustomResponseEntity<T> {
    private boolean success;
    private T response;
    private CustomError error;

    public CustomResponseEntity(boolean success, T response, CustomError error) {
        this.success = success;
        this.response = response;
        this.error = error;
    }

    public static <T> CustomResponseEntity<T> success(T response) {
        return new CustomResponseEntity<>(true, response, null);
    }

    public static <T> CustomResponseEntity<T> error(T response, HttpStatus status, String message) {
        return new CustomResponseEntity<>(false, null, new CustomError(message, status));
    }

    public Map<String, Object> to() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("response", response);
        result.put("error", error);
        return result;
    }
}