package com.test.shared;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.test.model.EventInfo;

import lombok.Getter;

public class CommonEventQueue {

	@Getter
	private static BlockingQueue<EventInfo> eventQuequ = new LinkedBlockingQueue<>(20);
}
