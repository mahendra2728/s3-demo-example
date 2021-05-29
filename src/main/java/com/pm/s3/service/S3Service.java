package com.pm.s3.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class S3Service {

	@Autowired
	private Environment env;
	
	@Autowired
	private AmazonS3 s3Client;
	
	
	public String uploadFile(MultipartFile  file) {
		
		String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
		File fileObject=convertMultipartToFile(file);
		s3Client.putObject(env.getProperty("bucket.name"),fileName,fileObject);
		fileObject.delete();
		return "File Uploaded Successfully "+fileName;
		
	}
	
	public byte[] downloadFile(String fileName) {
		
		S3Object s3Object= s3Client.getObject(env.getProperty("bucket.name"), fileName);
		S3ObjectInputStream sois= s3Object.getObjectContent();
		try {
			return com.amazonaws.util.IOUtils.toByteArray(sois);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return null;
	}
	
	public String deleteFile(String fileName) {
		s3Client.deleteObject(env.getProperty("bucket.name"), fileName);
		return "file deleted successfully "+fileName;
	}
	
	
	public static File convertMultipartToFile(MultipartFile file) {
		File convertedFile=new File(file.getOriginalFilename());
		try(FileOutputStream fos=new FileOutputStream(convertedFile)){
			fos.write(file.getBytes());
		}catch(Exception e) {
		log.error("Invalid file");	
		}
		return convertedFile;
	}
}
