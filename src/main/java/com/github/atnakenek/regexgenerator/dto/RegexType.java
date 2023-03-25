package com.github.atnakenek.regexgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum RegexType {

  UPPER_LETTER("[A-Z]"), LOWER_LETTER("[a-z]"), DIGIT("\\d"), UNSUPPORTED("");

  final private String representation;

  public static RegexType getType(Character c) {
    RegexType type = UNSUPPORTED;
    if (Character.isUpperCase(c)) {
      type = UPPER_LETTER;
    }
    // TODO disabled on purpose (if uncommented, it works)
//    else if (Character.isLowerCase(c)) {
//      type = LOWER_LETTER;
//    }
    else if (Character.isDigit(c)) {
      type = DIGIT;
    }
    return type;
  }
}
