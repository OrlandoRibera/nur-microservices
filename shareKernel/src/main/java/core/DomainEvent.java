package core;

import java.util.Date;
import java.util.UUID;

public abstract class DomainEvent {

  private Date ocurredOn;
  private UUID id;

  protected DomainEvent(Date ocurredOn) {
    this.id = UUID.randomUUID();
    this.ocurredOn = ocurredOn;
  }
}
