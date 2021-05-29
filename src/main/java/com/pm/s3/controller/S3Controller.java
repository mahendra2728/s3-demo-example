package com.pm.s3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pm.s3.service.S3Service;

@RestController
public class S3Controller {

	@Autowired
	private S3Service service;

	@PostMapping("/v1/aws/s3/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		return new ResponseEntity<String>(service.uploadFile(file), HttpStatus.OK);
	}

	@GetMapping("/v1/aws/s3/upload/{fileName}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable("fileName") String fileName) {
		byte[] data = service.downloadFile(fileName);
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + fileName + "\"").body(resource);
	}

	@DeleteMapping("/v1/aws/s3/upload/{fileName}")
	public String deleteFile(@PathVariable("fileName") String fileName) {
		return service.deleteFile(fileName);
	}
}
