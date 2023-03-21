package com.github.atnakenek.regexgenerator.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RegexGeneratorController.class)
public class RegexGeneratorControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  public void when_italianCarPlatesStrings_then_regexIsGiven() throws Exception {
    mockMvc.perform(post("/api/regex/generate")
            .contentType(APPLICATION_JSON)
            .content("{\"texts\": [\"AB123ZZ\",\"BB742TG\",\"CF678HG\"]}")
            .accept(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.regex").value("[A-Z]{2}\\d{3}[A-Z]{2}"));

  }
}