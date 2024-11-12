package com.pet.fileencryptor.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.pet.fileencryptor.dto.response.ErrorResponseDto;
import java.time.Instant;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<ErrorResponseDto> processException(String message) {
    log.error(message);
    ErrorResponseDto error = buildErrorResponseDto(message);
    return ResponseEntity.status(BAD_REQUEST.value()).body(error);
  }

  private ErrorResponseDto buildErrorResponseDto(String message) {
    return ErrorResponseDto.builder()
        .message(message)
        .timestamp(Instant.now())
        .build();
  }

  @ExceptionHandler({Exception.class, NoSuchPaddingException.class})
  protected ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
    String message = ex.getMessage();
    return processException(message);
  }

}
