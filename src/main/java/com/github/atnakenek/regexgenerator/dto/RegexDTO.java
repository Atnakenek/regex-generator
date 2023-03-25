package com.github.atnakenek.regexgenerator.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "type")
@NoArgsConstructor
@AllArgsConstructor
public class RegexDTO {

  private RegexType type;
  private int min = 0;
  private int max = 0;
}
