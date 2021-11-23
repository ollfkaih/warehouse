package core.main;

/**
 * Interface for a listener used for listening to changes in an EntityCollection.
 */
public interface EntityCollectionListener<T> {
  /**
   * Notifies that an entity has been added to the collection.
   */
  public void entityAdded(T entity);

  /**
   * Notifies that there has been a change to an entity in the collection.
   */
  public void entityUpdated(T entity);

  /**
   * Notifies that an entity has been removed from the collection.
   */
  public void entityRemoved(T entity);
}
