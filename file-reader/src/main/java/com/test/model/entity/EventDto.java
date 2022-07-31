package com.test.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "EVENT_DETAILS")
public class EventDto implements Serializable {

	private static final long serialVersionUID = 3952291495303128674L;

	@Id
	@Column(name = "EVENT_ID")
	private String eventId;

	@Column(name = "EVENT_DURATION")
	private Long duration;

	@Column(name = "EVENT_TYPE")
	private String type;
	@Column(name = "HOST")
	private String host;
	@Column(name = "ALERT")
	private Boolean alert;

}
