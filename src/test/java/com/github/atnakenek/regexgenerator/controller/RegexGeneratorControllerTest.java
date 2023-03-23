package com.github.atnakenek.regexgenerator.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.mapper.RegexGeneratorMapper;
import com.github.atnakenek.regexgenerator.resource.RegexModel;
import com.github.atnakenek.regexgenerator.service.RegexGeneratorService;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = RegexGeneratorController.class)
public class RegexGeneratorControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RegexGeneratorService regexGeneratorService;

  @MockBean
  private RegexGeneratorMapper regexGeneratorMapper;

  @Test
  public void when_italianCarPlatesStrings_then_regexIsReturned() throws Exception {
    when(regexGeneratorService.generate(any(CodesDTO.class))).thenReturn(
        Arrays.asList(new RegexDTO(), new RegexDTO(), new RegexDTO()));
    when(regexGeneratorMapper.toModel(anyList())).thenReturn(
        new RegexModel("[A-Z]{2}\\d{3}[A-Z]{2}"));
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content("{\"texts\": [\"AB123ZZ\",\"BB742TG\",\"CF678HG\"]}")
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{2}\\d{3}[A-Z]{2}"));

  }
}