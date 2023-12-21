package generatorakceptorov.restapi.dto.code;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import generatorakceptorov.domain.regex.ProgrammingLanguage;
import generatorakceptorov.restapi.dto.automaton.BaseAutomatonDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.immutables.value.Value;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.immutables.value.Value.Style.ValidationMethod.NONE;

@JsonDeserialize(as = ImmutableGenerateCodesRequest.class)
@JsonSerialize(as = ImmutableGenerateCodesRequest.class)
@Value.Style(stagedBuilder = true, validationMethod = NONE)
@Value.Immutable
public interface GenerateCodesRequest {

    @Schema(
            description = "rest.api.code.dto.automaton.description",
            requiredMode = REQUIRED,
            example =
                    """
                    {
                        "alphabet": ["a", "b", "c"],
                        "stateCount": 4,
                        "startState": 0,
                        "acceptStates": [1, 2],
                        "transitions": [[1, 2, 0], [2, 3, 1], [3, 0, 2], [0, 1, 1]],
                        "dfaToMinDfaStateTransitions": [
                            [0, 2],
                            [1, 1],
                            [2, 0],
                            [3, 3]
                        ]
                    }
                    """)
    @NotNull
    BaseAutomatonDto automaton();

    @Schema(
            description = "rest.api.code.dto.languagesToDownloadCode.description",
            requiredMode = REQUIRED,
            example =
                """
                {
                    "C": true,
                    "Java": false,
                    "Python": true
                }
                """)
    @NotNull
    List<ProgrammingLanguage> languagesToDownloadCode();
}
