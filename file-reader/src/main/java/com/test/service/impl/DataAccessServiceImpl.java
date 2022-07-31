package com.test.service.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.consumer.task.EventDBConsumerTask;
import com.test.dao.EventRepository;
import com.test.model.EventInfo;
import com.test.service.DataHandlerService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DataAccessServiceImpl implements DataHandlerService {

	private ExecutorService executorService;
	private BlockingQueue<EventInfo> queue;

	@Autowired
	private EventRepository eventRepository;

	public DataAccessServiceImpl(BlockingQueue<EventInfo> queue, Integer poolSize, EventRepository eventRepository) {

		this.executorService = Executors.newFixedThreadPool(poolSize);
		this.queue = queue;
		this.eventRepository = eventRepository;

	}

	public DataAccessServiceImpl(BlockingQueue<EventInfo> queue, Integer poolSize) {

		this.executorService = Executors.newFixedThreadPool(poolSize);
		this.queue = queue;

	}

	@Override
	public void handleData() {
		executorService.execute(new EventDBConsumerTask(queue, eventRepository));
	}
}
