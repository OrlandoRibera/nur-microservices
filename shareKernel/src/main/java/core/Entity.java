package core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Entity {

  public UUID id;
  public final List<DomainEvent> domainEvents;

  protected Entity() {
    id = UUID.randomUUID();
    domainEvents = new ArrayList<>();
  }

  public void addDomainEvent(DomainEvent event) {
    domainEvents.add(event);
  }

  public void clearDomainEvents() {
    domainEvents.clear();
  }

  protected void checkRule(BusinessRule rule)
    throws BusinessRuleValidationException {
    if (rule == null) throw new IllegalArgumentException("Rule cannot be null");

    if (!rule.isValid()) throw new BusinessRuleValidationException(rule);
  }

  public UUID getId() {
    return id;
  }

  public List<DomainEvent> getDomainEvents() {
    return new ArrayList<>(domainEvents);
  }
}
