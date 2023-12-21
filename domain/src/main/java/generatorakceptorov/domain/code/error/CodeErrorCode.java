package generatorakceptorov.domain.code.error;

import generatorakceptorov.command.error.domain.ErrorCode;
import generatorakceptorov.command.error.domain.ErrorCodeType;

import static generatorakceptorov.command.error.domain.ErrorCodeType.INTERNAL;


public enum CodeErrorCode implements ErrorCode {

    CODE_FILES_TO_ZIP_GENERATE_ERROR(
            INTERNAL, "An unexpected error occurred while attempting to generate ZIP of code files. Cause exception message: %s");

    private final ErrorCodeType type;
    private final String template;

    CodeErrorCode(ErrorCodeType type, String template) {
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
