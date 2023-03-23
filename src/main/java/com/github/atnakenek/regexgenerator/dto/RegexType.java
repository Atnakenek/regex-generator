package com.github.atnakenek.regexgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RegexType {

  LETTER("[A-Z]"), DIGIT("\\d");

  final private String representation;
  public static RegexType getType(Character c) {
    return Character.isLetter(c) ? LETTER : DIGIT;
  }
}
