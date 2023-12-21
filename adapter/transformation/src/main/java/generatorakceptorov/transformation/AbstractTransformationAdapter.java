package generatorakceptorov.transformation;

//TODO: needs to be reworked
public class AbstractTransformationAdapter {

    public final static char LEFT_PARENTHESIS = '(';
    protected final static char RIGHT_PARENTHESIS = ')';
    protected final static char LEFT_BRACKET = '[';
    protected final static char RIGHT_BRACKET = ']';
    protected final static char LEFT_CURLY_BRACE = '{';
    protected final static char RIGHT_CURLY_BRACE = '}';

    protected final static char CONCATENATION = '.';
    protected final static char OR = '|';
    protected final static char STAR = '*';
    protected final static char PLUS = '+';
    protected final static char QUESTION = '?';

    protected final static int START_STATE = 0;

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