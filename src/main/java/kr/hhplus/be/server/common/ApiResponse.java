package kr.hhplus.be.server.common;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private String message;
    private int code;
    private T data;
}
