package ui;

/**
 * Class to store and get errors returned by the server through the REST-API.
 */
public class ServerError extends RuntimeException {
  private final int status;
  private final String message;

  public ServerError(int status, String message) {
    this.status = status;
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
