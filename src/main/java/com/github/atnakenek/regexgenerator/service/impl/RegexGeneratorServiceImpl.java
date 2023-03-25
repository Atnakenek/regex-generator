package com.github.atnakenek.regexgenerator.service.impl;

import com.github.atnakenek.regexgenerator.dto.CodesDTO;
import com.github.atnakenek.regexgenerator.dto.RegexDTO;
import com.github.atnakenek.regexgenerator.dto.RegexType;
import com.github.atnakenek.regexgenerator.exception.InvalidInputException;
import com.github.atnakenek.regexgenerator.exception.UnsupportedCharacterException;
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
    if (codeList == null || codeList.size() == 0) {
      throw new InvalidInputException();
    }
    // it is used to determine the shortest regex pattern of all strings
    int minPatternIdx = -1;
    for (String str : codeList) {
      int regexMapIdx = 0;
      if (str == null) {
        throw new InvalidInputException();
      }
      if (!str.isEmpty()) {
        RegexDTO currentRegex = getRegex(str, 0);
        for (int charIdx = 0; charIdx < str.length(); charIdx++) {
          if (currentRegex.getType() == RegexType.UNSUPPORTED) {
            throw new UnsupportedCharacterException(str.charAt(charIdx));
          }
          RegexDTO nextRegex = getRegex(str, charIdx + 1);
          if (currentRegex.equals(nextRegex)) {
            currentRegex.addOccurrence();
          } else {
            // next char is different, so add/merge this to map
            regexMapIdx = addRegex(currentRegex, regexMap, regexMapIdx) + 1;
            currentRegex = nextRegex;
          }
        }
      }
      minPatternIdx = minPatternIdx == -1 ? regexMap.size() : Math.min(minPatternIdx, regexMapIdx);
    }
    // longer regexes must be optional
    setOptionalRegexes(regexMap, minPatternIdx);
    return new ArrayList<>(regexMap.values());
  }

  private RegexDTO getRegex(String str, int index) {
    RegexDTO regex = null;
    if (index < str.length()) {
      RegexType type = RegexType.getType(str.charAt(index));
      regex = new RegexDTO(type);
    }
    return regex;
  }

  private int addRegex(RegexDTO regex, Map<Integer, RegexDTO> regexMap, int mapIndex) {
    boolean isMapped = false;
    while (!isMapped) {
      RegexDTO mappedRegex = regexMap.putIfAbsent(mapIndex, regex);
      // when null, it means empty bucket and successful insertion
      if (mappedRegex == null) {
        isMapped = true;
      } else if (!regex.equals(mappedRegex)) {
        // if in the current bucket a different regex is already present, min occurrences must be 0
        mappedRegex.setMinOccurrences(0);
        //and try next bucket
        ++mapIndex;
      } else {
        // if they match, set min/max between mapped min/max and current occurrences
        mappedRegex.adjustMinMax(regex.getMaxOccurrences());
        isMapped = true;
      }
    }
    // return updated map index (when regex types collide)
    return mapIndex;
  }

  private void setOptionalRegexes(Map<Integer, RegexDTO> regexMap, int startIndex) {
    regexMap.entrySet()
        .stream()
        .filter(e -> e.getKey() >= startIndex)
        .map(Entry::getValue)
        .forEach(r -> r.setMinOccurrences(0));
  }
}
