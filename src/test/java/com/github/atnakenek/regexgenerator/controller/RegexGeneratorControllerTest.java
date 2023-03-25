package com.github.atnakenek.regexgenerator.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.dto.RegexType;
import com.github.atnakenek.regexgenerator.exception.ExceptionsHandler;
import com.github.atnakenek.regexgenerator.exception.InvalidInputException;
import com.github.atnakenek.regexgenerator.mapper.RegexGeneratorMapper;
import com.github.atnakenek.regexgenerator.resource.RegexModel;
import com.github.atnakenek.regexgenerator.service.RegexGeneratorService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RegexGeneratorController.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
public class RegexGeneratorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RegexGeneratorService regexGeneratorService;

  @MockBean
  private RegexGeneratorMapper regexGeneratorMapper;

  @SpyBean
  private ExceptionsHandler exceptionsHandler;

  @Captor
  private ArgumentCaptor<CodesDTO> codesCaptor;

  @Captor
  private ArgumentCaptor<List<RegexDTO>> regexListCaptor;


  @Test
  public void Given_strings_then_service_and_mapper_are_called_with_proper_args_and_regex_is_returned()
      throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("AB123ZZ", "BB742TG", "CF678HG"));
    List<RegexDTO> listRegexDTO = Arrays.asList(new RegexDTO(RegexType.UPPER_LETTER, 2, 2),
        new RegexDTO(RegexType.DIGIT, 3, 3),
        new RegexDTO(RegexType.UPPER_LETTER, 2, 2));
    RegexModel response = new RegexModel("[A-Z]{2}\\d{3}[A-Z]{2}");

    when(regexGeneratorService.generate(any(CodesDTO.class))).thenReturn(listRegexDTO);
    when(regexGeneratorMapper.toModel(eq(listRegexDTO))).thenReturn(response);

    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{2}\\d{3}[A-Z]{2}"));

    verify(regexGeneratorService).generate(codesCaptor.capture());
    assertThat(codesCaptor.getValue().getCodes()).containsExactlyElementsOf(request.getCodes());
    verify(regexGeneratorMapper).toModel(regexListCaptor.capture());
    assertThat(regexListCaptor.getValue()).containsExactlyElementsOf(listRegexDTO);

  }

  @Test
  public void When_InvalidInputException_is_thrown_then_bad_request_is_returned() throws Exception {
    CodesDTO request = new CodesDTO(Arrays.asList("AB123ZZ", null, "CF678HG"));

    when(regexGeneratorService.generate(any(CodesDTO.class))).thenThrow(
        new InvalidInputException());

    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .accept(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("Request is invalid."));

    verify(regexGeneratorService).generate(codesCaptor.capture());
    assertThat(codesCaptor.getValue().getCodes()).containsExactlyElementsOf(request.getCodes());
    verify(regexGeneratorMapper, times(0)).toModel(any());
  }
}