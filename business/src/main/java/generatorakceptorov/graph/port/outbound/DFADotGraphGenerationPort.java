package generatorakceptorov.graph.port.outbound;

import generatorakceptorov.domain.automaton.entity.MinDFAEntity;

public interface DFADotGraphGenerationPort {

  String generateFromMinDFA(MinDFAEntity minDFA);
}
