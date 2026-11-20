package com.newgen.employeemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class EmployeeRestExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<EmployeeErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException e) {

        return answer(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(EmployeeBadRequestException.class)
    public ResponseEntity<EmployeeErrorResponse> handleEmployeeBadRequestException(EmployeeBadRequestException e) {
        return answer(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<EmployeeErrorResponse> answer(HttpStatus httpStatus, String message) {

        EmployeeErrorResponse error = new EmployeeErrorResponse();
        error.setStatus(httpStatus.value());
        error.setMessage(message);
        error.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(error, httpStatus);
    }


}
