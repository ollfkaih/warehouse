package data;

import core.Entity;
import core.EntityCollectionListener;

import java.io.IOException;

/**
 * Helper for automatically saving changes to an EntityCollection.
 *
 * @param <T> the type of entity in the auto-saved collection.
 */
public class EntityCollectionAutoPersistence<T extends Entity<T>> implements EntityCollectionListener<T> {
  private final DataPersistence<T> dataPersistence;

  public EntityCollectionAutoPersistence(DataPersistence<T> dataPersistence) {
    this.dataPersistence = dataPersistence;
  }

  @Override
  public void entityAdded(T entity) {
    save(entity);
  }

  @Override
  public void entityUpdated(T entity) {
    save(entity);
  }

  @Override
  public void entityRemoved(T entity) {
    delete(entity);
  }

  private void save(T entity) {
    try {
      dataPersistence.save(entity, entity.getId());
    } catch (IOException e) {
      System.err.println("ERROR: could not auto-save entity with id '" + entity.getId() + "'");
      e.printStackTrace();
    }
  }

  private void delete(T entity) {
    try {
      dataPersistence.delete(entity.getId());
    } catch (IOException e) {
      System.err.println("ERROR: could not auto-save deletion of entity with id '" + entity.getId() + "'");
      e.printStackTrace();
    }
  }
}
