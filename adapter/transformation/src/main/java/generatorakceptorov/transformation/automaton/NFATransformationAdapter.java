package generatorakceptorov.transformation.automaton;

import generatorakceptorov.domain.automaton.entity.DFAEntity;
import generatorakceptorov.domain.automaton.entity.ImmutableDFAEntity;
import generatorakceptorov.domain.automaton.entity.NFAEntity;
import generatorakceptorov.automaton.port.outbound.NFATransformationPort;
import generatorakceptorov.transformation.AbstractTransformationAdapter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeSet;

@Component
public class NFATransformationAdapter extends AbstractTransformationAdapter implements NFATransformationPort {

    public DFAEntity transformToDFA(NFAEntity nfa) {
        final ArrayList<Character> alphabet = resolveAlphabet(nfa);
        final Integer[][] transitions = new Integer[nfa.stateCount()][alphabet.size()];

        final ArrayList<TreeSet<Integer>> nfaToDfaStateTransitions = new ArrayList<>();
        final Stack<Integer> reachedStates = new Stack<>();
        nfaToDfaStateTransitions.add(getClosure(nfa, nfa.startStates()));
        reachedStates.add(START_STATE);
        while (!reachedStates.isEmpty()) {
            int fromState = reachedStates.pop();
            for (int operand = 0; operand < alphabet.size(); operand++) {
                TreeSet<Integer> nextNfaStates = new TreeSet<>();
                for (int fromNfaState : nfaToDfaStateTransitions.get(fromState))
                    nextNfaStates.addAll(nfa.transitions()[fromNfaState][operand + 1]);
                if (!nextNfaStates.isEmpty()) {
                    TreeSet<Integer> union = getClosure(nfa, nextNfaStates);
                    if (!nfaToDfaStateTransitions.contains(union)) {
                        nfaToDfaStateTransitions.add(union);
                        reachedStates.add(nfaToDfaStateTransitions.indexOf(union));
                    }
                    transitions[fromState][operand] = nfaToDfaStateTransitions.indexOf(union);
                }
            }
        }

        return ImmutableDFAEntity.builder()
                .alphabet(alphabet)
                .stateCount(nfaToDfaStateTransitions.size())
                .startState(START_STATE)
                .acceptStates(resolveAcceptStates(nfa, nfaToDfaStateTransitions))
                .transitions(transitions)
                .nfaToDfaStateTransitions(nfaToDfaStateTransitions)
                .build();
    }

    private TreeSet<Integer> getClosure(NFAEntity nfa, TreeSet<Integer> nextNfaStates) {
        TreeSet<Integer> reachableStates = new TreeSet<>();
        reachableStates.addAll(nextNfaStates);
        Stack<Integer> reachedStates = new Stack<>();
        reachedStates.addAll(nextNfaStates);

        while (!reachedStates.isEmpty()) {
            int fromState = reachedStates.pop();
            if (nfa.transitions()[fromState][START_STATE] != null) {
                ArrayList<Integer> toStates = nfa.transitions()[fromState][START_STATE];
                for (int toState : toStates) {
                    if (!reachableStates.contains(toState)) {
                        reachableStates.add(toState);
                        reachedStates.push(toState);
                    }
                }
            }
        }
        return reachableStates;
    }

    private ArrayList<Character> resolveAlphabet(NFAEntity entity) {
        final ArrayList<Character> alphabet = entity.alphabet();
        entity.alphabet().remove(START_STATE);

        return alphabet;
    }

    private TreeSet<Integer> resolveAcceptStates(NFAEntity entity, ArrayList<TreeSet<Integer>> nfaToDfaStateTransitions) {
        final TreeSet<Integer> acceptStates = new TreeSet<>();
        for (TreeSet<Integer> transition : nfaToDfaStateTransitions)
            if (transition.containsAll(entity.acceptStates()))
                acceptStates.add(nfaToDfaStateTransitions.indexOf(transition));

        return acceptStates;
    }
}