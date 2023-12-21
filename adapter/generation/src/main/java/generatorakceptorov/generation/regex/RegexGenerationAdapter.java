package generatorakceptorov.generation.regex;

import generatorakceptorov.domain.regex.command.RandomRegexCommand;
import generatorakceptorov.domain.regex.command.RegexNotationTypeCommand;
import generatorakceptorov.regex.port.outbound.RegexGenerationPort;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static generatorakceptorov.domain.regex.NotationType.COMMON;
import static generatorakceptorov.domain.regex.NotationType.SIMPLE;
import static generatorakceptorov.domain.regex.RegexOperator.*;
import static generatorakceptorov.domain.regex.error.RegexErrorCode.UNEXPECTED_REGEX_NOTATION_TYPE_ERROR;
import static java.lang.Character.isLetter;
import static java.lang.Character.isLetterOrDigit;

@Component
//TODO: needs to be reworked
public class RegexGenerationAdapter implements RegexGenerationPort {

    private static final Random random = new Random();

    private static final int OPERANDS_COUNT = 1;

    private static Integer symbolsCount;
    private static String alphabet, numbers;
    private static String parentheses, brackets, braces;
    private static String[] groupingSymbols;

    @Override
    public String generate(RandomRegexCommand command) {
        if (command.notationType() == null)
            throw UNEXPECTED_REGEX_NOTATION_TYPE_ERROR
                    .createError(command.notationType().toString())
                    .convertToException();

        initialize(command);

        String randomRegex = "";
        int randomRegexSymbolsCount = 0;

        if (command.notationType() == COMMON) {
            if (!command.regexNumOfSymbols().equals("") || !command.regexSymbolsToUse().equals("")) {
                while (randomRegexSymbolsCount != (Integer.parseInt(command.regexNumOfSymbols()))) {
                    randomRegexSymbolsCount = 0;
                    randomRegex = addAlternations(
                            addQuantifiers(
                                    generateRandomExpression(symbolsCount)));
                    for (int i = 0; i < randomRegex.length(); i++)
                        if (command.regexSymbolsToUse().contains(String.valueOf(randomRegex.charAt(i))))
                            randomRegexSymbolsCount++;
                }
            } else {
                return addAlternations(
                        addQuantifiers(
                                generateRandomExpression(symbolsCount)));
            }
        } else if (command.notationType() == SIMPLE) {
            if (!command.regexNumOfSymbols().equals("") || !command.regexSymbolsToUse().equals("")) {
                while (randomRegexSymbolsCount != (Integer.parseInt(command.regexNumOfSymbols()))) {
                    randomRegexSymbolsCount = 0;
                    randomRegex = addAlternations(
                            generateRandomExpression(symbolsCount));
                    for (int i = 0; i < randomRegex.length(); i++)
                        if (command.regexSymbolsToUse().contains(String.valueOf(randomRegex.charAt(i))))
                            randomRegexSymbolsCount++;
                }
            } else {
                return addAlternations(
                        generateRandomExpression(symbolsCount));
            }
        } else {
            throw UNEXPECTED_REGEX_NOTATION_TYPE_ERROR
                    .createError(command.notationType().toString())
                    .convertToException();
        }

        return randomRegex;
    }

    private static void initialize(RandomRegexCommand command) {
        parentheses = LEFT_PARENTHESIS.getValue() + String.valueOf(RIGHT_PARENTHESIS.getValue());

        int newMaxOperandsCount = OPERANDS_COUNT * 3;
        if (command.notationType() == COMMON) {
            // * by 3 -> just to generate approximately the same length common regex as the simple notation regex.
            // By default, the simple notation regex will be bigger due to all the possible grouping symbols in it,
            // which invokes more recursions to place something into it.
            newMaxOperandsCount *= 3;
            groupingSymbols = new String[]{parentheses};
        } else if (command.notationType() == SIMPLE) {
            brackets = LEFT_BRACKET.getValue() + String.valueOf(RIGHT_BRACKET.getValue());
            braces = LEFT_CURLY_BRACE.getValue() + String.valueOf(RIGHT_CURLY_BRACE.getValue());
            groupingSymbols = new String[]{parentheses, brackets, braces};
        } else {
            throw UNEXPECTED_REGEX_NOTATION_TYPE_ERROR
                    .createError(command.notationType().toString())
                    .convertToException();
        }

        if (!command.regexNumOfSymbols().equals("")) {
            symbolsCount = Integer.parseInt(command.regexNumOfSymbols());
        } else {
            int randomAlphabetCount = ThreadLocalRandom.current().nextInt(OPERANDS_COUNT, newMaxOperandsCount + 1);
            int randomNumberCount = ThreadLocalRandom.current().nextInt(OPERANDS_COUNT, newMaxOperandsCount + 1);
            symbolsCount = randomAlphabetCount + randomNumberCount;
        }

        StringBuilder uiAlphabet = new StringBuilder();
        StringBuilder uiNumbers = new StringBuilder();
        if (!command.regexSymbolsToUse().equals("")) {
            String uiA = command.regexSymbolsToUse();
            for (int i = 0; i < uiA.length(); i++) {
                if (isLetter(uiA.charAt(i))) uiAlphabet.append(uiA.charAt(i));
                else uiNumbers.append(uiA.charAt(i));
            }
        }

        if (!uiNumbers.toString().equals("") || !uiAlphabet.toString().equals("")) {
            alphabet = uiAlphabet.toString();
            numbers = uiNumbers.toString();
        } else {
            alphabet = IntStream.concat(IntStream.rangeClosed('a', 'z'), IntStream.rangeClosed('A', 'Z'))
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.joining());

            numbers = IntStream.rangeClosed('0', '9')
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.joining());
        }
    }

    public static String generateRandomExpression(int symbolsCount) {
        StringBuilder result = new StringBuilder();

        int symbolCount = 0;
        for (int i = 0; i < symbolsCount; i++) {
            int randomCaseChoice = random.nextInt(3);
            switch (randomCaseChoice) {
                case 0 -> result.insert(
                        random.nextInt(result.length() + 1),
                        !numbers.equals("")
                                ? numbers.charAt(random.nextInt(numbers.length()))
                                : alphabet.charAt(random.nextInt(alphabet.length())));

                case 1 -> result.insert(
                        random.nextInt(result.length() + 1),
                        !alphabet.equals("")
                                ? alphabet.charAt(random.nextInt(alphabet.length()))
                                : numbers.charAt(random.nextInt(numbers.length())));
                case 2 -> {
                    if (symbolCount < groupingSymbols.length) {
                        String groupingSymbol = groupingSymbols[random.nextInt(groupingSymbols.length)];
                        int index = random.nextInt(result.length() + 1);
                        if (index == 0 || index == result.length()) {
                            result.insert(index, groupingSymbol.charAt(0));
                            result.insert(index + 1, groupingSymbol.charAt(1));
                            symbolCount++;
                        } else {
                            if (!isGroupingSymbol(result.charAt(index - 1)) && !isGroupingSymbol(result.charAt(index))) {
                                result.insert(index, groupingSymbol.charAt(0));
                                result.insert(index + 1, groupingSymbol.charAt(1));
                                symbolCount++;
                            }
                        }
                    }
                }
            }
        }

        result = ensureNonEmptyGroupingSymbols(result, parentheses);
        if (brackets != null && braces != null) {
            result = ensureNonEmptyGroupingSymbols(result, brackets);
            result = ensureNonEmptyGroupingSymbols(result, braces);
        }

        if (result.length() == symbolsCount)
            return result.toString();

        return result.toString();
    }

    private static StringBuilder ensureNonEmptyGroupingSymbols(StringBuilder sb, String symbol) {
        int openingIndex = sb.indexOf(symbol);
        while (openingIndex >= 0) {
            int closingIndex = sb.indexOf(String.valueOf(symbol.charAt(1)), openingIndex + 1);
            if (closingIndex == openingIndex + 1) {
                sb.insert(
                        openingIndex + 1,
                        generateRandomExpression(random.nextInt(symbolsCount) + 1));
            } else if (closingIndex == -1) {
                sb.insert(sb.length(), symbol.charAt(1));
            }
            openingIndex = sb.indexOf(symbol, closingIndex + 1);
        }

        return sb;
    }

    private static boolean isGroupingSymbol(char symbol) {
        for (String groupingSymbol : groupingSymbols)
            if (groupingSymbol.contains(String.valueOf(symbol)))
                return true;

        return false;
    }

    private static String addAlternations(String regex) {
        final StringBuilder result = new StringBuilder(regex);
        for (int i = 0; i < result.length() - 1; i++) {
            if (isLetterOrDigit(result.charAt(i)) && isLetterOrDigit(result.charAt(i + 1))) {
                if (random.nextBoolean()) {
                    result.insert(i + 1, "|");
                    i++;
                }
            }
        }

        return result.toString();
    }

    private static String addQuantifiers(String regex) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < regex.length(); i++) {
            result.append(regex.charAt(i));
            if (regex.charAt(i) == RIGHT_PARENTHESIS.getValue() && random.nextBoolean()) {
                int randomQuantifier = random.nextInt(3);
                switch (randomQuantifier) {
                    case 0 -> result.append('*');
                    case 1 -> result.append('+');
                    case 2 -> result.append('?');
                }
            }
        }

        return result.toString();
    }
}