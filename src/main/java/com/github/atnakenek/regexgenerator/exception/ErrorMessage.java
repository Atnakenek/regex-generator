package com.github.atnakenek.regexgenerator.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorMessage {

  private LocalDateTime timestamp;
  private HttpStatus status;
  private String message;
}
