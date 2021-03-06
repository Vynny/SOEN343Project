package com.soen343.client;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationMessage {

	final Logger logger = LoggerFactory.getLogger(ReservationMessage.class);

	private Long userId;
	private Long roomId;
	private String startTime;
	private String endTime;

	@JsonCreator
	public ReservationMessage(@JsonProperty("userId") Long userId, @JsonProperty("roomId") Long roomId,
			@JsonProperty("startTime") String startTime, @JsonProperty("endTime") String endTime) {
		this.setUserId(userId);
		this.setRoomId(roomId);	
		this.setStartTime(startTime);
		this.setEndTime(endTime);
	}

	@JsonProperty
	public Long getUserId() {
		return userId;
	}

	@JsonProperty
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@JsonProperty
	public Long getRoomId() {
		return roomId;
	}

	@JsonProperty
	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	@JsonProperty
	public String getStartTime() {
		return startTime;
	}

	@JsonProperty
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@JsonProperty
	public String getEndTime() {
		return endTime;
	}

	@JsonProperty
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String toString() {
		return "userId: " + getUserId() + ", roomId: " + getRoomId() + ", startTime: " + getStartTime() + ", endTime: " + getEndTime();
	}

}
