package generatorakceptorov.graph.port.outbound;

import generatorakceptorov.domain.automaton.entity.DFAEntity;

public interface TransitionDotGraphGenerationPort {

  String generateFromDFA(DFAEntity DFA);
}
