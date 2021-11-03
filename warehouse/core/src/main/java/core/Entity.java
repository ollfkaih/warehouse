package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

/**
 * Base class for all objects with id.
 */
public abstract class Entity<T extends Entity<T>> {
  private final String id;
  private final Collection<EntityListener<T>> listeners = new ArrayList<>();

  public Entity(String id) {
    this.id = id;
  }

  public Entity() {
    this.id = UUID.randomUUID().toString();
  }

  public String getId() {
    return id;
  }

  protected abstract T getThis(); 

  protected void notifyUpdated() {
    for (EntityListener<T> listener : listeners) {
      listener.updated(getThis());
    }
  }

  public void addListener(EntityListener<T> listener) {
    listeners.add(listener);
  }

  public void removeListener(EntityListener<T> listener) {
    listeners.remove(listener);
  }
}
