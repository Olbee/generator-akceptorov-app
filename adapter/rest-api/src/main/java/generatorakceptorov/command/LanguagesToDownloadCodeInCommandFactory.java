package generatorakceptorov.command;

import generatorakceptorov.domain.automaton.entity.ImmutableMinDFAEntity;
import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import generatorakceptorov.domain.code.command.ImmutableLanguagesToDownloadCodeCommand;
import generatorakceptorov.domain.code.command.LanguagesToDownloadCodeCommand;
import generatorakceptorov.restapi.dto.automaton.BaseAutomatonDto;
import generatorakceptorov.restapi.dto.code.GenerateCodesRequest;

public class LanguagesToDownloadCodeInCommandFactory {

    private static MinDFAEntity map(BaseAutomatonDto automaton) {
        return ImmutableMinDFAEntity.builder()
                .acceptStates(automaton.acceptStates())
                .transitions(automaton.transitions())
                .alphabet(automaton.alphabet())
                .stateCount(automaton.stateCount())
                .startState(automaton.startState())
                .dfaToMinDfaStateTransitions(automaton.dfaToMinDfaStateTransitions())
                .build();
    }

    public static LanguagesToDownloadCodeCommand getLanguagesToDownloadCodeCommand(GenerateCodesRequest request) {
            return ImmutableLanguagesToDownloadCodeCommand.builder()
                .minDFA(map(request.automaton()))
                .languagesToDownloadCode(request.languagesToDownloadCode())
                .build();
    }
}
