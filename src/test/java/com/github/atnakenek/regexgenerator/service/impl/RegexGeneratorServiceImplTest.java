package com.github.atnakenek.regexgenerator.service.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.dto.RegexType;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class RegexGeneratorServiceImplTest {

  @InjectMocks
  private RegexGeneratorServiceImpl regexGeneratorService;

  @Test
  void given_stringsOfItalianCarPlates_then_regexIsReturned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("AB123ZZ", "BB742TG", "CF678HG"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(3)
        .extracting("type", "min", "max").containsExactly(
            Tuple.tuple(RegexType.LETTER, 2, 2),
            Tuple.tuple(RegexType.DIGIT, 3, 3),
            Tuple.tuple(RegexType.LETTER, 2, 2));
  }

  @Test
  void given_stringsOfItalianFiscalCodes_then_regexIsReturned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("TNTTST80A01F205E", "LVRRGL43E04Z403R"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(7)
        .extracting("type", "min", "max").containsExactly(
            Tuple.tuple(RegexType.LETTER, 6, 6),
            Tuple.tuple(RegexType.DIGIT, 2, 2),
            Tuple.tuple(RegexType.LETTER, 1, 1),
            Tuple.tuple(RegexType.DIGIT, 2, 2),
            Tuple.tuple(RegexType.LETTER, 1, 1),
            Tuple.tuple(RegexType.DIGIT, 3, 3),
            Tuple.tuple(RegexType.LETTER, 1, 1));
  }

  @Test
  void given_stringsOfDifferentSizeButSamePattern_then_regexIsReturned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("AA123", "BA1234", "BA12345"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(2)
        .extracting("type", "min", "max").containsExactly(
            Tuple.tuple(RegexType.LETTER, 2, 2),
            Tuple.tuple(RegexType.DIGIT, 3, 5));
  }

  @Test
  void given_stringsOfDifferentSizeButAscending_then_regexIsReturned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("A123XY", "BA1234ZT", "AB12345B"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(3)
        .extracting("type", "min", "max").containsExactly(
            Tuple.tuple(RegexType.LETTER, 1, 2),
            Tuple.tuple(RegexType.DIGIT, 3, 5),
            Tuple.tuple(RegexType.LETTER, 1, 2));
  }

  @Test
  void given_stringsWithLetterAndNumberOnSamePosition_then_aRegexWithMinZeroIsReturned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("AA123", "111111", "AB23345"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(2)
        .extracting("type", "min", "max").containsExactly(
            Tuple.tuple(RegexType.LETTER, 0, 2),
            Tuple.tuple(RegexType.DIGIT, 3, 6));
  }

  void given_stringsWithMixedSize_then_aRegexWithMinZeroOnLongerPatternsIsReturned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("11AA", "AB23345CC", "A1A1","B11"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(5)
        .extracting("type", "min", "max").containsExactly(
            Tuple.tuple(RegexType.DIGIT, 0, 2),
            Tuple.tuple(RegexType.LETTER, 1, 2),
            Tuple.tuple(RegexType.DIGIT, 0, 5),
            Tuple.tuple(RegexType.LETTER, 0, 2),
            Tuple.tuple(RegexType.DIGIT, 0, 1));
  }

  @Test
  void given_stringsWithEmptyString_then_aRegexWithMinZeroEverywhereIsReturned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("AAA111ABC", "BBB22222AAAA", "BB22BB","AA1234CC", ""));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(3)
        .extracting("type", "min", "max").containsExactly(
            Tuple.tuple(RegexType.LETTER, 0, 3),
            Tuple.tuple(RegexType.DIGIT, 0, 5),
            Tuple.tuple(RegexType.LETTER, 0, 4));
  }

}