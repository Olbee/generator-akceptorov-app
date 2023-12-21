package generatorakceptorov.domain.regex.error;

import generatorakceptorov.command.error.domain.ErrorCode;
import generatorakceptorov.command.error.domain.ErrorCodeType;

import static generatorakceptorov.command.error.domain.ErrorCodeType.UNPROCESSABLE_ENTITY;

public enum RegexErrorCode implements ErrorCode {
    INVALID_REGEX_SYNTAX_ERROR(
            UNPROCESSABLE_ENTITY,
            "Provided regex: '%s' is invalid." +
                    " Please ensure the correct usage of parentheses, operators, syntax and valid semantic meaning."),
    UNEXPECTED_REGEX_NOTATION_TYPE_ERROR(
            UNPROCESSABLE_ENTITY, "An unexpected error occurred while attempting to parse regex notation type: %s."),

    INVALID_RANDOM_REGEX_SIZE_ERROR(
            UNPROCESSABLE_ENTITY, "The regex notation provided exceeds the maximum allowed size of 100, but actual is: %s.");

    private final ErrorCodeType type;
    private final String template;

    RegexErrorCode(ErrorCodeType type, String template) {
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
