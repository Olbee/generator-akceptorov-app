package generatorakceptorov.restapi.dto.automaton;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.immutables.value.Value.Style.ValidationMethod.NONE;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.TreeSet;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableBaseAutomatonDto.class)
@JsonDeserialize(as = ImmutableBaseAutomatonDto.class)
@Value.Style(stagedBuilder = true, validationMethod = NONE)
@Value.Immutable
public interface BaseAutomatonDto {

  @Schema(
      description = "rest.api.automaton.dto.alphabet.description",
      requiredMode = REQUIRED,
      example = "[\"a\", \"b\", \"c\"]")
  @NotNull
  ArrayList<Character> alphabet();

  @Schema(
      description = "rest.api.automaton.dto.stateCount.description",
      requiredMode = REQUIRED,
      example = "3")
  @NotNull
  Integer stateCount();

  @Schema(
      description = "rest.api.automaton.dto.startState.description",
      requiredMode = REQUIRED,
      example = "0")
  @NotNull
  Integer startState();

  @Schema(
      description = "rest.api.automaton.dto.acceptStates.description",
      requiredMode = REQUIRED,
      example = "[1, 2]")
  @NotNull
  TreeSet<Integer> acceptStates();

  @Schema(
      description = "rest.api.automaton.dto.transitions.description",
      requiredMode = REQUIRED,
      example =
          """
                [
                    [0, 1],
                    [1, 2],
                    [2, 1]
                ]
                """)
  @NotNull
  Integer[][] transitions();

  @Schema(
      description = "rest.api.automaton.dto.dfaToMinDfaStateTransitions.description",
      requiredMode = REQUIRED,
      example =
          """
                [
                    [0, 1],
                    [1, 2]
                ]
                """)
  @NotNull
  ArrayList<TreeSet<Integer>> dfaToMinDfaStateTransitions();
}
