package generatorakceptorov.generation.table;

import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import generatorakceptorov.graph.port.outbound.DFATransitionTableGenerationPort;
import org.springframework.stereotype.Component;

import java.util.Map;

//TODO: rework
@Component
public class DFATransitionTableGenerationAdapter implements DFATransitionTableGenerationPort {

    @Override
    public String generateFromMinDFA(MinDFAEntity entity) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>");
        sb.append("<th>state</th>");

        for (char currentChar : entity.alphabet())
            sb.append("<th>").append(currentChar).append("</th>");

        for (int state = 0; state < entity.stateCount(); state++) {
            sb.append("<tr>");
            sb.append("<td>q").append(state).append("</td>");
            for (int currentChar = 0; currentChar < entity.alphabet().size(); currentChar++) {
                final String toAppend =
                        entity.transitions()[state][currentChar] == null
                            ? "⊥"
                            : "q" + entity.transitions()[state][currentChar];
                sb.append("<td>").append(toAppend).append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }
}