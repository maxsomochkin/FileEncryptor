# Getting Started

### Overview

This document provides details on how to use the FileEncryptionRequest and associated components for encrypting and decrypting files. It covers the configuration, structure, and functionalities of the classes, annotations, and dependencies.
### Table of Contents

1. Class Descriptions
   * FileEncryptionRequest
   * EncryptionController
   * EncryptionServiceImpl
2. API Endpoints
3. Error Handling

## Class Descriptions
### FileEncryptionRequest
This is a DTO (Data Transfer Object) that contains the request payload for file encryption or decryption.
The following guides illustrate how to use some features concretely:

* Fields:
  * inputFilePath (String): Path of the input file to be encrypted or decrypted.
  * outputFilePath (String): Path where the encrypted/decrypted file will be saved.
  * encryptionKey (String): Encryption key, must be at least 32 characters.
  * isEncryptMode (boolean): Determines if the file will be encrypted (true) or decrypted (false).

### EncryptionController

A REST controller that provides an API endpoint for file encryption and decryption.

* Method:
  * processFile: Accepts a FileEncryptionRequest, validates it, and processes encryption or decryption


### EncryptionServiceImpl
A service class implementing EncryptionService that handles the encryption and decryption logic.
* Methods:
  * processFile: Submits encryption or decryption tasks to an executor service.
  * createSecretKey: Converts a string key into a SecretKeySpec object.
  * initializeCipher: Initializes a cipher with AES/CTR/NoPadding mode.
  * processFileData: Reads input file data, processes it, and writes the output file.

### API Endpoints
* POST /api/encryption/process

Processes a file for encryption or decryption based on the request payload.

* Request Body (FileEncryptionRequest):
  * inputFilePath (String, required): Path of the file to be processed.
  * outputFilePath (String, required): Destination path for the output.
  * encryptionKey (String, required): Key for encryption/decryption; must be at least 32 characters.
  * isEncryptMode (boolean, required): Indicates whether to encrypt (true) or decrypt (false).

* Response:
  * 200 OK: File processing was successful.

### Error Handling

* Validation Errors:
  * Missing or invalid fields will result in HTTP 400 Bad Request with detailed error messages.
* Encryption/Decryption Errors:
  * If encryption or decryption fails, a FileEncryptorException is thrown with an appropriate message.
