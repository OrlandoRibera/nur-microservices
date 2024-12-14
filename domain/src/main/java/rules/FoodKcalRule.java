package rules;

import core.BusinessRule;

public class FoodKcalRule implements BusinessRule {
  private final float kcal;
  private static final float MIN_KCAL = 0;

  public FoodKcalRule(float kcal) {
    this.kcal = kcal;
  }


  @Override
  public boolean isValid() {
    return this.kcal > MIN_KCAL;
  }

  @Override
  public String getMessage() {
    return "The kcal can not be lower than " + MIN_KCAL;
  }
}
