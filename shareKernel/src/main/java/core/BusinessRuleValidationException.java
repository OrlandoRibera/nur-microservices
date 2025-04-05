package core;

public class BusinessRuleValidationException extends Exception {

	private BusinessRule brokenRule;
	private String message;

	public BusinessRuleValidationException(final BusinessRule brokenRule) {
		this.brokenRule = brokenRule;
		this.message = brokenRule.getMessage();
	}

	public BusinessRuleValidationException(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String toString() {
		return "BusinessRule " + this.message;
	}
}
