package generatorakceptorov.controller;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import generatorakceptorov.code.port.inbound.DownloadCodeFilesAsZipUseCase;
import generatorakceptorov.command.LanguagesToDownloadCodeInCommandFactory;
import generatorakceptorov.command.RandomRegexCommandFactory;
import generatorakceptorov.command.RegexNotationCommandFactory;
import generatorakceptorov.domain.code.data.CodeFilesZipData;
import generatorakceptorov.domain.regex.command.RandomRegexCommand;
import generatorakceptorov.domain.regex.command.RegexNotationTypeCommand;
import generatorakceptorov.graph.port.inbound.GenerateGraphsUseCase;
import generatorakceptorov.mapper.AutomatonResponseMapper;
import generatorakceptorov.mapper.RegexResponseMapper;
import generatorakceptorov.regex.port.inbound.GenerateRandomRegexUseCase;
import generatorakceptorov.restapi.dto.automaton.AutomatonDetailResponse;
import generatorakceptorov.restapi.dto.code.GenerateCodesRequest;
import generatorakceptorov.restapi.dto.notation.RegexNotationTypeRequest;
import generatorakceptorov.restapi.dto.regex.BaseRegexDto;
import generatorakceptorov.restapi.dto.regex.GenerateRandomRegexRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Generator controller")
@Controller
public class GeneratorController extends BaseController {

  public static final String GENERATE_GRAPHS_URI = BASE_V1_URI + "/generate-graphs";
  public static final String GENERATE_CODES_URI = BASE_V1_URI + "/generate-codes";
  public static final String GENERATE_RANDOM_REGEX_URI = BASE_V1_URI + "/generate-random-regex";

  private final DownloadCodeFilesAsZipUseCase downloadCodeFilesAsZipUseCase;
  private final GenerateGraphsUseCase generateGraphsUseCase;
  private final GenerateRandomRegexUseCase generateRandomRegexUseCase;

  @Autowired
  public GeneratorController(
      DownloadCodeFilesAsZipUseCase downloadCodeFilesAsZipUseCase,
      GenerateGraphsUseCase generateGraphsUseCase,
      GenerateRandomRegexUseCase generateRandomRegexUseCase) {
    this.downloadCodeFilesAsZipUseCase = downloadCodeFilesAsZipUseCase;
    this.generateGraphsUseCase = generateGraphsUseCase;
    this.generateRandomRegexUseCase = generateRandomRegexUseCase;
  }

  @Operation(summary = "Generate graphs", description = "Generate graphs")
  @ApiResponse(
      responseCode = "200",
      description = "Graphs detail response",
      content = @Content(schema = @Schema(implementation = AutomatonDetailResponse.class)))
  @PostMapping(value = GENERATE_GRAPHS_URI, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> generateGraphs(
      @RequestParam(value = PARAM_REGEX) String regex,
      @RequestBody RegexNotationTypeRequest request) {
    final RegexNotationTypeCommand command =
        RegexNotationCommandFactory.getRegexNotationTypeCommand(request);
    final AutomatonDetailResponse response =
        AutomatonResponseMapper.map(generateGraphsUseCase.execute(regex, command));

    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
  }

  @Operation(summary = "Generate codes", description = "Generate codes")
  @ApiResponse(
      responseCode = "200",
      description = "Codes detail response",
      content = @Content(schema = @Schema(implementation = CodeFilesZipData.class)))
  @PostMapping(value = GENERATE_CODES_URI, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> generateCodes(@RequestBody GenerateCodesRequest request) {
    final CodeFilesZipData codeFilesZipData =
        downloadCodeFilesAsZipUseCase.execute(
            LanguagesToDownloadCodeInCommandFactory.getLanguagesToDownloadCodeCommand(request));

    return ResponseEntity.ok()
        .header(CONTENT_DISPOSITION, "attachment; filename=" + codeFilesZipData.fileName())
        .contentType(MediaType.valueOf("application/zip"))
        .body(codeFilesZipData.fileContent());
  }

  @Operation(summary = "Generate random regex", description = "Generate random regex")
  @ApiResponse(
      responseCode = "200",
      description = "Random regex detail response",
      content = @Content(schema = @Schema(implementation = BaseRegexDto.class)))
  @PostMapping(value = GENERATE_RANDOM_REGEX_URI, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> generateRandomRegex(@RequestBody GenerateRandomRegexRequest request) {
    final RandomRegexCommand command = RandomRegexCommandFactory.getRandomRegexCommand(request);
    final BaseRegexDto response =
        RegexResponseMapper.map(generateRandomRegexUseCase.execute(command));

    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
  }
}
