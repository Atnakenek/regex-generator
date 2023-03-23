package com.github.atnakenek.regexgenerator.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class RegexGeneratorControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void when_italianCarPlatesStrings_then_regexIsReturned() throws Exception {
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content("{\"codes\": [\"AB123ZZ\",\"BB742TG\",\"CF678HG\"]}")
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{2}\\d{3}[A-Z]{2}"));
  }
}