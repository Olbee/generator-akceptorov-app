package generatorakceptorov.command.error.domain;

import generatorakceptorov.command.exception.Exception;
import java.util.Arrays;
import java.util.List;
import org.immutables.value.Value;

@Value.Immutable
public abstract class Error extends Throwable {

  @Value.Parameter
  public abstract ErrorCode code();

  @Value.Parameter
  public abstract List<String> params();

  @Value.Derived
  public String message() {
    return String.format(this.code().template(), this.params().toArray());
  }

  public static Error of(ErrorCode errorCode, String... params) {
    return ImmutableError.of(errorCode, Arrays.asList(params));
  }

  public Exception convertToException() {
    return new Exception(message(), this);
  }

  @Override
  public String getMessage() {
    return message();
  }

  @Override
  public String toString() {
    return message();
  }
}
