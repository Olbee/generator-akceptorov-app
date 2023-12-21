package generatorakceptorov.mapper;

import generatorakceptorov.domain.automaton.data.AutomatonData;
import generatorakceptorov.restapi.dto.automaton.AutomatonDetailResponse;
import generatorakceptorov.restapi.dto.automaton.ImmutableAutomatonDetailResponse;

public final class AutomatonResponseMapper {

    public static AutomatonDetailResponse map(AutomatonData data) {
        return ImmutableAutomatonDetailResponse.builder()
                .transitionPNGGraph(data.transitionPNGGraph())
                .dfaPNGGraph(data.dfaPNGGraph())
                .transitionTable(data.transitionTable())
                .alphabet(data.minDfa().alphabet())
                .stateCount(data.minDfa().stateCount())
                .startState(data.minDfa().startState())
                .acceptStates(data.minDfa().acceptStates())
                .transitions(data.minDfa().transitions())
                .dfaToMinDfaStateTransitions(data.minDfa().dfaToMinDfaStateTransitions())
                .build();
    }
}
