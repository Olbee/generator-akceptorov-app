package generatorakceptorov.transformation.automaton;

import generatorakceptorov.domain.automaton.entity.DFAEntity;
import generatorakceptorov.domain.automaton.entity.ImmutableMinDFAEntity;
import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import generatorakceptorov.automaton.port.outbound.DFATransformationPort;
import generatorakceptorov.transformation.AbstractTransformationAdapter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

//TODO: rework
@Component
public class DFATransformationAdapter extends AbstractTransformationAdapter implements DFATransformationPort {

    public MinDFAEntity transformToMinDFA(DFAEntity dfa) {
        final ArrayList<Character> alphabet = dfa.alphabet();
        final TreeSet<Integer> notAcceptDfaStates = resolveNotAcceptDfaStates(dfa);

        final ArrayList<TreeSet<Integer>> dfaToMinDfaStateTransitions = new ArrayList<>();
        dfaToMinDfaStateTransitions.add(dfa.acceptStates());
        dfaToMinDfaStateTransitions.add(notAcceptDfaStates);

        ArrayList<TreeSet<Integer>> currentPartition = new ArrayList<>();
        currentPartition.add(dfa.acceptStates());
        currentPartition.add(notAcceptDfaStates);

        while (!currentPartition.isEmpty()) {
            TreeSet<Integer> currentStates = currentPartition.remove(currentPartition.size() - 1);
            for (int operand = 0; operand < alphabet.size(); operand++) {
                TreeSet<Integer> nextPartitions = new TreeSet<>();
                for (int fromState = 0; fromState < dfa.stateCount(); fromState++)
                    if (dfa.transitions()[fromState][operand] != null
                            && currentStates.contains(dfa.transitions()[fromState][operand]))
                        nextPartitions.add(fromState);

                final ArrayList<TreeSet<Integer>> transitions = new ArrayList<>();
                for (TreeSet<Integer> transition : dfaToMinDfaStateTransitions) {
                    TreeSet<Integer> statesFirst = new TreeSet<>(nextPartitions);
                    TreeSet<Integer> statesSecond = new TreeSet<>(transition);
                    statesFirst.retainAll(transition);
                    statesSecond.removeAll(nextPartitions);
                    if (!statesFirst.isEmpty() && !statesSecond.isEmpty()) {
                        transitions.add(statesFirst);
                        transitions.add(statesSecond);
                        if (currentPartition.contains(transition)) {
                            currentPartition.remove(transition);
                            currentPartition.add(statesFirst);
                            currentPartition.add(statesSecond);
                        } else {
                            currentPartition.add(
                                statesFirst.size() <= statesSecond.size()
                                    ? statesFirst
                                    : statesSecond);
                        }
                    } else {
                        transitions.add(transition);
                    }
                }
                dfaToMinDfaStateTransitions.clear();
                dfaToMinDfaStateTransitions.addAll(transitions);
            }
        }

        final Integer[][] transitions = resolveTransitions(dfa, alphabet, dfaToMinDfaStateTransitions);
        final TreeSet<Integer> acceptStates = resolveAcceptStates(dfa, dfaToMinDfaStateTransitions);

        return ImmutableMinDFAEntity.builder()
                .alphabet(alphabet)
                .stateCount(dfaToMinDfaStateTransitions.size())
                .startState(START_STATE)
                .acceptStates(acceptStates)
                .transitions(transitions)
                .dfaToMinDfaStateTransitions(dfaToMinDfaStateTransitions)
                .build();
    }

    private TreeSet<Integer> resolveNotAcceptDfaStates(DFAEntity entity) {
        final TreeSet<Integer> notAcceptDfaStates = new TreeSet<>();
        for (int state = 0; state < entity.stateCount(); state++)
            if (!entity.acceptStates().contains(state))
                notAcceptDfaStates.add(state);

        return notAcceptDfaStates;
    }

    private Integer[][] resolveTransitions(DFAEntity entity, ArrayList<Character> alphabet, ArrayList<TreeSet<Integer>> dfaToMinDfaStateTransitions) {
        dfaToMinDfaStateTransitions.removeIf(TreeSet::isEmpty);
        dfaToMinDfaStateTransitions.sort(Comparator.comparingInt(TreeSet::first));

        final int statesCount = dfaToMinDfaStateTransitions.size();
        final Integer[][] transitions = new Integer[statesCount][alphabet.size()];

        for (int fromState = 0; fromState < statesCount; fromState++) {
            for (int operand = 0; operand < alphabet.size(); operand++) {
                int fromDfaState = dfaToMinDfaStateTransitions.get(fromState).first();
                Integer toDfaState = entity.transitions()[fromDfaState][operand];
                if (toDfaState != null) {
                    for (TreeSet<Integer> toMinDfaState : dfaToMinDfaStateTransitions) {
                        if (toMinDfaState.contains(toDfaState)) {
                            transitions[fromState][operand] = dfaToMinDfaStateTransitions.indexOf(toMinDfaState);
                            break;
                        }
                    }
                }
            }
        }

        return transitions;
    }

    private TreeSet<Integer> resolveAcceptStates(DFAEntity entity, ArrayList<TreeSet<Integer>> dfaToMinDfaStateTransitions) {
        final TreeSet<Integer> acceptStates = new TreeSet<>();
        for (TreeSet<Integer> transition : dfaToMinDfaStateTransitions) {
            for (Integer acceptState : entity.acceptStates()) {
                if (transition.contains(acceptState)) {
                    acceptStates.add(dfaToMinDfaStateTransitions.indexOf(transition));
                    break;
                }
            }
        }

        return acceptStates;
    }
}