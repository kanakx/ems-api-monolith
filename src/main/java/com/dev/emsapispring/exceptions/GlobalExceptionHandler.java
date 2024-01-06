package com.dev.emsapispring.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomApiException.class)
    public final ResponseEntity<Object> handleCustomException(CustomApiException customApiException) {
        ExceptionContent exceptionContent = ExceptionContent.builder()
                .httpStatus(customApiException.getHttpStatus())
                .message(customApiException.getMessage())
                .build();

        return ResponseEntity
                .status(customApiException.getHttpStatus())
                .body(exceptionContent);
    }

}
