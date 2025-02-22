package valueobjects;

import core.BusinessRuleValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodKcalValueTest {

  @Test
  void testGetKcal() throws BusinessRuleValidationException {
    // Arrange
    float expectedKcal = 250.5f;
    FoodKcalValue foodKcalValue = new FoodKcalValue(expectedKcal);

    // Act
    float result = foodKcalValue.getKcal();

    // Assert
    assertEquals(expectedKcal, result);
  }
}