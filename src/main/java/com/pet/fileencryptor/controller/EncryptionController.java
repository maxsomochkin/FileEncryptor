package com.pet.fileencryptor.controller;

import com.pet.fileencryptor.dto.request.FileEncryptionRequest;
import com.pet.fileencryptor.service.EncryptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/encryption")
@RequiredArgsConstructor
public class EncryptionController {

  private final EncryptionService encryptionService;

  @PostMapping("/process")
  @Tag(name = "Encryption API", description = "API for file encryption and decryption")
  public ResponseEntity<Void> processFile(@Valid @RequestBody FileEncryptionRequest request) {
    encryptionService.processFile(request);
    return ResponseEntity.ok().build();
  }

}
