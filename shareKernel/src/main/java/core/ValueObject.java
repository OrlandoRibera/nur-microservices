package core;

public abstract class ValueObject {

  protected void checkRule(BusinessRule rule)
    throws BusinessRuleValidationException {
    if (rule == null) throw new IllegalArgumentException(
      "Rule can not be null"
    );

    if (!rule.isValid()) throw new BusinessRuleValidationException(rule);
  }
}
