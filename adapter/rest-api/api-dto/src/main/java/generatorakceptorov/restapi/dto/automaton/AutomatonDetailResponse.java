package generatorakceptorov.restapi.dto.automaton;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.immutables.value.Value.Style.ValidationMethod.NONE;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableAutomatonDetailResponse.class)
@JsonDeserialize(as = ImmutableAutomatonDetailResponse.class)
@Value.Style(stagedBuilder = true, validationMethod = NONE)
@Value.Immutable
public interface AutomatonDetailResponse extends BaseAutomatonDto {

  @Schema(
      description = "rest.api.automaton.dto.transitionPNGGraph.description",
      requiredMode = REQUIRED,
      format = "binary")
  @NotNull
  byte[] transitionPNGGraph();

  @Schema(
      description = "rest.api.automaton.dto.dfaPNGGraph.description",
      requiredMode = REQUIRED,
      format = "binary")
  @NotNull
  byte[] dfaPNGGraph();

  @Schema(
      description =
          "rest.api.automaton.dto.transitionTable.description", // TODO: add description to
      // messages.properties
      requiredMode = REQUIRED,
      format = "string")
  @NotBlank
  @NotNull
  String transitionTable();
}
