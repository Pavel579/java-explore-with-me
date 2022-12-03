package ru.practicum.ewm.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private final ErrorApi errorApi = new ErrorApi();

    @ExceptionHandler
    public ResponseEntity<String> handleOtherException(final Throwable e) {
        log.debug("OtherException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorApi> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.debug("MethodArgumentNotValidException - log");
        errorApi.setErrors(e.getStackTrace());
        errorApi.setReason("For the requested operation the conditions are not met.");
        errorApi.setMessage(e.getMessage());
        errorApi.setStatus("BAD_REQUEST");
        errorApi.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorApi, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundException(final NotFoundException e) {
        log.debug("NotFoundException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleForbiddenException(final ForbiddenException e) {
        log.debug("ForbiddenException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleValidationException(final ValidationException e) {
        log.debug("ValidationException");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorApi> handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.debug("DataIntegrityViolationException");
        errorApi.setErrors(e.getStackTrace());
        errorApi.setReason("For the requested operation the conditions are not met.");
        errorApi.setMessage(e.getMessage());
        errorApi.setStatus("CONFLICT");
        errorApi.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorApi, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorApi> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        log.debug("MissingServletRequestParameterException");
        errorApi.setErrors(e.getStackTrace());
        errorApi.setReason("For the requested operation the conditions are not met.");
        errorApi.setMessage(e.getMessage());
        errorApi.setStatus("BAD_REQUEST");
        errorApi.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorApi, HttpStatus.BAD_REQUEST);
    }
}
