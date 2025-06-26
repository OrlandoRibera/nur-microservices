package controllers;

import core.BusinessRuleValidationException;
import dto.ErrorResponse;
import infrastructure.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private ServletWebRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/catering/packages");
        request = new ServletWebRequest(servletRequest);
    }

    @Test
    void handleBusinessRuleValidationException_ShouldReturnBadRequest() {
        // Arrange
        BusinessRuleValidationException exception = new BusinessRuleValidationException("Invalid status transition");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleBusinessRuleValidationException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Business Rule Validation Error", response.getBody().error());
        assertEquals("Invalid status transition", response.getBody().message());
        assertEquals(400, response.getBody().status());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void handleCustomException_ShouldReturnBadRequest() {
        // Arrange
        CustomException exception = new CustomException("Custom business error");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleCustomException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Custom Business Error", response.getBody().error());
        assertEquals("Custom business error", response.getBody().message());
        assertEquals(400, response.getBody().status());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void handleIllegalArgumentException_ShouldReturnBadRequest() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument provided");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleIllegalArgumentException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid Argument", response.getBody().error());
        assertEquals("Invalid argument provided", response.getBody().message());
        assertEquals(400, response.getBody().status());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerError() {
        // Arrange
        Exception exception = new RuntimeException("Unexpected error");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleGlobalException(exception, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().error());
        assertTrue(response.getBody().message().contains("An unexpected error occurred"));
        assertEquals(500, response.getBody().status());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void errorResponse_ShouldHaveCorrectStructure() {
        // Arrange
        BusinessRuleValidationException exception = new BusinessRuleValidationException("Test error");

        // Act
        ResponseEntity<ErrorResponse> response = handler.handleBusinessRuleValidationException(exception, request);

        // Assert
        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNotNull(errorResponse.timestamp());
        assertEquals(400, errorResponse.status());
        assertEquals("Business Rule Validation Error", errorResponse.error());
        assertEquals("Test error", errorResponse.message());
        assertNotNull(errorResponse.path());
    }
} 