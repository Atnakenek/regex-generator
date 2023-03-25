package com.github.atnakenek.regexgenerator.dto;

import java.util.List;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodesDTO {

  private List<String> codes;
}
