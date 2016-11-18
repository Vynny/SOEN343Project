package com.soen343.session;

import org.joda.time.DateTime;

public class ReservationSession {

	private Long userId;
	private Long roomId;
	private DateTime day;

	public ReservationSession(long userId, long roomId, DateTime day) {
		this.userId = userId;
		this.roomId = roomId;
		this.day = day;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public DateTime getDay() {
		return day;
	}

	public void setDay(DateTime day) {
		this.day = day;
	}
	
	public String toString() {
		return "userId: " + getUserId() + ", roomId: " + getRoomId() + " , day: " + getDay().toDate();
	}

}