package controllers;

import core.BusinessRuleValidationException;
import dto.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler exceptionHandler;

  @BeforeEach
  void setUp() {
    exceptionHandler = new GlobalExceptionHandler();
  }

  @Test
  void testHandleGenericException() {
    // Arrange
    Exception exception = new Exception("Invalid status");

    // Act
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

    // Assert
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
    assertEquals("Invalid status", response.getBody().message());

    // Verificando formato del timestamp
    String timestamp = response.getBody().timestamp();
    assertTrue(isValidTimestampFormat(timestamp));
  }

  @Test
  void testHandleCustomException() {
    // Arrange
    BusinessRuleValidationException exception = new BusinessRuleValidationException("Invalid transition of Food from COOKED to COOKING");

    // Act
    ResponseEntity<ErrorResponse> response = exceptionHandler.handleCustomException(exception);

    // Assert
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
    assertEquals("Invalid transition of Food from COOKED to COOKING", response.getBody().message());

    // Verificando formato del timestamp
    String timestamp = response.getBody().timestamp();
    assertTrue(isValidTimestampFormat(timestamp));
  }

  @Test
  void testTimestampFormat() {
    // Act
    String timestamp = exceptionHandler.handleGenericException(new Exception()).getBody().timestamp();

    // Assert
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime parsedTimestamp = LocalDateTime.parse(timestamp, formatter);
    assertNotNull(parsedTimestamp);
  }

  private boolean isValidTimestampFormat(String timestamp) {
    // Validando formato de timestamp: yyyy-MM-dd HH:mm:ss
    String pattern = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
    return Pattern.matches(pattern, timestamp);
  }
}
