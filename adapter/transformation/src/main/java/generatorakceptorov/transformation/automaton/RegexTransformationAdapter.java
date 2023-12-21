package generatorakceptorov.transformation.automaton;

import generatorakceptorov.domain.automaton.entity.ImmutableNFAEntity;
import generatorakceptorov.domain.automaton.entity.NFAEntity;
import generatorakceptorov.automaton.port.outbound.RegexTransformationPort;
import generatorakceptorov.domain.regex.entity.RegexEntity;
import generatorakceptorov.transformation.AbstractTransformationAdapter;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RegexTransformationAdapter extends AbstractTransformationAdapter implements RegexTransformationPort {

    public NFAEntity transformToNFA(RegexEntity entity) {
        final ArrayList<Character> alphabet = resolveAlphabet(entity);
        final int statesCount = resolveStatesCount(entity);
        final ArrayList<Integer>[][] transitions = resolveTransitions(alphabet, statesCount);

        Stack<Integer> startStates = new Stack<>();
        Stack<Integer> acceptStates = new Stack<>();
        int newState = START_STATE;
        for (char currentChar : entity.regex().toCharArray()) {
            if (currentChar == CONCATENATION) {
                handleConcatenation(transitions, startStates, acceptStates);
            } else {
                int startState = newState++;
                int acceptState = newState++;
                if (currentChar == OR) handleOr(startState, acceptState, transitions, startStates, acceptStates);
                else if (currentChar == STAR) handleStar(startState, acceptState, transitions, startStates, acceptStates);
                else if (currentChar == PLUS) handlePlus(startState, acceptState, transitions, startStates, acceptStates);
                else if (currentChar == QUESTION) handleQuestion(startState, acceptState, transitions, startStates, acceptStates);
                else handleOperand(alphabet.indexOf(currentChar), startState, acceptState, transitions, startStates, acceptStates);
            }
        }

        final TreeSet<Integer> resultStartStates = new TreeSet<>();
        resultStartStates.add(startStates.pop());
        final TreeSet<Integer> resultAcceptStates = new TreeSet<>();
        resultAcceptStates.add(acceptStates.pop());

        return ImmutableNFAEntity.builder()
                .alphabet(alphabet)
                .stateCount(statesCount)
                .startStates(resultStartStates)
                .acceptStates(resultAcceptStates)
                .transitions(transitions)
                .build();
    }

    private void handleConcatenation(
            List<Integer>[][] transitions,
            Stack<Integer> startStates,
            Stack<Integer> acceptStates) {
        int tempState = acceptStates.pop();
        int fromState = acceptStates.pop();
        acceptStates.push(tempState);
        int toState = startStates.pop();
        transitions[fromState][START_STATE].add(toState);
    }

    private void handleOr(
            int startState,
            int acceptState,
            List<Integer>[][] transitions,
            Stack<Integer> startStates,
            Stack<Integer> acceptStates) {
        transitions[startState][START_STATE].add(startStates.pop());
        transitions[startState][START_STATE].add(startStates.pop());
        transitions[acceptStates.pop()][START_STATE].add(acceptState);
        transitions[acceptStates.pop()][START_STATE].add(acceptState);
        startStates.push(startState);
        acceptStates.push(acceptState);
    }

    private void handleStar(
            int startState,
            int acceptState,
            List<Integer>[][] transitions,
            Stack<Integer> startStates,
            Stack<Integer> acceptStates) {
        transitions[startState][START_STATE].add(acceptState);
        transitions[acceptStates.peek()][START_STATE].add(startStates.peek());
        transitions[startState][START_STATE].add(startStates.pop());
        transitions[acceptStates.pop()][START_STATE].add(acceptState);
        startStates.push(startState);
        acceptStates.push(acceptState);
    }

    private void handlePlus(
            int startState,
            int acceptState,
            List<Integer>[][] transitions,
            Stack<Integer> startStates,
            Stack<Integer> acceptStates) {
        int prevStartState = startStates.pop();
        int prevAcceptState = acceptStates.pop();
        transitions[prevAcceptState][START_STATE].add(prevStartState);
        transitions[startState][START_STATE].add(prevStartState);
        transitions[prevAcceptState][START_STATE].add(acceptState);
        transitions[prevAcceptState][START_STATE].add(startState);
        startStates.push(startState);
        acceptStates.push(acceptState);
    }

    private void handleQuestion(
            int startState,
            int acceptState,
            List<Integer>[][] transitions,
            Stack<Integer> startStates,
            Stack<Integer> acceptStates) {
        int prevStartState = startStates.pop();
        int prevAcceptState = acceptStates.pop();
        transitions[startState][START_STATE].add(prevStartState);
        transitions[startState][START_STATE].add(acceptState);
        transitions[prevAcceptState][START_STATE].add(acceptState);
        startStates.push(startState);
        acceptStates.push(acceptState);
    }

    private void handleOperand(
            int alphabetCurrentChar,
            int startState,
            int acceptState,
            List<Integer>[][] transitions,
            Stack<Integer> startStates,
            Stack<Integer> acceptStates) {
        transitions[startState][alphabetCurrentChar].add(acceptState);
        startStates.push(startState);
        acceptStates.push(acceptState);
    }

    private ArrayList<Character> resolveAlphabet(RegexEntity entity) {
        final ArrayList<Character> alphabet = new ArrayList<>();
        alphabet.add('$');
        for (char currentChar : entity.regex().toCharArray()) {
            if (isOperand(currentChar) && !alphabet.contains(currentChar))
                alphabet.add(currentChar);
        }

        return alphabet;
    }

    private int resolveStatesCount(RegexEntity entity) {
        int statesCount = 0;
        for (char currentChar : entity.regex().toCharArray())
            if (isOperand(currentChar)
                    || currentChar == OR
                    || currentChar == STAR
                    || currentChar == QUESTION
                    ||currentChar == PLUS)
                statesCount += 2;

        return statesCount;
    }

    private ArrayList<Integer>[][] resolveTransitions(ArrayList<Character> alphabet, int statesCount) {
        final ArrayList<Integer>[][] transitions = new ArrayList[statesCount][alphabet.size()];
        for (int i = 0; i < statesCount; i++)
            for (int j = 0; j < alphabet.size(); j++)
                transitions[i][j] = new ArrayList<>(2);

        return transitions;
    }
}
