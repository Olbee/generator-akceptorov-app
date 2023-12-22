package generatorakceptorov.domain.regex;

// TODO: rework
public enum RegexOperator {
  LEFT_PARENTHESIS('('),
  RIGHT_PARENTHESIS(')'),
  LEFT_BRACKET('['),
  RIGHT_BRACKET(']'),
  LEFT_CURLY_BRACE('{'),
  RIGHT_CURLY_BRACE('}'),
  CONCATENATION('.'),
  OR('|'),
  STAR('*'),
  PLUS('+'),
  QUESTION('?');

  private final char symbol;

  RegexOperator(char symbol) {
    this.symbol = symbol;
  }

  public char getValue() {
    return symbol;
  }
}
