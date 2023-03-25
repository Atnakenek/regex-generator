package com.github.atnakenek.regexgenerator.controller;

import static com.github.atnakenek.regexgenerator.exception.ExceptionsHandler.UNSUPPORTED_CHARACTER_USED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(ReplaceUnderscores.class)
@TestMethodOrder(OrderAnnotation.class)
public class RegexGeneratorControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Order(1)
  public void Given_strings_of_italian_car_plates_then_regex_is_returned() throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("AB123ZZ", "BB742TG", "CF678HG"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{2}\\d{3}[A-Z]{2}"));
  }

  @Test
  @Order(2)
  public void Given_strings_of_italian_fiscal_codes_then_regex_is_returned() throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("TNTTST80A01F205E", "LVRRGL43E04Z403R"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]"));
  }

  @Test
  @Order(3)
  public void Given_strings_of_uppercased_letters_and_digit_of_increasing_size_when_each_type_matches_at_least_two_in_all_strings_then_regex_of_varying_but_mandatory_letters_or_digits_is_returned()
      throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("AA123", "BA1234", "BA12345"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{2}\\d{3,5}"));
  }

  @Test
  @Order(4)
  public void Given_strings_of_uppercased_letters_and_digit_of_increasing_size_when_each_type_matches_at_least_one_in_all_strings_then_regex_of_varying_but_mandatory_letters_or_digits_is_returned()
      throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("A123XY", "BA1234ZT", "AB12345B"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{1,2}\\d{3,5}[A-Z]{1,2}"));
  }

  @Test
  @Order(5)
  public void Given_strings_of_uppercased_letters_and_digit_of_increasing_size_when_each_type_does_not_match_at_least_one_in_all_strings_then_regex_of_optional_and_varying_letters_or_digits_is_returned()
      throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("AA123", "111111", "AB23345"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{0,2}\\d{3,6}"));
  }

  @Test
  @Order(6)
  public void Given_strings_of_uppercased_letters_and_digit_of_mixed_size_then_regex_of_optional_and_varying_letters_or_digits_is_returned()
      throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("11AA", "AB23345CC", "A1A1", "B11"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("\\d{0,2}[A-Z]{1,2}\\d{0,5}[A-Z]{0,2}\\d{0,1}"));
  }

  @Test
  @Order(7)
  public void Given_strings_of_uppercased_letters_and_digit_of_mixed_size_when_empty_string_is_present_then_regex_of_all_optional_letters_or_digits_is_returned()
      throws Exception {
    CodesDTO request = new CodesDTO(
        Arrays.asList("AAA111ABC", "BBB22222AAAA", "BB22BB", "AA1234CC", ""));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{0,3}\\d{0,5}[A-Z]{0,4}"));
  }

  @Test
  @Order(8)
  public void Given_no_strings_then_InvalidInputException_is_thrown()
      throws Exception {
    CodesDTO request = new CodesDTO(null);
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("Request is invalid."));

    request = new CodesDTO(Collections.emptyList());
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("Request is invalid."));
  }

  @Test
  @Order(9)
  public void Given_strings_of_uppercased_letters_and_digit_when_null_string_is_present_then_bad_request_is_returned()
      throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("AAA111ABC", null, "BB22BB"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("Request is invalid."));
  }

  @Test
  @Order(10)
  public void Given_strings_containing_special_character_then_UnsupportedCharacterException_is_thrown()
      throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("AAA111ABC", "AA@1", "BB22BB"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isPreconditionFailed())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value("PRECONDITION_FAILED"))
        .andExpect(jsonPath("$.message").value(UNSUPPORTED_CHARACTER_USED + "@"));
  }

  @Test
  @Order(11)
  public void Given_malformed_request_then_bad_request_is_returned()
      throws Exception {
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content("{codes}")
            .accept(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("Request is invalid."));
  }
}