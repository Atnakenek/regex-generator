package com.github.atnakenek.regexgenerator.service;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import java.util.List;

public interface RegexGeneratorService {

  List<RegexDTO> generate(CodesDTO codesDTO);
}
