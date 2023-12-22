package generatorakceptorov.command.error.domain;

public enum ErrorCodeType {
  BAD_REQUEST(400),
  INTERNAL(500),
  UNPROCESSABLE_ENTITY(422);

  private final int codeNumber;

  ErrorCodeType(int codeNumber) {
    this.codeNumber = codeNumber;
  }

  public int getCodeNumber() {
    return codeNumber;
  }
}
