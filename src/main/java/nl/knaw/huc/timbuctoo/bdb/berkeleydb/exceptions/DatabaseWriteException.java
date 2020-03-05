package nl.knaw.huc.timbuctoo.bdb.berkeleydb.exceptions;

public class DatabaseWriteException extends Exception {
  public DatabaseWriteException(Exception cause) {
    super(cause);
  }
}
