package data;

import java.io.IOException;
import java.util.Collection;

/**
 * Interface for persisting data. 
 */
public interface DataPersistence<T> {
  /**
   * Get all the saved objects.
   *
   * @return The object
   * @throws IOException if the saved object cannot be loaded
   */
  Collection<T> loadAll() throws IOException;

  /**
   * Get a the saved object.
   *
   * @param key The key that the object was saved with
   * @return The object
   * @throws IOException if the saved object cannot be loaded
   */
  T load(String key) throws IOException;

  /**
   * Save the object with the given key.
   *
   * @param object Object to be saved
   * @param key Unique object key, used for loading the object
   * @throws IOException If the object can't be saved
   */
  void save(T object, String key) throws IOException;

  /**
   * Deletes the object saved with the given key.

   * @param key The key to delete
   * @throws IOException If object can't be deleted
   */
  void delete(String key) throws IOException;

  /**
   * Deletes all objects.

   * @throws IOException If objects can't be deleted
   */
  void deleteAll() throws IOException;
}
