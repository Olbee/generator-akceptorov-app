package generatorakceptorov.regex.port.outbound;

import generatorakceptorov.domain.regex.command.RandomRegexCommand;

public interface RegexGenerationPort {

  String generate(RandomRegexCommand command);
}
