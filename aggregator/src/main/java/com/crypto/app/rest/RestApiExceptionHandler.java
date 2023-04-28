package com.crypto.app.rest;

import com.crypto.app.exception.DataNotFoundException;
import com.crypto.app.model.response.ApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String OBJECT_NOT_FOUND_EXCEPTION = "Object is not found!";
    private static final String OTHER_ERRORS_EXCEPTION = "Server side exception!";

    @ExceptionHandler(value = DataNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundExceptions(DataNotFoundException ex) {
        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.NOT_FOUND, OBJECT_NOT_FOUND_EXCEPTION, ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleALLExceptions() {
        final ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, OTHER_ERRORS_EXCEPTION, null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return buildResponseEntity(apiExceptionResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiExceptionResponse apiExceptionResponse) {
        return new ResponseEntity<>(apiExceptionResponse, apiExceptionResponse.getStatusCode());
    }
}
