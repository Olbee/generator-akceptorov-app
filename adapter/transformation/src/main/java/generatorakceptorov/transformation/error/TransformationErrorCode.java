package generatorakceptorov.transformation.error;

import static generatorakceptorov.command.error.domain.ErrorCodeType.INTERNAL;

import generatorakceptorov.command.error.domain.ErrorCode;
import generatorakceptorov.command.error.domain.ErrorCodeType;

public enum TransformationErrorCode implements ErrorCode {
  DOT_TO_PNG_CONVERT_ERROR(
      INTERNAL,
      "An unexpected error occurred while attempting to convert the minimal deterministic automaton from DOT to PNG format. Cause exception message: %s");

  private final ErrorCodeType type;
  private final String template;

  TransformationErrorCode(ErrorCodeType type, String template) {
    this.type = type;
    this.template = template;
  }

  @Override
  public String template() {
    return this.template;
  }

  @Override
  public ErrorCodeType type() {
    return this.type;
  }
}
