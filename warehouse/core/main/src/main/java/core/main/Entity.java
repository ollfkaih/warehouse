package core.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Base class for all objects with id.
 */
public abstract class Entity<T extends Entity<T>> {
  /**
   * Functional interface for doing multiple changes to item, without notifying multiple times.  
   */
  @FunctionalInterface
  public interface BatchChanger {
    public void doChanges();
  }

  private final String id;
  private final Collection<EntityListener<T>> listeners = new ArrayList<>();
  private boolean notifyChanges = true;

  protected Entity(String id) {
    this.id = id;
  }

  protected Entity() {
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  public void doBatchChanges(BatchChanger changeFunction) {
    notifyChanges = false;
    changeFunction.doChanges();
    notifyChanges = true;
    notifyUpdated();
  }

  protected abstract T getThis(); 

  protected void notifyUpdated() {
    if (notifyChanges) {
      for (EntityListener<T> listener : listeners) {
        listener.updated(getThis());
      }
    }
  }

  public void addListener(EntityListener<T> listener) {
    listeners.add(listener);
  }

  public void removeListener(EntityListener<T> listener) {
    listeners.remove(listener);
  }
}
