package com.example.blog.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceException extends RuntimeException {

    private Integer code;

    private static final long serialVersionUID = 1L;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    };

    public ServiceException(Integer code, String message, String logString) {
        super(message);
        log.warn(logString + ": " + message);
        this.code = code;
    };

}
