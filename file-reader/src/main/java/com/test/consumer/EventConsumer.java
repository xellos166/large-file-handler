package com.test.consumer;

import com.test.model.EventInfo;

public interface EventConsumer extends Runnable {

	public void consume(EventInfo eventInfo);
}
