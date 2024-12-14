package valueobjects;

import core.BusinessRuleValidationException;
import core.ValueObject;
import rules.FoodKcalRule;

public class FoodKcalValue extends ValueObject {
  private final float kcal;

  public FoodKcalValue(float kcal) throws BusinessRuleValidationException {
    checkRule(new FoodKcalRule(kcal));
    this.kcal = kcal;
  }

  public float getKcal() {
    return kcal;
  }
}
