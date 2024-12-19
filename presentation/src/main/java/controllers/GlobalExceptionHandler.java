package controllers;

import core.BusinessRuleValidationException;
import dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private String getCurrentTimestamp() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.now().format(formatter);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      ex.getMessage(),
      getCurrentTimestamp()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(BusinessRuleValidationException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(BusinessRuleValidationException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      ex.getMessage(),
      getCurrentTimestamp()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
