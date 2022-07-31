package com.test.consumer.task;

import java.text.MessageFormat;
import java.util.concurrent.BlockingQueue;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.test.consumer.EventConsumer;
import com.test.dao.EventRepository;
import com.test.model.EventInfo;
import com.test.model.entity.EventDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class EventDBConsumerTask implements EventConsumer {

	private BlockingQueue<EventInfo> eventQueue;
	private EventRepository repository;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void consume(EventInfo eventInfo) {
		EventDto eventDto = EventDto.builder().eventId(eventInfo.getId()).alert(eventInfo.isLonRunning())
				.duration(eventInfo.getEndtime() - eventInfo.getStartTime()).type(eventInfo.getType())
				.host(eventInfo.getHost()).build();
		log.debug(MessageFormat.format("Entry to be inserted in DB {0}", eventDto));
		repository.save(eventDto);

	}

	@Override
	public void run() {
		try {
			while (true) {

				EventInfo eventTobeconsumed = eventQueue.take();
				log.debug(MessageFormat.format("Event to be condumed {0}", eventTobeconsumed));
				consume(eventTobeconsumed);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
