package generatorakceptorov.domain.automaton.entity;

import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.TreeSet;

@Value.Immutable
public interface NFAEntity {

    ArrayList<Character> alphabet();

    Integer stateCount();

    TreeSet<Integer> startStates();

   TreeSet<Integer> acceptStates();

    ArrayList<Integer>[][] transitions();
}

