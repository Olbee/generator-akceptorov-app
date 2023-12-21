package generatorakceptorov.automaton.port.outbound;

import generatorakceptorov.domain.automaton.entity.NFAEntity;
import generatorakceptorov.domain.regex.entity.RegexEntity;

public interface RegexTransformationPort {

  NFAEntity transformToNFA(RegexEntity regex);
}
