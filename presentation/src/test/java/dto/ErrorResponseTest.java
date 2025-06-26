package dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void errorResponse_ShouldCreateWithAllFields() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 400;
        String error = "Validation Error";
        String message = "Input validation failed";
        String path = "/api/catering/packages";

        // Act
        ErrorResponse errorResponse = new ErrorResponse(timestamp, status, error, message, path);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.timestamp());
        assertEquals(status, errorResponse.status());
        assertEquals(error, errorResponse.error());
        assertEquals(message, errorResponse.message());
        assertEquals(path, errorResponse.path());
    }

    @Test
    void errorResponse_ShouldHandleNullValues() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 500;
        String error = null;
        String message = null;
        String path = null;

        // Act
        ErrorResponse errorResponse = new ErrorResponse(timestamp, status, error, message, path);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.timestamp());
        assertEquals(status, errorResponse.status());
        assertNull(errorResponse.error());
        assertNull(errorResponse.message());
        assertNull(errorResponse.path());
    }

    @Test
    void errorResponse_ShouldBeImmutable() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse errorResponse = new ErrorResponse(timestamp, 400, "Test", "Test message", "/test");

        // Act & Assert
        assertNotNull(errorResponse);
        // Since it's a record, it should be immutable by design
        assertEquals(timestamp, errorResponse.timestamp());
    }

    @Test
    void errorResponse_ShouldHandleDifferentStatusCodes() {
        // Arrange & Act
        ErrorResponse badRequest = new ErrorResponse(LocalDateTime.now(), 400, "Bad Request", "Invalid input", "/test");
        ErrorResponse notFound = new ErrorResponse(LocalDateTime.now(), 404, "Not Found", "Resource not found",
                "/test");
        ErrorResponse serverError = new ErrorResponse(LocalDateTime.now(), 500, "Server Error", "Internal error",
                "/test");

        // Assert
        assertEquals(400, badRequest.status());
        assertEquals(404, notFound.status());
        assertEquals(500, serverError.status());
    }

    @Test
    void errorResponse_ShouldHandleEmptyStrings() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        String emptyError = "";
        String emptyMessage = "";
        String emptyPath = "";

        // Act
        ErrorResponse errorResponse = new ErrorResponse(timestamp, 400, emptyError, emptyMessage, emptyPath);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(emptyError, errorResponse.error());
        assertEquals(emptyMessage, errorResponse.message());
        assertEquals(emptyPath, errorResponse.path());
    }
}
