package com.github.atnakenek.regexgenerator.service.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class RegexGeneratorServiceImplTest {

  @InjectMocks
  RegexGeneratorServiceImpl regexGeneratorService;

  @Test
  void when_italianCarPlatesStrings_then_regexIsConvertedToListOfRegexElements() {

    CodesDTO codesDTO = new CodesDTO(Arrays.asList("AB123ZZ", "BB742TG", "CF678HG"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate).isNotNull().hasSize(3);
  }

}