package com.member.task.exceptions;

import com.member.task.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DateOfBirthFormatNotValidException.class})
    public ResponseEntity<ApiError> handleConflict(DateOfBirthFormatNotValidException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(value = {MemberKeyAlreadyAvalilableException.class})
    public ResponseEntity<ApiError> handleConflict(MemberKeyAlreadyAvalilableException ex) {
        ApiError apiError = new ApiError(HttpStatus.ALREADY_REPORTED, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(value = {MemberNotAvailableException.class})
    public ResponseEntity<ApiError> handleConflict(MemberNotAvailableException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
