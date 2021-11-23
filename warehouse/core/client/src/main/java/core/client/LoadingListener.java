package core.client;

/**
 * Interface for observing when something is loading.
 */
public interface LoadingListener {
  public void startedLoading();
  
  public void stoppedLoading();
}
