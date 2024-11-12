package com.pet.fileencryptor.service;

import com.pet.fileencryptor.dto.request.FileEncryptionRequest;

public interface EncryptionService {

  void processFile(FileEncryptionRequest request);
}
