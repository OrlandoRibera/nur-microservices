package dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error Response Data Transfer Object")
public record ErrorResponse(
    @Schema(description = "Timestamp when the error occurred", example = "2024-01-15T10:30:00")
    LocalDateTime timestamp,
    
    @Schema(description = "HTTP status code", example = "400")
    int status,
    
    @Schema(description = "Error type/category", example = "Validation Error")
    String error,
    
    @Schema(description = "Detailed error message", example = "Input validation failed")
    String message,
    
    @Schema(description = "Request path where the error occurred", example = "/api/catering/packages")
    String path
) {
}
