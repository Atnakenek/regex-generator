package com.github.atnakenek.regexgenerator.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.atnakenek.regexgenerator.dto.RegexGenerateRequest;
import com.github.atnakenek.regexgenerator.dto.model.RegexModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/regex")
@Slf4j
public class RegexGeneratorController {

  @PostMapping(path = "/generate",
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE})
  public ResponseEntity<RegexModel> generateRegex(
      @RequestBody RegexGenerateRequest request) {
    RegexModel response = RegexModel.builder().regex("[A-Z]{2}\\d{3}[A-Z]{2}").build();
    return ResponseEntity.ok(response);
  }
}
