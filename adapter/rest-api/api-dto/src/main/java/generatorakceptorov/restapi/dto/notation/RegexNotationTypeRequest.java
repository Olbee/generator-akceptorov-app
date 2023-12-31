package generatorakceptorov.restapi.dto.notation;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.immutables.value.Value.Style.ValidationMethod.NONE;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import generatorakceptorov.domain.regex.NotationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableRegexNotationTypeRequest.class)
@JsonDeserialize(as = ImmutableRegexNotationTypeRequest.class)
@Value.Style(stagedBuilder = true, validationMethod = NONE)
@Value.Immutable
public interface RegexNotationTypeRequest {

  @Schema(
      description = "rest.api.notation.dto.notations.description",
      requiredMode = REQUIRED,
      example = "SIMPLE")
  @NotNull
  NotationType notationType();
}
