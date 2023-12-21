package generatorakceptorov.domain.regex.command;

import org.immutables.value.Value;

@Value.Immutable
public interface RandomRegexCommand extends RegexNotationTypeCommand {

    @Value.Parameter
    String regexNumOfSymbols();

    @Value.Parameter
    String regexSymbolsToUse();
}