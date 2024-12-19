package dto;

public record ErrorResponse(int status, String message, String timestamp) {
}
