package com.github.atnakenek.regexgenerator.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnsupportedCharacterException extends RuntimeException {

  private char unsupportedChar;
}
