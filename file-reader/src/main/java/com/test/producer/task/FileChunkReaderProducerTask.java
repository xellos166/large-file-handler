package com.test.producer.task;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;

import com.google.gson.Gson;
import com.test.model.Event;
import com.test.model.EventInfo;
import com.test.producer.EventProducer;
import com.test.shared.SharedCache;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class FileChunkReaderProducerTask implements EventProducer {

	private List<String> fileBlock;
	private ConcurrentMap<String, EventInfo> sharedMap;
	private BlockingQueue<EventInfo> eventQueue;

	@Override
	public void run() {
		log.debug(MessageFormat.format("File block received {0}", fileBlock));
		log.debug(MessageFormat.format("Current thread--> {0}", Thread.currentThread()));
		for (String line : fileBlock) {
			Optional<Event> eventopt = Optional.of(new Gson().fromJson(line.trim(), Event.class));
			if (eventopt.isPresent()) {
				Event event = eventopt.get();
				EventInfo eventInfo = EventInfo.builder().id(event.getId()).host(event.getHost()).type(event.getType())
						.build();
				String eventid = event.getId();
				if (sharedMap.containsKey(eventid)) {
					EventInfo existingEventInfo = sharedMap.get(eventid);
					setAdditionalInfo(event, existingEventInfo, true);
					sharedMap.replace(eventid, existingEventInfo);

				} else {
					setAdditionalInfo(event, eventInfo, true);
					sharedMap.putIfAbsent(eventid, eventInfo);
				}
			}

		}
		log.debug(MessageFormat.format("Shared Map status --> {0}", SharedCache.getSharedMap()));
	}

	private boolean isStartEvent(Event event) {
		return event.getState().equalsIgnoreCase("STARTED") ? true : false;
	}

	private void setAdditionalInfo(Event event, EventInfo eventInfo, boolean lognRunningCheck) {
		boolean isStartEvent = isStartEvent(event);
		if (isStartEvent) {
			eventInfo.setStartTime(event.getTimestamp());
			eventInfo.setStarted(true);
		} else {
			eventInfo.setEndtime(event.getTimestamp());
			eventInfo.setFinished(true);
		}

		if (eventInfo.getEndtime() != null && eventInfo.getStartTime() != null && lognRunningCheck) {
			boolean isLongRunning = (eventInfo.getEndtime() - eventInfo.getStartTime() > 4) ? true : false;
			eventInfo.setLonRunning(isLongRunning);
			if (isLongRunning) {
				produce(eventInfo);
			}
		}
	}

	@Override
	public void produce(EventInfo eventInfo) {
		try {
			log.debug(MessageFormat.format("Event produced {0}", eventInfo));
			eventQueue.put(eventInfo);
			log.debug("Event queue size " + eventQueue.size());
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

	}

}
