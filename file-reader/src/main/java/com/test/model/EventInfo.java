package com.test.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class EventInfo {

	private String id;
	private String host;
	private String type;
	private boolean isStarted;
	private boolean isFinished;
	private Long startTime;
	private Long endtime;
	private boolean isLonRunning;

}
