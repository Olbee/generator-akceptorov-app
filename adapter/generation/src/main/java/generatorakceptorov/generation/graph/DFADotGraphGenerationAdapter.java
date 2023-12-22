package generatorakceptorov.generation.graph;

import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import generatorakceptorov.graph.port.outbound.DFADotGraphGenerationPort;
import java.util.Map;
import org.springframework.stereotype.Component;

// TODO: rework
@Component
public class DFADotGraphGenerationAdapter extends AbstractDotGraphGenerationAdapter
    implements DFADotGraphGenerationPort {

  @Override
  public String generateFromMinDFA(MinDFAEntity entity) {
    final StringBuilder sb = new StringBuilder();
    sb.append("digraph transition_graph {\n");
    sb.append("    rankdir=LR;\n");
    for (int i = 0; i < entity.transitions().length; i++) {
      String shape = entity.acceptStates().contains(i) ? "doublecircle" : "ellipse";
      sb.append("    ")
          .append(i)
          .append(" [label=\"q")
          .append(i)
          .append("\", shape=")
          .append(shape)
          .append("];\n");
    }

    final Map<String, String> uniqueTransitions = getUniqueTransition(entity);
    for (String key : uniqueTransitions.keySet()) {
      String[] parts = key.split(":");
      int sourceState = Integer.parseInt(parts[0]);
      int targetState = Integer.parseInt(parts[1]);
      String symbols = uniqueTransitions.get(key);
      sb.append("    ")
          .append(sourceState)
          .append(" -> ")
          .append(targetState)
          .append(" [dir=forward, label=<")
          .append(symbols)
          .append(">];\n");
    }
    sb.append("}");

    return sb.toString();
  }
}
