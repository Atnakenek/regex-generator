package com.github.atnakenek.regexgenerator.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

import java.time.LocalDateTime;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

  public static final String UNSUPPORTED_CHARACTER_USED = "Unsupported character used: ";

  @ExceptionHandler(value = InvalidInputException.class)
  protected ResponseEntity<Object> handleException(InvalidInputException ex, WebRequest request) {
    return handleExceptionInternal(ex, getBadRequestErrorMessage(),
        new HttpHeaders(), BAD_REQUEST, request);

  }

  @ExceptionHandler(value = UnsupportedCharacterException.class)
  protected ResponseEntity<Object> handleException(UnsupportedCharacterException ex,
      WebRequest request) {
    HttpStatus status = PRECONDITION_FAILED;
    ErrorMessage errorMessage = ErrorMessage.builder()
        .timestamp(LocalDateTime.now())
        .status(status)
        .message(UNSUPPORTED_CHARACTER_USED + ex.getUnsupportedChar())
        .build();
    return handleExceptionInternal(ex, errorMessage,
        new HttpHeaders(), status, request);

  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    return handleExceptionInternal(ex, getBadRequestErrorMessage(),
        new HttpHeaders(), BAD_REQUEST, request);
  }

  private static ErrorMessage getBadRequestErrorMessage() {
    return ErrorMessage.builder()
        .timestamp(LocalDateTime.now())
        .status(BAD_REQUEST)
        .message("Request is invalid.")
        .build();
  }
}
