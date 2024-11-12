package com.pet.fileencryptor.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pet.fileencryptor.service.EncryptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EncryptionController.class)
public class EncryptionControllerTest {

  private static final String BASE_URL = "/api/encryption/process";

  @Autowired
  protected MockMvc mockMvc;

  @MockBean
  private EncryptionService encryptionService;

  @Test
  void whenIsCorrectBody_thenSuccessHttpResponse() throws Exception {
    String postBody = "{"
        + "\"inputFilePath\":\"src/main/resources/testInput.txt\","
        + "\"outputFilePath\":\"src/main/resources/testOutput.txt\","
        + "\"encryptionKey\":\"CharacterEncryptionKeyExample12345\","
        + "\"isEncryptMode\":true"
        + "}";

    mockMvc.perform(post(BASE_URL).contentType("application/json")
            .content(postBody))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void whenIsEmptyInputFilePath_thenBadRequestHttpResponse() throws Exception {
    String postBody = "{"
        + "\"inputFilePath\":\"\","
        + "\"outputFilePath\":\"src/main/resources/testOutput.txt\","
        + "\"encryptionKey\":\"CharacterEncryptionKeyExample12345\","
        + "\"isEncryptMode\":true"
        + "}";

    mockMvc.perform(post(BASE_URL).contentType("application/json")
            .content(postBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void whenIsEmptyOutputFilePath_thenBadRequestHttpResponse() throws Exception {
    String postBody = "{"
        + "\"inputFilePath\":\"src/main/resources/testInput.txt\","
        + "\"outputFilePath\":\"\","
        + "\"encryptionKey\":\"CharacterEncryptionKeyExample12345\","
        + "\"isEncryptMode\":true"
        + "}";

    mockMvc.perform(post(BASE_URL).contentType("application/json")
            .content(postBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void whenEncryptionKeyIsShort_thenBadRequestHttpHttpResponse() throws Exception {
    String postBody = "{"
        + "\"inputFilePath\":\"src/main/resources/testInput.txt\","
        + "\"outputFilePath\":\"src/main/resources/testOutput.txt\","
        + "\"encryptionKey\":\"shortKey\","
        + "\"isEncryptMode\":true"
        + "}";

    mockMvc.perform(post(BASE_URL).contentType("application/json")
            .content(postBody))
        .andExpect(status().isBadRequest());
  }

  @Test
  void whenEncryptModeIsNull_thenBadRequestHttpHttpResponse() throws Exception {
    String postBody = "{"
        + "\"inputFilePath\":\"src/main/resources/testInput.txt\","
        + "\"outputFilePath\":\"src/main/resources/testOutput.txt\","
        + "\"encryptionKey\":\"CharacterEncryptionKeyExample12345\","
        + "\"isEncryptMode\":"
        + "}";

    mockMvc.perform(post(BASE_URL).contentType("application/json")
            .content(postBody))
        .andExpect(status().isBadRequest());
  }


}
