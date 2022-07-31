package com.test.producer;

import com.test.model.EventInfo;

public interface EventProducer extends Runnable {

	public void produce(EventInfo eventInfo);
}
