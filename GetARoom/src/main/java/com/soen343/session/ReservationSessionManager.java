package com.soen343.session;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soen343.client.ReservationController;
import com.soen343.client.ReservationMessage;
import com.soen343.client.RoomController;

public class ReservationSessionManager {

	final Logger logger = LoggerFactory.getLogger(ReservationSessionManager.class);
	private List<ReservationSession> sessionList;
	public static ReservationController reservationController;
	public static RoomController roomController;

	public ReservationSessionManager() {
		sessionList = new ArrayList<ReservationSession>();
	}
	
	public ReservationSession getSessionByUserId(long userId) {
		Iterator<ReservationSession> it = sessionList.iterator();
		while (it.hasNext()) {
			ReservationSession session = it.next();
			if (session.getUserId() == userId) {
				return session;
			}
		}
		return null;
	}
	
	public boolean doesSessionExist(ReservationSessionMessage request) {
		Iterator<ReservationSession> it = sessionList.iterator();
		while (it.hasNext()) {
			ReservationSession session = it.next();
			if (session.getRoomId() == request.getRoomId() && session.getDay().equals(request.getDay())) {
				return true;
			}
		}
		
		return false;
	}
	
	public ReservationSession validateSessionRequest(ReservationSessionMessage request) {
		ReservationSession newSession = null;
		boolean foundMatch = false;
		Iterator<ReservationSession> it = sessionList.iterator();
		while (it.hasNext()) {
			ReservationSession session = it.next();
			if (session.getRoomId() == request.getRoomId() && session.getDay().equals(request.getDay())) {
				foundMatch = true;
			}
		}
		
		if (!foundMatch) {
			newSession = new ReservationSession(request.getUserId(), request.getRoomId(), request.getDay());
			logger.info("Creating new ReservationSession with data: \n\t" + newSession);
			addSession(newSession);
		} 
		
		return newSession;
	}
	
	public void destroySessionRequest(ReservationSessionMessage request) {
		Iterator<ReservationSession> it = sessionList.iterator();
		while (it.hasNext()) {
			ReservationSession session = it.next();
			if (session.getRoomId() == request.getRoomId() && session.getDay().equals(request.getDay())) {
				logger.info("Found session! \n\t " + session);
				it.remove();
			}
		}
	}

	private void addSession(ReservationSession session) {
		sessionList.add(session);
	}

	public void setReservationController(ReservationController reservationController) {
		ReservationSessionManager.reservationController = reservationController;
	}

	public void setRoomController(RoomController roomController) {
		ReservationSessionManager.roomController = roomController;
	}

}
