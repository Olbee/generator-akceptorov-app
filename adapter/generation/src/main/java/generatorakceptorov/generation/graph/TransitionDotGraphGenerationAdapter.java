package generatorakceptorov.generation.graph;

import generatorakceptorov.domain.automaton.entity.DFAEntity;
import generatorakceptorov.graph.port.outbound.TransitionDotGraphGenerationPort;
import org.springframework.stereotype.Component;

import java.util.*;

//TODO: rework
@Component
public class TransitionDotGraphGenerationAdapter extends AbstractDotGraphGenerationAdapter implements TransitionDotGraphGenerationPort {

    @Override
    public String generateFromDFA(DFAEntity dfaEntity) {
        return "digraph transition_graph {\n" +
                "    rankdir=LR;\n" +
                addInitializedStates(dfaEntity) +
                addTransitions(dfaEntity) +
                addAcceptStatesToAcceptInGraph(dfaEntity) +
                "}";
    }

    private String addInitializedStates(DFAEntity entity) {
        final StringBuilder sb = new StringBuilder();
        sb.append("    ").append("start").append(" [shape=none, label=\"start\"]\n");
        sb.append("    ").append("accept").append(" [shape=none, label=\"accept\"]\n\n");

        final Map<String, String> uniqueTransitions = getUniqueTransition(entity);
        for (String key : uniqueTransitions.keySet()) {
            final String fromState = key.split(":")[1];
            String toState = uniqueTransitions.get(key);
            sb.append("    ").append(fromState).append(" [label=\"").append(toState).append("\"];\n");
        }

        return sb.toString();
    }

    private String addTransitions(DFAEntity entity) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entity.transitions().length; i++) {
            for (int j = 0; j < entity.transitions()[i].length; j++) {
                if (entity.transitions()[i][j] != null) {
                    final String fromState = (i == 0) ? "start" : String.valueOf(i);
                    final Integer toState = entity.transitions()[i][j];
                    sb.append(fromState).append(" -> ").append(toState).append(" [dir=forward];\n");
                }
            }
        }

        return sb.toString();
    }

    private String addAcceptStatesToAcceptInGraph(DFAEntity entity) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= entity.stateCount(); i++) {
            if (entity.acceptStates().contains(i)) {
                String fromState = (i == 0) ? "start" : String.valueOf(i);
                sb.append(fromState).append(" -> ").append("accept").append(" [dir=forward];\n");
            }
        }

        return sb.toString();
    }
}
