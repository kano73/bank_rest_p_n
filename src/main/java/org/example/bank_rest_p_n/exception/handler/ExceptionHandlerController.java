package org.example.bank_rest_p_n.exception.handler;

import jakarta.persistence.OptimisticLockException;
import org.example.bank_rest_p_n.exception.IllegalOperation;
import org.example.bank_rest_p_n.exception.NoDataFoundException;
import org.example.bank_rest_p_n.exception.ValidationFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String optimisticLockException(OptimisticLockException exception) {
        return "Please try again, transaction error";
    }

    @ExceptionHandler(ValidationFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String validationFailedException(ValidationFailedException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(NoDataFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String noDataFoundException(NoDataFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(IllegalOperation.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String illegalOperation(IllegalOperation exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(BadJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleJwtError() {
        return "Please login";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return "404 not found";
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String runtime(RuntimeException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String runtime(MethodArgumentNotValidException exception) {
        String message = exception.getMessage();
        if (message.isEmpty()) {
            message = "Validation error";
        }
        return message;
    }
}