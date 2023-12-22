package generatorakceptorov.restapi.dto.regex;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import generatorakceptorov.restapi.dto.notation.RegexNotationTypeRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.immutables.value.Value;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.immutables.value.Value.Style.ValidationMethod.NONE;

@JsonSerialize(as = ImmutableGenerateRandomRegexRequest.class)
@JsonDeserialize(as = ImmutableGenerateRandomRegexRequest.class)
@Value.Style(stagedBuilder = true, validationMethod = NONE)
@Value.Immutable
public interface GenerateRandomRegexRequest extends RegexNotationTypeRequest {

    @Schema(
            description = "rest.api.notation.dto.numberOfSymbols.description", // TODO: documentation
            requiredMode = REQUIRED,
            example = "12")
    @NotBlank
    @NotNull
    String numberOfSymbols();

    @Schema(
            description = "rest.api.notation.dto.symbolsToUse.description", // TODO: documentation
            requiredMode = REQUIRED,
            example = "abc123")
    @NotBlank
    @NotNull
    String symbolsToUse();
}
