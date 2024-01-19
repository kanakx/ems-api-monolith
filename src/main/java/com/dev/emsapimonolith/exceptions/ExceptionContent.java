package com.dev.emsapimonolith.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ExceptionContent {

    private final HttpStatus httpStatus;
    private final String message;

    public ExceptionContent(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
