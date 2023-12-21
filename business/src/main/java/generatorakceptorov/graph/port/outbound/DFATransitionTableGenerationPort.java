package generatorakceptorov.graph.port.outbound;

import generatorakceptorov.domain.automaton.entity.MinDFAEntity;

public interface DFATransitionTableGenerationPort {

    String generateFromMinDFA(MinDFAEntity minDFA);
}
