package com.github.atnakenek.regexgenerator.service.impl;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.dto.RegexType;
import com.github.atnakenek.regexgenerator.exception.InvalidInputException;
import com.github.atnakenek.regexgenerator.exception.UnsupportedCharacterException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@TestMethodOrder(OrderAnnotation.class)
class RegexGeneratorServiceImplTest {

  public static final String TYPE = "type";
  public static final String MIN = "min";
  public static final String MAX = "max";
  @InjectMocks
  private RegexGeneratorServiceImpl regexGeneratorService;

  @Test
  @Order(1)
  void Given_strings_of_italian_car_plates_then_regex_is_returned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("AB123ZZ", "BB742TG", "CF678HG"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(3)
        .extracting(TYPE, MIN, MAX)
        .containsExactly(
            Tuple.tuple(RegexType.UPPER_LETTER, 2, 2),
            Tuple.tuple(RegexType.DIGIT, 3, 3),
            Tuple.tuple(RegexType.UPPER_LETTER, 2, 2));
  }

  @Test
  @Order(2)
  void Given_strings_of_italian_fiscal_codes_then_regex_is_returned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("TNTTST80A01F205E", "LVRRGL43E04Z403R"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(7)
        .extracting(TYPE, MIN, MAX)
        .containsExactly(
            Tuple.tuple(RegexType.UPPER_LETTER, 6, 6),
            Tuple.tuple(RegexType.DIGIT, 2, 2),
            Tuple.tuple(RegexType.UPPER_LETTER, 1, 1),
            Tuple.tuple(RegexType.DIGIT, 2, 2),
            Tuple.tuple(RegexType.UPPER_LETTER, 1, 1),
            Tuple.tuple(RegexType.DIGIT, 3, 3),
            Tuple.tuple(RegexType.UPPER_LETTER, 1, 1));
  }

  @Test
  @Order(3)
  void Given_strings_of_uppercased_letters_and_digit_of_increasing_size_when_each_type_matches_at_least_two_in_all_strings_then_regex_of_varying_but_mandatory_letters_or_digits_is_returned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("AA123", "BA1234", "BA12345"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(2)
        .extracting(TYPE, MIN, MAX)
        .containsExactly(
            Tuple.tuple(RegexType.UPPER_LETTER, 2, 2),
            Tuple.tuple(RegexType.DIGIT, 3, 5));
  }

  @Test
  @Order(4)
  void Given_strings_of_uppercased_letters_and_digit_of_increasing_size_when_each_type_matches_at_least_one_in_all_strings_then_regex_of_varying_but_mandatory_letters_or_digits_is_returned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("A123XY", "BA1234ZT", "AB12345B"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(3)
        .extracting(TYPE, MIN, MAX)
        .containsExactly(
            Tuple.tuple(RegexType.UPPER_LETTER, 1, 2),
            Tuple.tuple(RegexType.DIGIT, 3, 5),
            Tuple.tuple(RegexType.UPPER_LETTER, 1, 2));
  }

  @Test
  @Order(5)
  void Given_strings_of_uppercased_letters_and_digit_of_increasing_size_when_each_type_does_not_match_at_least_one_in_all_strings_then_regex_of_optional_and_varying_letters_or_digits_is_returned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("AA123", "111111", "AB23345"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(2)
        .extracting(TYPE, MIN, MAX)
        .containsExactly(
            Tuple.tuple(RegexType.UPPER_LETTER, 0, 2),
            Tuple.tuple(RegexType.DIGIT, 3, 6));
  }

  @Test
  @Order(6)
  void Given_strings_of_uppercased_letters_and_digit_of_mixed_size_then_regex_of_optional_and_varying_letters_or_digits_is_returned() {
    CodesDTO codesDTO = new CodesDTO(Arrays.asList("11AA", "AB23345CC", "A1A1", "B11"));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(5)
        .extracting(TYPE, MIN, MAX)
        .containsExactly(
            Tuple.tuple(RegexType.DIGIT, 0, 2),
            Tuple.tuple(RegexType.UPPER_LETTER, 1, 2),
            Tuple.tuple(RegexType.DIGIT, 0, 5),
            Tuple.tuple(RegexType.UPPER_LETTER, 0, 2),
            Tuple.tuple(RegexType.DIGIT, 0, 1));
  }

  @Test
  @Order(7)
  void Given_strings_of_uppercased_letters_and_digit_of_mixed_size_when_empty_string_is_present_then_regex_of_all_optional_letters_or_digits_is_returned() {
    CodesDTO codesDTO = new CodesDTO(
        Arrays.asList("AAA111ABC", "BBB22222AAAA", "BB22BB", "AA1234CC", ""));
    List<RegexDTO> generate = regexGeneratorService.generate(codesDTO);
    assertThat(generate)
        .isNotNull()
        .hasSize(3)
        .extracting(TYPE, MIN, MAX).containsExactly(
            Tuple.tuple(RegexType.UPPER_LETTER, 0, 3),
            Tuple.tuple(RegexType.DIGIT, 0, 5),
            Tuple.tuple(RegexType.UPPER_LETTER, 0, 4));
  }

  @Test
  @Order(8)
  void Given_no_strings_then_InvalidInputException_is_thrown() {
    CodesDTO codesDTOEmtpy = new CodesDTO(Collections.emptyList());
    assertThrows(InvalidInputException.class, () -> regexGeneratorService.generate(codesDTOEmtpy));
    CodesDTO codesDTONull = new CodesDTO(null);
    assertThrows(InvalidInputException.class, () -> regexGeneratorService.generate(codesDTONull));
  }

  @Test
  @Order(9)
  void Given_strings_of_uppercased_letters_and_digit_when_null_string_is_present_then_InvalidInputException_is_thrown() {
    CodesDTO codesDTO = new CodesDTO(
        Arrays.asList("AAA111ABC", null, "BB22BB"));
    assertThrows(InvalidInputException.class, () -> regexGeneratorService.generate(codesDTO));
  }

  @Test
  @Order(10)
  void Given_strings_containing_special_character_then_UnsupportedCharacterException_is_thrown() {
    CodesDTO codesDTO = new CodesDTO(
        Arrays.asList("AAA111ABC", "AA@1", "BB22BB"));
    assertThrows(UnsupportedCharacterException.class,
        () -> regexGeneratorService.generate(codesDTO));
  }

}