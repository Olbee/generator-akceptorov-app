package generatorakceptorov.automaton.port.outbound;

import generatorakceptorov.domain.automaton.entity.DFAEntity;
import generatorakceptorov.domain.automaton.entity.NFAEntity;

public interface NFATransformationPort {

  DFAEntity transformToDFA(NFAEntity nfa);
}
