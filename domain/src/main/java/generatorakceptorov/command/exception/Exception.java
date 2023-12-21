package generatorakceptorov.command.exception;

import generatorakceptorov.command.error.domain.Error;

public class Exception extends RuntimeException {

  private final Error error;

  public Exception(String message, Error error) {
    super(message, error.getCause());
    this.error = error;
  }

  public Error getError() {
    return error;
  }
}
