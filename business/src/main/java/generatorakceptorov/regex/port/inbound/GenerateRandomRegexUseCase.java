package generatorakceptorov.regex.port.inbound;

import generatorakceptorov.domain.regex.command.RandomRegexCommand;
import generatorakceptorov.domain.regex.data.RegexData;

public interface GenerateRandomRegexUseCase {

    RegexData execute(RandomRegexCommand command);
}
