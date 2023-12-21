package generatorakceptorov.graph.service;

import generatorakceptorov.automaton.port.outbound.DFATransformationPort;
import generatorakceptorov.automaton.port.outbound.NFATransformationPort;
import generatorakceptorov.automaton.port.outbound.RegexTransformationPort;
import generatorakceptorov.domain.automaton.entity.DFAEntity;
import generatorakceptorov.graph.port.inbound.GenerateGraphsUseCase;
import generatorakceptorov.domain.automaton.data.AutomatonData;
import generatorakceptorov.domain.automaton.data.ImmutableAutomatonData;
import generatorakceptorov.domain.automaton.entity.MinDFAEntity;
import generatorakceptorov.domain.regex.command.RegexNotationTypeCommand;
import generatorakceptorov.graph.port.outbound.DFADotGraphGenerationPort;
import generatorakceptorov.graph.port.outbound.DFATransitionTableGenerationPort;
import generatorakceptorov.graph.port.outbound.DotGraphTransformationPort;
import generatorakceptorov.graph.port.outbound.TransitionDotGraphGenerationPort;
import generatorakceptorov.regex.port.outbound.StringTransformationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateGraphsService implements GenerateGraphsUseCase {

    private final StringTransformationPort stringTransformationPort;
    private final RegexTransformationPort regexTransformationPort;
    private final NFATransformationPort NFATransformationPort;
    private final DFATransformationPort DFATransformationPort;
    private final TransitionDotGraphGenerationPort transitionDotGraphGenerationPort;
    private final DFADotGraphGenerationPort dfaDotGraphGenerationPort;
    private final DotGraphTransformationPort dotGraphTransformationPort;
    private final DFATransitionTableGenerationPort dfaTransitionTableGenerationPort;

    @Autowired
    public GenerateGraphsService(
            StringTransformationPort stringTransformationPort,
            RegexTransformationPort regexTransformationPort,
            NFATransformationPort NFATransformationPort,
            DFATransformationPort DFATransformationPort,
            TransitionDotGraphGenerationPort transitionDotGraphGenerationPort,
            DFADotGraphGenerationPort dfaDotGraphGenerationPort,
            DotGraphTransformationPort dotGraphTransformationPort, DFATransitionTableGenerationPort dfaTransitionTableGenerationPort) {
        this.stringTransformationPort = stringTransformationPort;
        this.regexTransformationPort = regexTransformationPort;
        this.NFATransformationPort = NFATransformationPort;
        this.DFATransformationPort = DFATransformationPort;
        this.transitionDotGraphGenerationPort = transitionDotGraphGenerationPort;
        this.dfaDotGraphGenerationPort = dfaDotGraphGenerationPort;
        this.dotGraphTransformationPort = dotGraphTransformationPort;
        this.dfaTransitionTableGenerationPort = dfaTransitionTableGenerationPort;
    }

    @Override
    //TODO: refactoring. Separate functionality for automaton and graphs
    public AutomatonData execute(String input, RegexNotationTypeCommand command) {
        final DFAEntity dfa =
                NFATransformationPort.transformToDFA(
                    regexTransformationPort.transformToNFA(
                        stringTransformationPort.transformToRegex(input, command.notationType())));
        final MinDFAEntity minDFA = DFATransformationPort.transformToMinDFA(dfa);

        final byte[] transitionPNGGraph =
                dotGraphTransformationPort.convertToPNG(
                        transitionDotGraphGenerationPort.generateFromDFA(dfa));
        final byte[] dfaPNGGraph =
                dotGraphTransformationPort.convertToPNG(
                    dfaDotGraphGenerationPort.generateFromMinDFA(minDFA));
        final String transitionTable =
                dfaTransitionTableGenerationPort.generateFromMinDFA(minDFA);

        return ImmutableAutomatonData.builder()
                .minDfa(minDFA)
                .transitionPNGGraph(transitionPNGGraph)
                .dfaPNGGraph(dfaPNGGraph)
                .transitionTable(transitionTable)
                .build();
    }
}
