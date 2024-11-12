package com.pet.fileencryptor.exception;

import lombok.Getter;

@Getter
public class FileEncryptorException extends RuntimeException {

  public FileEncryptorException(String message) {
    super(message);
  }

}