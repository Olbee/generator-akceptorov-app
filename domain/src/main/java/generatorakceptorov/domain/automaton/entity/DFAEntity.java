package generatorakceptorov.domain.automaton.entity;

import org.immutables.value.Value;

import java.util.ArrayList;
import java.util.TreeSet;

@Value.Immutable
public interface DFAEntity extends AutomatonEntity {

    ArrayList<TreeSet<Integer>> nfaToDfaStateTransitions();
}
