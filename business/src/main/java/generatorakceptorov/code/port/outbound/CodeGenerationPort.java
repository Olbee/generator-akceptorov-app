package generatorakceptorov.code.port.outbound;

import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import java.util.HashMap;

public interface CodeGenerationPort {

  HashMap<String, String> generateFromMinDFA(MinDFAEntity minDFA);
}
