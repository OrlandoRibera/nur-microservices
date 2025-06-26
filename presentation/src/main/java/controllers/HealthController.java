package controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Health check and monitoring endpoints")
public class HealthController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Health check", description = "Returns the health status of the catering service")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is healthy"),
        @ApiResponse(responseCode = "503", description = "Service is unhealthy")
    })
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("service", "catering");
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("version", "1.0.0");
        
        return ResponseEntity.ok(healthStatus);
    }

    @GetMapping(value = "/ready", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Readiness probe", description = "Checks if the service is ready to receive traffic")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is ready"),
        @ApiResponse(responseCode = "503", description = "Service is not ready")
    })
    public ResponseEntity<Map<String, Object>> readiness() {
        Map<String, Object> readinessStatus = new HashMap<>();
        readinessStatus.put("status", "READY");
        readinessStatus.put("service", "catering");
        readinessStatus.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(readinessStatus);
    }

    @GetMapping(value = "/live", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Liveness probe", description = "Checks if the service is alive and running")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service is alive"),
        @ApiResponse(responseCode = "503", description = "Service is not alive")
    })
    public ResponseEntity<Map<String, Object>> liveness() {
        Map<String, Object> livenessStatus = new HashMap<>();
        livenessStatus.put("status", "ALIVE");
        livenessStatus.put("service", "catering");
        livenessStatus.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(livenessStatus);
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Test endpoint", description = "Simple test endpoint to verify API is working")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Test successful")
    })
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> testResponse = new HashMap<>();
        testResponse.put("message", "API is working correctly");
        testResponse.put("timestamp", LocalDateTime.now());
        testResponse.put("endpoint", "/api/health/test");
        
        return ResponseEntity.ok(testResponse);
    }
} 