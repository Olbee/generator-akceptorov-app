package generatorakceptorov.domain.automaton.data;

import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import org.immutables.value.Value;

@Value.Immutable
public interface AutomatonData {

  MinDFAEntity minDfa();

  byte[] transitionPNGGraph();

  byte[] dfaPNGGraph();

  String transitionTable();
}
