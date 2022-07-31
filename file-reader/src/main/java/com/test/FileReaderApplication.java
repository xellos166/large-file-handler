package com.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import com.test.service.DataHandlerService;
import com.test.service.FileProcessorService;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
@ConfigurationPropertiesScan("com.test.config")
public class FileReaderApplication implements CommandLineRunner {

	private final FileProcessorService fileProcessorService;
	private final DataHandlerService dataHandlerService;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FileReaderApplication.class);
		app.run(args);

	}

	@Override
	public void run(String... args) throws Exception {
		fileProcessorService.process("C:\\Users\\DIBANGSHU\\Documents\\app_log.txt");
		dataHandlerService.handleData();
	}

}