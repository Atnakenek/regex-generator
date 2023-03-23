package com.github.atnakenek.regexgenerator.service.impl;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.dto.RegexType;
import com.github.atnakenek.regexgenerator.service.RegexGeneratorService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;

@Service
public class RegexGeneratorServiceImpl implements RegexGeneratorService {

  @Override
  public List<RegexDTO> generate(CodesDTO codesDTO) {
    Map<Integer, RegexDTO> regexMap = new LinkedHashMap<>();
    List<String> codeList = codesDTO.getCodes();
    int minIdx = -1;
    for (String str : codeList) {
      int idx = 0;
      if (str != null && !str.isEmpty()) {
        int occurrences = 1;
        RegexType currentType = RegexType.getType(str.charAt(0));
        for (int j = 0; j < str.length(); j++) {
          RegexType nextType = getNextType(str, j);
          if (currentType == nextType) {
            ++occurrences;
          } else {
            // add this regex with its occurrences number since next one has different type
            idx = placeRegex(regexMap, idx, occurrences, currentType) + 1;
            currentType = nextType;
            occurrences = 1;
          }
        }
      }
      // determine lowest pattern: longer patterns must be optional
      minIdx = minIdx == -1 ? regexMap.size() : Math.min(minIdx, idx);
    }
    setLongerPatternsOptional(regexMap, minIdx);
    return new ArrayList<>(regexMap.values());
  }

  private RegexType getNextType(String str, int index) {
    boolean isLastChar = index == str.length() - 1;
    return isLastChar ? null : RegexType.getType(str.charAt(index + 1));
  }

  private int placeRegex(Map<Integer, RegexDTO> regexMap, int index, int occurrences,
      RegexType type) {
    RegexDTO regex = buildRegex(occurrences, type);
    RegexDTO savedRegex = regexMap.computeIfAbsent(index, k -> regex);
    // if type does not match, minimum occurrences must be 0
    if (savedRegex.getType() != regex.getType()) {
      savedRegex.setMin(0);
      // insert regex into the next bucket. if present, type is guaranteed to be of the same type
      savedRegex = regexMap.computeIfAbsent(++index, k -> regex);
    }
    savedRegex.setMin(Math.min(savedRegex.getMin(), regex.getMin()));
    savedRegex.setMax(Math.max(savedRegex.getMax(), regex.getMax()));
    return index;
  }

  private RegexDTO buildRegex(int occurrences, RegexType regexType) {
    return RegexDTO.builder()
        .type(regexType)
        .min(occurrences)
        .max(occurrences)
        .build();
  }

  private void setLongerPatternsOptional(Map<Integer, RegexDTO> regexMap, int index) {
    regexMap.entrySet()
        .stream()
        .filter(e -> e.getKey() >= index)
        .map(Entry::getValue)
        .forEach(r -> r.setMin(0));
  }
}
