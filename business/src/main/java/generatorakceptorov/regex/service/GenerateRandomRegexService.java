package generatorakceptorov.regex.service;

import generatorakceptorov.domain.regex.command.RandomRegexCommand;
import generatorakceptorov.domain.regex.data.ImmutableRegexData;
import generatorakceptorov.domain.regex.data.RegexData;
import generatorakceptorov.regex.port.inbound.GenerateRandomRegexUseCase;
import generatorakceptorov.regex.port.outbound.RegexGenerationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateRandomRegexService implements GenerateRandomRegexUseCase {

    private final RegexGenerationPort regexGenerationPort;

    @Autowired
    public GenerateRandomRegexService(RegexGenerationPort regexGenerationPort) {
        this.regexGenerationPort = regexGenerationPort;
    }

    @Override
    public RegexData execute(RandomRegexCommand command) {
        return ImmutableRegexData.of(regexGenerationPort.generate(command));
    }
}
