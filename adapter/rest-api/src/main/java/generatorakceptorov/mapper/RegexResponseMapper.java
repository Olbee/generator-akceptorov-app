package generatorakceptorov.mapper;

import generatorakceptorov.domain.regex.data.RegexData;
import generatorakceptorov.restapi.dto.regex.BaseRegexDto;
import generatorakceptorov.restapi.dto.regex.ImmutableBaseRegexDto;

public final class RegexResponseMapper {

  public static BaseRegexDto map(RegexData data) {
    return ImmutableBaseRegexDto.builder().regex(data.regex()).build();
  }
}
