package infrastructure.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionTest {
  @Test
  void testCustomExceptionMessage() {
    // Arrange
    String expectedMessage = "Custom exception message";

    // Act & Assert
    CustomException exception = assertThrows(CustomException.class, () -> {
      throw new CustomException(expectedMessage);
    });

    assertEquals(expectedMessage, exception.getMessage());
  }
}