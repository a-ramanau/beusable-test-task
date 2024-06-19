package com.github.aramanau.roomoccupancymanager;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public final class ExceptionHandlingAdvice {

  @ExceptionHandler({
      MethodArgumentNotValidException.class,
      MethodArgumentTypeMismatchException.class,
      ConstraintViolationException.class,
      MissingServletRequestParameterException.class
  })
  public ResponseEntity<ErrorResponse> handleIllegalArgumentException(final Exception e) {
    log.error(e.getMessage(), e);
    return new ResponseEntity<>(new ErrorResponse(e.getMessage()), BAD_REQUEST);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ErrorResponse> handleException(final Throwable t) {
    log.error(t.getMessage(), t);
    return new ResponseEntity<>(new ErrorResponse(t.getMessage()), INTERNAL_SERVER_ERROR);
  }

}

