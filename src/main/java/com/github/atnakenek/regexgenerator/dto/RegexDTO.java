package com.github.atnakenek.regexgenerator.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of="type")
@NoArgsConstructor
@AllArgsConstructor
public class RegexDTO {

  private RegexType type;
  int min = 0;
  int max = 0;

}
