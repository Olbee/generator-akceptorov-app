package generatorakceptorov.transformation;

// TODO: rework
public abstract class AbstractTransformationAdapter {

  protected final char LEFT_PARENTHESIS = '(';
  protected final char RIGHT_PARENTHESIS = ')';
  protected final char LEFT_BRACKET = '[';
  protected final char RIGHT_BRACKET = ']';
  protected final char LEFT_CURLY_BRACE = '{';
  protected final char RIGHT_CURLY_BRACE = '}';

  protected final char CONCATENATION = '.';
  protected final char OR = '|';
  protected final char STAR = '*';
  protected final char PLUS = '+';
  protected final char QUESTION = '?';

  protected final int START_STATE = 0;

  protected boolean isOperand(char currentChar) {
    return (currentChar != '('
        && currentChar != ')'
        && currentChar != '|'
        && currentChar != '.'
        && currentChar != '*'
        && currentChar != '+'
        && currentChar != '?');
  }
}
