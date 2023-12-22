package generatorakceptorov.generation.graph;

import generatorakceptorov.domain.automaton.entity.AutomatonEntity;
import java.util.HashMap;
import java.util.Map;

// TODO: rework
public abstract class AbstractDotGraphGenerationAdapter {

  protected Map<String, String> getUniqueTransition(AutomatonEntity entity) {
    final Map<String, String> uniqueTransitions = new HashMap<>();
    for (int i = 0; i < entity.transitions().length; i++) {
      for (int j = 0; j < entity.transitions()[i].length; j++) {
        if (entity.transitions()[i][j] != null) {
          int nextState = entity.transitions()[i][j];
          char value = entity.alphabet().get(j);
          String key = i + ":" + nextState;
          String symbols = uniqueTransitions.getOrDefault(key, "");
          if (!symbols.isEmpty()) {
            symbols += ",";
          }
          symbols += value;
          uniqueTransitions.put(key, symbols);
        }
      }
    }

    return uniqueTransitions;
  }
}
