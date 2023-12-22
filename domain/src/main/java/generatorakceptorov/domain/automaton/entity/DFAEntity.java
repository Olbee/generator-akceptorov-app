package generatorakceptorov.domain.automaton.entity;

import java.util.ArrayList;
import java.util.TreeSet;
import org.immutables.value.Value;

@Value.Immutable
public interface DFAEntity extends AutomatonEntity {

  ArrayList<TreeSet<Integer>> nfaToDfaStateTransitions();
}
