package generatorakceptorov.graph.port.inbound;

import generatorakceptorov.domain.automaton.data.AutomatonData;
import generatorakceptorov.domain.regex.command.RegexNotationTypeCommand;

public interface GenerateGraphsUseCase {

    AutomatonData execute(String regex, RegexNotationTypeCommand command);
}
