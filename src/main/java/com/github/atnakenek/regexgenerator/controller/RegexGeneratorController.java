package com.github.atnakenek.regexgenerator.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.mapper.RegexGeneratorMapper;
import com.github.atnakenek.regexgenerator.resource.RegexModel;
import com.github.atnakenek.regexgenerator.service.RegexGeneratorService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/regex")
@Slf4j
public class RegexGeneratorController {

  @Autowired
  RegexGeneratorService regexGeneratorService;

  @Autowired
  RegexGeneratorMapper regexGeneratorMapper;

  @PostMapping(path = "/generate",
      consumes = {APPLICATION_JSON_VALUE},
      produces = {APPLICATION_JSON_VALUE})
  public ResponseEntity<RegexModel> generateRegex(
      @RequestBody CodesDTO request) {
    List<RegexDTO> regexList = regexGeneratorService.generate(request);
    return ResponseEntity.ok(regexGeneratorMapper.toModel(regexList));
  }
}
