package com.test.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.test.model.EventInfo;
import com.test.producer.task.FileChunkReaderProducerTask;
import com.test.service.FileProcessorService;
import com.test.shared.SharedCache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcurrentFileProcessorImpl implements FileProcessorService {

	private BlockingQueue<EventInfo> queue;
	private ExecutorService ecExecutorService;
	private Integer chunkSize;

	public ConcurrentFileProcessorImpl(Integer poolSize, Integer chunkSize, BlockingQueue<EventInfo> queue) {
		this.queue = queue;
		this.ecExecutorService = Executors.newFixedThreadPool(poolSize);
		this.chunkSize = chunkSize;
	}

	@Override
	public void process(String filepath) {
		List<String> items = new ArrayList<>();
		try {
			Files.lines(Paths.get(filepath))

					.forEach(line -> {

						items.add(line);
						if (items.size() % chunkSize == 0) {
							// Create chunkprocessor task
							FileChunkReaderProducerTask task = new FileChunkReaderProducerTask(new ArrayList<>(items),
									SharedCache.getSharedMap(), queue);
							ecExecutorService.execute(task);
							items.clear();
						}
					});
			if (items.size() > 0) {
				// add task for remaining rows
				FileChunkReaderProducerTask task = new FileChunkReaderProducerTask(items, SharedCache.getSharedMap(),
						queue);
				ecExecutorService.execute(task);

			}
		} catch (IOException e) {
			log.error("Erro occured in Producer Servie ", e);
		}
		ecExecutorService.shutdown();

	}

}
