package generatorakceptorov.domain.automaton.entity;

import java.util.ArrayList;
import java.util.TreeSet;
import org.immutables.value.Value;

@Value.Immutable
public interface NFAEntity {

  ArrayList<Character> alphabet();

  Integer stateCount();

  TreeSet<Integer> startStates();

  TreeSet<Integer> acceptStates();

  ArrayList<Integer>[][] transitions();
}
