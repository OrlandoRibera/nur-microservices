package infrastructure.model;

public class CustomException extends RuntimeException {
  public CustomException(String message) {
    super(message);
  }
}
