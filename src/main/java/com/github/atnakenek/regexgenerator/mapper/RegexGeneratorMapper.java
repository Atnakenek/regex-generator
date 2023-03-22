package com.github.atnakenek.regexgenerator.mapper;

import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.resource.RegexModel;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegexGeneratorMapper {

  default RegexModel toModel(List<RegexDTO> obj) {
    RegexModel regexModel = new RegexModel();
    regexModel.setRegex("[A-Z]{2}\\d{3}[A-Z]{2}");
    return regexModel;
  }

}
