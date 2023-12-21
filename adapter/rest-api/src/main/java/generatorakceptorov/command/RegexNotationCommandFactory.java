package generatorakceptorov.command;

import generatorakceptorov.domain.regex.command.ImmutableRegexNotationTypeCommand;
import generatorakceptorov.domain.regex.command.RegexNotationTypeCommand;
import generatorakceptorov.restapi.dto.notation.RegexNotationTypeRequest;

public class RegexNotationCommandFactory {

    public static RegexNotationTypeCommand getRegexNotationTypeCommand(RegexNotationTypeRequest request) {
        return ImmutableRegexNotationTypeCommand.of(request.notationType());
    }
}
