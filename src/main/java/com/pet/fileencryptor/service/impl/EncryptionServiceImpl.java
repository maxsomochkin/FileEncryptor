package com.pet.fileencryptor.service.impl;

import static com.pet.fileencryptor.util.ErrorCodeConstants.FAILED_TO_READ_IV;

import com.pet.fileencryptor.dto.request.FileEncryptionRequest;
import com.pet.fileencryptor.exception.FileEncryptorException;
import com.pet.fileencryptor.service.EncryptionService;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {

  private static final String TRANSFORMATION = "AES/CTR/NoPadding";

  private static final String ALGORITHM = "AES";

  private static int KEY_LENGTH = 32;

  private static final int BUFFER_CAPACITY = 8192;

  private final ExecutorService executorService =
      Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

  @Override
  public void processFile(FileEncryptionRequest request) {
    executorService.submit(() -> {
      try {
        var secretKey = createSecretKey(request.getEncryptionKey());
        Cipher cipher;

        if (request.isEncryptMode()) {
          cipher = initializeCipher(secretKey);
          processFileData(request, cipher, secretKey);
        } else {
          cipher = Cipher.getInstance(TRANSFORMATION);
          processFileData(request, cipher, secretKey);
        }
      } catch (Exception e) {
        throw new FileEncryptorException(e.getMessage());
      }
    });
  }

  private SecretKeySpec createSecretKey(String key) {
    var keyBytes = key.getBytes();
    var fullKey = new byte[KEY_LENGTH];

    System.arraycopy(keyBytes, 0, fullKey, 0, Math.min(keyBytes.length, fullKey.length));
    return new SecretKeySpec(fullKey, ALGORITHM);
  }

  private Cipher initializeCipher(SecretKeySpec secretKey)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
      InvalidKeyException {
    var cipher = Cipher.getInstance(TRANSFORMATION);
    var iv = new byte[cipher.getBlockSize()];
    new SecureRandom().nextBytes(iv);
    var ivSpec = new IvParameterSpec(iv);

    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
    return cipher;
  }

  private void processFileData(FileEncryptionRequest request, Cipher cipher,
      SecretKeySpec secretKey)
      throws IOException, InvalidAlgorithmParameterException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException {
    try (FileInputStream fis = new FileInputStream(request.getInputFilePath());
        FileOutputStream fos = new FileOutputStream(request.getOutputFilePath())) {

      if (request.isEncryptMode()) {
        var iv = cipher.getIV();
        fos.write(iv);
      } else {
        var iv = new byte[cipher.getBlockSize()];
        if (fis.read(iv) != iv.length) {
          throw new IOException(FAILED_TO_READ_IV);
        }
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
      }

      var buffer = new byte[BUFFER_CAPACITY];
      int bytesRead;
      while ((bytesRead = fis.read(buffer)) != -1) {
        var output = cipher.update(buffer, 0, bytesRead);
        if (output != null) {
          fos.write(output);
        }
      }

      var finalBytes = cipher.doFinal();
      if (finalBytes != null) {
        fos.write(finalBytes);
      }
    }
  }
}
