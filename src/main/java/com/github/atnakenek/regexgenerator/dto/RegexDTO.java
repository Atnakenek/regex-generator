package com.github.atnakenek.regexgenerator.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "type")
@RequiredArgsConstructor
@AllArgsConstructor
public class RegexDTO {

  @NonNull
  private RegexType type;
  private int minOccurrences = 1;
  private int maxOccurrences = 1;

  public void addOccurrence() {
    ++minOccurrences;
    ++maxOccurrences;
  }

  public void adjustMinMax(int occurrences) {
    minOccurrences = Math.min(minOccurrences, occurrences);
    maxOccurrences = Math.max(this.maxOccurrences, occurrences);
  }
}
