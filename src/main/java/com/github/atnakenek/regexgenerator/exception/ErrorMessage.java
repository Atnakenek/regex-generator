package com.github.atnakenek.regexgenerator.exception;

import java.time.LocalDateTime;
import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorMessage {

  private LocalDateTime timestamp;
  private HttpStatus status;
  private String message;
}
