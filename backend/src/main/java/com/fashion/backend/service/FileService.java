package com.fashion.backend.service;

import com.fashion.backend.constant.ApplicationConst;
import com.fashion.backend.constant.Message;
import com.fashion.backend.exception.AppException;
import com.fashion.backend.payload.file.FileLinkResponse;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FileService {

	@Transactional
	public FileLinkResponse upload(MultipartFile file) {
		try {
			String fileName = file.getOriginalFilename();
			assert fileName != null;

			String onlyFileName = getOnlyName(fileName);
			String extension = getExtension(fileName);

			String postfix = UUID.randomUUID().toString().concat(extension);
			fileName = onlyFileName + "_" + postfix;

			File convertedFile = convertToFile(file, fileName);

			String fileUploadType = getFileUploadType(extension);
			String url = uploadFile(convertedFile, fileName, fileUploadType);
			if (!convertedFile.delete()) {
				throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, Message.COMMON_ERR);
			}
			return new FileLinkResponse(url);
		} catch (Exception e) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, Message.File.FILE_UPLOAD_FAIL);
		}
	}

	private String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	private String getOnlyName(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
		File tempFile = new File(fileName);
		try (FileOutputStream fos = new FileOutputStream(tempFile)) {
			fos.write(multipartFile.getBytes());
		}
		return tempFile;
	}

	private String getFileUploadType(String extensionPath) {
		return switch (extensionPath) {
			case ".png" -> "image/png";
			case ".svg" -> "image/svg+xml";
			case ".jpeg", ".jpg" -> "image/jpeg";
			case ".gif" -> "image/gif";
			default -> throw new AppException(HttpStatus.BAD_REQUEST, Message.File.FILE_INVALID_FORMAT);
		};
	}

	private String uploadFile(File convertedFile, String fileName, String fileUploadType) throws IOException {
		BlobId blobId = BlobId.of(ApplicationConst.BUCKET_NAME, fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(fileUploadType).build();
		InputStream inputStream = FileService.class.getClassLoader().getResourceAsStream("firebase-private-key.json");
		assert inputStream != null;
		Credentials credentials = GoogleCredentials.fromStream(inputStream);
		Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
		storage.create(blobInfo, Files.readAllBytes(convertedFile.toPath()));

		String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/" + ApplicationConst.BUCKET_NAME +
							 "/o/%s?alt=media";
		return String.format(downloadUrl, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
	}
}