package com.dev.emsapispring.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class CustomApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String message;

}
