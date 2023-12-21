package generatorakceptorov.command;

import generatorakceptorov.domain.regex.command.ImmutableRandomRegexCommand;
import generatorakceptorov.domain.regex.command.RandomRegexCommand;
import generatorakceptorov.restapi.dto.regex.GenerateRandomRegexRequest;

public class RandomRegexCommandFactory {

    public static RandomRegexCommand getRandomRegexCommand(GenerateRandomRegexRequest request) {
        return ImmutableRandomRegexCommand.builder()
                .notationType(request.notationType())
                .regexNumOfSymbols(request.numberOfSymbols())
                .regexSymbolsToUse(request.symbolsToUse())
                .build();
    }
}
