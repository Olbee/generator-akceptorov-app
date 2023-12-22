package generatorakceptorov.restapi.dto.regex;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.immutables.value.Value.Style.ValidationMethod.NONE;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableBaseRegexDto.class)
@JsonDeserialize(as = ImmutableBaseRegexDto.class)
@Value.Style(stagedBuilder = true, validationMethod = NONE)
@Value.Immutable
public interface BaseRegexDto {

  @Schema(
      description = "rest.api.regex.dto.regex.description",
      requiredMode = REQUIRED,
      example = "0|1")
  @NotBlank
  @NotNull
  String regex();
}
