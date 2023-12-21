package generatorakceptorov.automaton.port.outbound;

import generatorakceptorov.domain.automaton.entity.DFAEntity;
import generatorakceptorov.domain.automaton.entity.MinDFAEntity;

public interface DFATransformationPort {

  MinDFAEntity transformToMinDFA(DFAEntity dfa);
}
