package com.github.atnakenek.regexgenerator.service.impl;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.service.RegexGeneratorService;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RegexGeneratorServiceImpl implements RegexGeneratorService {

  @Override
  public List<RegexDTO> generate(CodesDTO codesDTO) {

    return Arrays.asList(new RegexDTO(), new RegexDTO(), new RegexDTO());
  }
}
