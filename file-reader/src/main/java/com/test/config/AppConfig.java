package com.test.config;

import java.text.MessageFormat;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.test.service.DataHandlerService;
import com.test.service.FileProcessorService;
import com.test.service.impl.ConcurrentFileProcessorImpl;
import com.test.service.impl.DataAccessServiceImpl;
import com.test.shared.CommonEventQueue;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppConfig {

	@Value("${config.producer.poolsize:4}")
	private Integer producerPoolSize;

	@Value("${config.consumer.poolsize:4}")
	private Integer consumerPoolSize;

	@Value("${config.file.chunksize:2}")
	private Integer chunkSize;

	/*
	 * @Bean(initMethod = "start", destroyMethod = "stop") public Server
	 * hsqlServer(@Value("classpath:/application.properties") Resource props) throws
	 * IOException, AclFormatException { Server bean = new Server();
	 * bean.setProperties(PropertiesLoaderUtils.loadProperties(props)); return bean;
	 * }
	 * 
	 * @Bean
	 * 
	 * @DependsOn("hsqlServer") public DataSource getDataSource(@Autowired
	 * DataSourceProperties dsProps) { DataSourceBuilder dataSourceBuilder =
	 * DataSourceBuilder.create();
	 * dataSourceBuilder.driverClassName(dsProps.getDriverClassName());
	 * dataSourceBuilder.url(dsProps.getUrl());
	 * dataSourceBuilder.username(dsProps.getUsername());
	 * dataSourceBuilder.password(dsProps.getPassword()); return
	 * dataSourceBuilder.build(); }
	 */

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DataSource dataSource = createDataSource();
		// DatabasePopulatorUtils.execute(createDatabasePopulator(), dataSource);
		return dataSource;
	}

	/*
	 * private DatabasePopulator createDatabasePopulator() {
	 * ResourceDatabasePopulator databasePopulator = new
	 * ResourceDatabasePopulator(); databasePopulator.setContinueOnError(true);
	 * databasePopulator.addScript(new ClassPathResource("schema.sql")); return
	 * databasePopulator; }
	 */

	private DataSource createDataSource() {
		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName("org.hsqldb.jdbc.JDBCDriver");
		dataSourceBuilder.url("jdbc:hsqldb:hsql://localhost/xdb");
		dataSourceBuilder.username("SA");
		dataSourceBuilder.password("");
		return dataSourceBuilder.build();
	}

	@Bean
	FileProcessorService fileProcessorService() {
		log.info(MessageFormat.format(
				"Initializing file processor service bean with poolSize: {0} and file chunk Size: {1}",
				producerPoolSize, chunkSize));
		return new ConcurrentFileProcessorImpl(producerPoolSize, chunkSize, CommonEventQueue.getEventQuequ());
	}

	@Bean
	DataHandlerService dataHandlerService() {
		return new DataAccessServiceImpl(CommonEventQueue.getEventQuequ(), consumerPoolSize);
	}
}
