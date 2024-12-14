package core;

public interface BusinessRule {
  boolean isValid();
  String getMessage();
}
