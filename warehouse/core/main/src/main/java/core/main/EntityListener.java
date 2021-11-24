package core.main;

/**
 * Interface for listeners that listen for changes to an Entity.
 */
public interface EntityListener<T extends Entity<T>> {
  /**
   * Notifies that there has been a change to the Entity.
   */
  public void updated(T entity);
}
