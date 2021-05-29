package com.pm.s3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Configuration {

	@Autowired
	private Environment env;
	
	@Bean
    public AmazonS3 s3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(env.getProperty("cloud.aws.credentials.access-key"), env.getProperty("cloud.aws.credentials.secret-key"));
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(env.getProperty("cloud.aws.region.static")).build();
    }
}
