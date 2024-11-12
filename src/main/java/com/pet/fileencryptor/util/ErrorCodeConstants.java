package com.pet.fileencryptor.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorCodeConstants {
  public static final String PATH_IS_EMPTY = "Path to file is empty";
  public static final String ENCRYPTION_KEY_IS_EMPTY = "Encryption key is empty";
  public static final String ENCRYPTION_KEY_IS_SMALL = "Encryption key is less than 32 symbols";
  public static final String ENCRYPTION_MODE_IS_INCORRECT = "Encryption mode is incorrect";
  public static final String FAILED_TO_READ_IV = "Failed to read IV from file";

}
