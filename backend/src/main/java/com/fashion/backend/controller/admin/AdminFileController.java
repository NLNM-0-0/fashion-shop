package com.fashion.backend.controller.admin;

import com.fashion.backend.payload.file.FileLinkResponse;
import com.fashion.backend.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin/file")
@RequiredArgsConstructor
@Tag(
		name = "File"
)
public class AdminFileController {
	private final FileService fileService;

	@PostMapping(consumes = "multipart/form-data")
	@Operation(
			summary = "Upload file",
			description = "Upload file for application. Only allows: Image (.png, .jpg, .jpeg, .gif, .svg)"
	)
	@ApiResponse(
			responseCode = "200",
			description = "Http Status is 200 OK"
	)
	@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
	public ResponseEntity<FileLinkResponse> upload(@RequestParam("file") MultipartFile multipartFile) {
		return new ResponseEntity<>(fileService.upload(multipartFile), HttpStatus.OK);
	}
}

