package generatorakceptorov.transformation.regex;

import generatorakceptorov.domain.regex.NotationType;
import generatorakceptorov.domain.regex.entity.ImmutableRegexEntity;
import generatorakceptorov.domain.regex.entity.RegexEntity;
import generatorakceptorov.regex.port.outbound.StringTransformationPort;
import generatorakceptorov.transformation.AbstractTransformationAdapter;
import org.springframework.stereotype.Component;

import java.util.*;

import static generatorakceptorov.domain.regex.NotationType.COMMON;
import static generatorakceptorov.domain.regex.NotationType.SIMPLE;
import static generatorakceptorov.domain.regex.error.RegexErrorCode.*;

@Component
public class StringTransformationAdapter extends AbstractTransformationAdapter implements StringTransformationPort {

    @Override
    public RegexEntity transformToRegex(String regex, NotationType notationType) {
        if (notationType == SIMPLE) {
            final String regexInCommonNotation = toCommonNotation(regex);
            if (!commonRegexIsCorrect(regexInCommonNotation) || regex.matches(".*[+*?].*")) // || regex contains * or + or ?
                throwInvalidRegexSyntaxErrorError(regex);
            return ImmutableRegexEntity.of(
                    toPostfix(addConcatenation(regexInCommonNotation)),
                    notationType);
        } else if (notationType == COMMON) {
            if (!commonRegexIsCorrect(regex))
                throwInvalidRegexSyntaxErrorError(regex);
            return ImmutableRegexEntity.of(
                    toPostfix(addConcatenation(regex)),
                    notationType);
        } else {
            throw UNEXPECTED_REGEX_NOTATION_TYPE_ERROR
                    .createError(notationType.toString())
                    .convertToException();
        }
    }

    private void throwInvalidRegexSyntaxErrorError(String regex) {
        throw INVALID_REGEX_SYNTAX_ERROR
                .createError(regex)
                .convertToException();
    }

    private String addConcatenation(String regex) {
        final StringBuilder result = new StringBuilder();
        result.append(regex.charAt(0));
        for (int i = 1; i < regex.length(); i++) {
            char leftSymbol = result.charAt(result.length() - 1);
            char rightSymbol = regex.charAt(i);
            if ((isOperand(leftSymbol) || leftSymbol == STAR || leftSymbol == QUESTION || leftSymbol == PLUS || leftSymbol == RIGHT_PARENTHESIS)
                    && (isOperand(rightSymbol) || rightSymbol == LEFT_PARENTHESIS))
                result.append(CONCATENATION);
            result.append(rightSymbol);
        }

        return result.toString();
    }

    private int getOperatorsPriority(char currentChar) {
        return switch (currentChar) {
            case LEFT_PARENTHESIS, RIGHT_PARENTHESIS -> 1;
            case OR -> 2;
            case CONCATENATION -> 3;
            case STAR, PLUS, QUESTION -> 4;
            default -> 0;
        };
    }

    private String toCommonNotation(String regex) {
        final StringBuilder result = new StringBuilder();
        final Stack<Character> stack = new Stack<>();
        for (char currentChar : regex.toCharArray()) {
            // we should not define PLUS (converting x{x} to (x)+)
            // it works fine using START instead
            switch (currentChar) {
                case LEFT_CURLY_BRACE, LEFT_BRACKET -> {
                    result.append(LEFT_PARENTHESIS);
                    stack.push(currentChar);
                }
                case RIGHT_CURLY_BRACE -> {
                    result.append(RIGHT_PARENTHESIS);
                    if (stack.peek() == LEFT_CURLY_BRACE) {
                        result.append(STAR);
                        stack.pop();
                    }
                }
                case RIGHT_BRACKET -> {
                    result.append(RIGHT_PARENTHESIS);
                    if (stack.peek() == LEFT_BRACKET) {
                        result.append(QUESTION);
                    }
                    stack.pop();
                }
                default -> result.append(currentChar);
            }
        }

        return result.toString();
    }

    private String toPostfix(String regex) {
        final StringBuilder sb = new StringBuilder();
        final Stack<Character> stack = new Stack<>();
        for (char currentChar : regex.toCharArray()) {
            if (isOperand(currentChar))
                sb.append(currentChar);
            else {
                if (currentChar == LEFT_PARENTHESIS)
                    stack.push(currentChar);
                else if (currentChar == RIGHT_PARENTHESIS) {
                    while (!(stack.peek() == LEFT_PARENTHESIS))
                        sb.append(stack.pop());
                    stack.pop();
                } else {
                    while (!stack.isEmpty() && getOperatorsPriority(stack.peek()) >= getOperatorsPriority(currentChar))
                        sb.append(stack.pop());
                    stack.push(currentChar);
                }
            }
        }

        while (!stack.isEmpty())
            sb.append(stack.pop());

        return sb.toString();
    }

    public boolean commonRegexIsCorrect(String regex) {
        final Map<Character, Integer> operatorsDepth = new HashMap<>(
                Map.of(
                        LEFT_PARENTHESIS, 0,
                        CONCATENATION, 0,
                        OR, 0,
                        STAR, 0,
                        PLUS, 0,
                        QUESTION, 0));

        for (char currentChar : regex.toCharArray()) {
            if (!symbolIsOk(currentChar))
                return false;
            switch (currentChar) {
                case OR, STAR, PLUS, QUESTION -> {
                    operatorsDepth.put(currentChar, operatorsDepth.get(currentChar) + 1);
                    if (regex.length() - 1 == currentChar || operatorsDepth.get(currentChar) > 1)
                        return false;
                }
                case LEFT_PARENTHESIS -> {
                    operatorsDepth.put(LEFT_PARENTHESIS, operatorsDepth.get(LEFT_PARENTHESIS) + 1);
                    clearOperatorsDepth(operatorsDepth, OR, STAR, PLUS, QUESTION);
                }
                case RIGHT_PARENTHESIS -> {
                    if (operatorsDepth.get(LEFT_PARENTHESIS) == 0)
                        return false;
                    operatorsDepth.put(LEFT_PARENTHESIS, operatorsDepth.get(LEFT_PARENTHESIS) - 1);
                    clearOperatorsDepth(operatorsDepth, OR, STAR, PLUS, QUESTION);
                }
                default -> clearOperatorsDepth(operatorsDepth, OR, STAR, PLUS, QUESTION);
            }
        }

        return operatorsDepth.get(LEFT_PARENTHESIS) == 0;
    }

    void clearOperatorsDepth(Map<Character, Integer> operatorsDepth, char... operators) {
        for (char operator : operators)
            operatorsDepth.put(operator, 0);
    }

    private boolean symbolIsOk(char currentChar) {
        return getOperatorsPriority(currentChar) != 0
                || Character.isDigit(currentChar)
                || Character.isLetter(currentChar)
                || Character.isAlphabetic(currentChar)
                || currentChar == ' ';
    }
}
