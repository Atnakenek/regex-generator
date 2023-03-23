package com.github.atnakenek.regexgenerator.mapper;

import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.resource.RegexModel;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegexGeneratorMapper {

  default RegexModel toModel(List<RegexDTO> obj) {
    RegexModel regexModel = RegexModel.builder().build();
    if (obj != null) {
      String regex = obj.stream()
          .map(r -> r.getType().getRepresentation() + mapOccurrences(r.getMin(), r.getMax()))
          .collect(Collectors.joining());
      regexModel.setRegex(regex);
    }
    return regexModel;
  }

  default String mapOccurrences(int min, int max) {
    String occurrences = StringUtils.EMPTY;
    if (min != 1 || max != 1) {
      StringJoiner stringJoiner = new StringJoiner(",", "{", "}");
      stringJoiner.add(String.valueOf(min));
      if (max != min) {
        stringJoiner.add(String.valueOf(max));
      }
      occurrences = stringJoiner.toString();
    }
    return occurrences;
  }


}
