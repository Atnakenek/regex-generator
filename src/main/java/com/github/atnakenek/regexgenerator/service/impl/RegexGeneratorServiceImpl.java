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
    int minIdx = -1;
    for (String str : codeList) {
      int regexIdx = 0;
      if (str == null) {
        throw new InvalidInputException();
      }
      if (!str.isEmpty()) {
        int occurrences = 1;
        RegexType currentType = RegexType.getType(str.charAt(0));
        for (int charIdx = 0; charIdx < str.length(); charIdx++) {
          if (currentType == RegexType.UNSUPPORTED) {
            throw new UnsupportedCharacterException(str.charAt(charIdx));
          }
          RegexType nextType = getNextType(str, charIdx);
          if (currentType == nextType) {
            ++occurrences;
          } else {
            // next char is different, so add/merge this type to map
            regexIdx = placeRegex(regexMap, regexIdx, occurrences, currentType) + 1;
            currentType = nextType;
            occurrences = 1;
          }
        }
      }
      minIdx = minIdx == -1 ? regexMap.size() : Math.min(minIdx, regexIdx);
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
    boolean isMapped = false;
    while (!isMapped) {
      RegexDTO mappedRegex = regexMap.putIfAbsent(index, regex);
      // when null, it means empty bucket and successful insertion
      if (mappedRegex == null) {
        isMapped = true;
      } else if (mappedRegex.getType() != regex.getType()) {
        // if already present but type does not match, minimum occurrences must be 0
        mappedRegex.setMin(0);
        //and try next bucket
        ++index;
      } else {
        // if they match, set min and max between those
        mappedRegex.setMin(Math.min(mappedRegex.getMin(), regex.getMin()));
        mappedRegex.setMax(Math.max(mappedRegex.getMax(), regex.getMax()));
        isMapped = true;
      }
    }
    return index;
  }

  private RegexDTO buildRegex(int occurrences, RegexType regexType) {
    return RegexDTO.builder().type(regexType).min(occurrences).max(occurrences).build();
  }

  private void setLongerPatternsOptional(Map<Integer, RegexDTO> regexMap, int index) {
    regexMap.entrySet().stream().filter(e -> e.getKey() >= index).map(Entry::getValue)
        .forEach(r -> r.setMin(0));
  }
}
