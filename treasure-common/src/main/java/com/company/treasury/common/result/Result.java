package com.company.treasury.common.result;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Result<T> implements Serializable {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    protected Result() {
        this.timestamp = LocalDateTime.now();
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.message = "success";
        r.data = data;
        return r;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }

    public static <T> Result<T> error(String message) {
        return error(500, message);
    }
}
