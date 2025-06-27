package controllers;

import infrastructure.outbox.OutboxService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OutboxHealthControllerTest {

    @Mock
    private OutboxService outboxService;

    @InjectMocks
    private OutboxHealthController outboxHealthController;

    @Test
    void testGetOutboxHealth_Success() {
        // Given
        when(outboxService.getUnprocessedMessageCount()).thenReturn(5L);

        // When
        ResponseEntity<Map<String, Object>> response = outboxHealthController.getOutboxHealth();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals(5L, response.getBody().get("unprocessedMessages"));
        assertEquals(true, response.getBody().get("healthy"));
    }

    @Test
    void testGetOutboxHealth_Unhealthy() {
        // Given
        when(outboxService.getUnprocessedMessageCount()).thenReturn(1500L);

        // When
        ResponseEntity<Map<String, Object>> response = outboxHealthController.getOutboxHealth();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals(1500L, response.getBody().get("unprocessedMessages"));
        assertEquals(false, response.getBody().get("healthy"));
    }

    @Test
    void testGetOutboxHealth_Error() {
        // Given
        when(outboxService.getUnprocessedMessageCount()).thenThrow(new RuntimeException("Database error"));

        // When
        ResponseEntity<Map<String, Object>> response = outboxHealthController.getOutboxHealth();

        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DOWN", response.getBody().get("status"));
        assertEquals("Database error", response.getBody().get("error"));
        assertEquals(false, response.getBody().get("healthy"));
    }

    @Test
    void testGetOutboxMetrics_Success() {
        // Given
        when(outboxService.getUnprocessedMessageCount()).thenReturn(10L);

        // When
        ResponseEntity<Map<String, Object>> response = outboxHealthController.getOutboxMetrics();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().get("unprocessedMessages"));
        assertNotNull(response.getBody().get("lastCheck"));
    }

    @Test
    void testGetOutboxMetrics_Error() {
        // Given
        when(outboxService.getUnprocessedMessageCount()).thenThrow(new RuntimeException("Service error"));

        // When
        ResponseEntity<Map<String, Object>> response = outboxHealthController.getOutboxMetrics();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Service error", response.getBody().get("error"));
    }
} 