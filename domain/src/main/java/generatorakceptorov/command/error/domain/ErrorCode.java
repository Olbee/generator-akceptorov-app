package generatorakceptorov.command.error.domain;

public interface ErrorCode {

  String template();

  ErrorCodeType type();

  default Error createError(String... params) {
    return Error.of(this, params);
  }
}
