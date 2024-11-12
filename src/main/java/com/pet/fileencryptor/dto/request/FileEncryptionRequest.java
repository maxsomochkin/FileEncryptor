package com.pet.fileencryptor.dto.request;

import static com.pet.fileencryptor.util.ErrorCodeConstants.ENCRYPTION_KEY_IS_EMPTY;
import static com.pet.fileencryptor.util.ErrorCodeConstants.ENCRYPTION_KEY_IS_SMALL;
import static com.pet.fileencryptor.util.ErrorCodeConstants.ENCRYPTION_MODE_IS_INCORRECT;
import static com.pet.fileencryptor.util.ErrorCodeConstants.PATH_IS_EMPTY;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Builder
@Validated
@AllArgsConstructor
@Schema(description = "Request payload for file encryption or decryption")
public class FileEncryptionRequest {

  @NotBlank(message = PATH_IS_EMPTY)
  @Schema(description = "Path to save the output file", example = "src/main/resources/testInput.txt", requiredMode = REQUIRED)
  private String inputFilePath;

  @NotBlank(message = PATH_IS_EMPTY)
  @Schema(description = "Path to save the output file", example = "src/main/resources/testOutput.txt", requiredMode = REQUIRED)
  private String outputFilePath;

  @NotBlank(message = ENCRYPTION_KEY_IS_EMPTY)
  @Size(min = 32, message = ENCRYPTION_KEY_IS_SMALL)
  @Schema(description = "Encryption key (must be at least 32 characters)", example = "32CharacterEncryptionKeyExample12345", requiredMode = REQUIRED)
  private String encryptionKey;

  @NotNull(message = ENCRYPTION_MODE_IS_INCORRECT)
  @Schema(description = "Encrypt or decrypt mode: true for encryption, false for decryption", example = "true", requiredMode = REQUIRED)
  private boolean isEncryptMode;

}
