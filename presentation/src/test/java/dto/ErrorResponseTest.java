package dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

  @Test
  void testErrorResponseCreation() {
    // Arrange
    int status = 404;
    String message = "Not Found";
    String timestamp = "2025-02-23T10:15:30";

    // Act
    ErrorResponse errorResponse = new ErrorResponse(status, message, timestamp);

    // Assert
    assertNotNull(errorResponse);
    assertEquals(status, errorResponse.status());
    assertEquals(message, errorResponse.message());
    assertEquals(timestamp, errorResponse.timestamp());
  }

  @Test
  void testErrorResponseImmutability() {
    // Arrange
    int status = 500;
    String message = "Internal Server Error";
    String timestamp = "2025-02-23T12:00:00";
    ErrorResponse errorResponse = new ErrorResponse(status, message, timestamp);

    // Act
    int newStatus = errorResponse.status();
    String newMessage = errorResponse.message();
    String newTimestamp = errorResponse.timestamp();

    // Assert
    assertEquals(500, newStatus);
    assertEquals("Internal Server Error", newMessage);
    assertEquals("2025-02-23T12:00:00", newTimestamp);

    // Asegurando la inmutabilidad
    assertSame(message, errorResponse.message());
    assertSame(timestamp, errorResponse.timestamp());
  }

  @Test
  void testErrorResponseEqualsAndHashCode() {
    // Arrange
    ErrorResponse errorResponse1 = new ErrorResponse(400, "Bad Request", "2025-02-23T09:00:00");
    ErrorResponse errorResponse2 = new ErrorResponse(400, "Bad Request", "2025-02-23T09:00:00");
    ErrorResponse errorResponse3 = new ErrorResponse(401, "Unauthorized", "2025-02-23T09:01:00");

    // Assert Equals y HashCode
    assertEquals(errorResponse1, errorResponse2);
    assertEquals(errorResponse1.hashCode(), errorResponse2.hashCode());
    assertNotEquals(errorResponse1, errorResponse3);
  }

  @Test
  void testErrorResponseToString() {
    // Arrange
    ErrorResponse errorResponse = new ErrorResponse(403, "Forbidden", "2025-02-23T14:30:00");

    // Act
    String toStringResult = errorResponse.toString();

    // Assert
    assertTrue(toStringResult.contains("status=403"));
    assertTrue(toStringResult.contains("message=Forbidden"));
    assertTrue(toStringResult.contains("timestamp=2025-02-23T14:30:00"));
  }
}
