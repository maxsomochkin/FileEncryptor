package com.pet.fileencryptor.service;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pet.fileencryptor.dto.request.FileEncryptionRequest;
import com.pet.fileencryptor.service.impl.EncryptionServiceImpl;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileEncryptorApplicationTests {

  private final String ENCRYPTION_KEY = "12345678901234567890123456789012";
  private String inputFilePath;
  private String outputFilePath;
  private String decryptedFilePath;

  FileEncryptionRequest request;

  @InjectMocks
  private EncryptionServiceImpl subject;


  @BeforeEach
  void setUp() throws Exception {
    inputFilePath = Files.createTempFile("testInput", ".txt").toString();
    outputFilePath = Files.createTempFile("testOutput", ".txt").toString();
    decryptedFilePath = Files.createTempFile("testDecrypted", ".txt").toString();

    Files.writeString(Path.of(inputFilePath), "Test content for encryption",
        StandardOpenOption.WRITE);

    request = FileEncryptionRequest.builder()
        .encryptionKey(ENCRYPTION_KEY)
        .inputFilePath(inputFilePath)
        .outputFilePath(outputFilePath)
        .isEncryptMode(true)
        .build();


  }

  @AfterEach
  void cleanUp() throws Exception {
    Files.deleteIfExists(Path.of(inputFilePath));
    Files.deleteIfExists(Path.of(request.getOutputFilePath()));
    Files.deleteIfExists(Path.of(decryptedFilePath));
  }

  @Test
  void processFile_encryptsSuccessfully() {

    subject.processFile(request);

    await().atMost(2, SECONDS).untilAsserted(() -> {
      subject.processFile(request);

      var encryptedFile = new File(request.getOutputFilePath());

      assertTrue(encryptedFile.exists());
      assertTrue(encryptedFile.length() > 0);
    });
  }

  @Test
  void processFile_decryptsSuccessfully() {
    subject.processFile(request);

    await().atMost(2, SECONDS).untilAsserted(() -> {
      subject.processFile(request);
      request.setInputFilePath(outputFilePath);
      request.setOutputFilePath(decryptedFilePath);
      request.setEncryptMode(false);
    });

    subject.processFile(request);

    await().atMost(2, SECONDS).untilAsserted(() -> {
      subject.processFile(request);

      var decryptedBytes = Files.readAllBytes(Path.of(decryptedFilePath));
      var decryptedContent = new String(decryptedBytes, StandardCharsets.UTF_8);

      assertEquals("Test content for encryption", decryptedContent);
    });
  }
}


