package com.test.shared;

import java.util.concurrent.ConcurrentHashMap;

import com.test.model.EventInfo;

import lombok.Getter;

public class SharedCache {

	@Getter
	private static ConcurrentHashMap<String, EventInfo> sharedMap = new ConcurrentHashMap<String, EventInfo>();

}
