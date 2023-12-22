package generatorakceptorov.domain.automaton.entity;

import java.util.ArrayList;
import java.util.TreeSet;

public interface AutomatonEntity {

  ArrayList<Character> alphabet();

  Integer stateCount();

  Integer startState();

  TreeSet<Integer> acceptStates();

  Integer[][] transitions();
}
