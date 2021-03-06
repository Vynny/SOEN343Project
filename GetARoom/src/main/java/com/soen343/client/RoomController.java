package com.soen343.client;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;
import com.soen343.core.Room;
import com.soen343.db.RoomTDG;
import com.soen343.mappers.ReservationMapper;
import com.soen343.mappers.RoomMapper;
import com.soen343.session.ReservationSession;
import com.soen343.session.ReservationSessionManager;
import com.soen343.session.ReservationSessionMessage;

@Path("/room")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomController {

	final Logger logger = LoggerFactory.getLogger(RoomController.class);

	private ReservationSessionManager reservationSessionManager;
	private RoomMapper roomMapper;

	public RoomController(RoomTDG roomTDG, ReservationSessionManager reservationSessionManager) {
		this.roomMapper = new RoomMapper(roomTDG);
		this.reservationSessionManager = reservationSessionManager;
	}

	@GET
	@Path("/")
	@Timed
	public List<Room> getAllRooms() {
		List<Room> rooms = roomMapper.getAll();
		if (rooms != null) {
			return rooms;
		} else {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
	}

	@GET
	@Path("/{id}")
	@Timed
	public Room getRoom(@PathParam("id") Integer id) {
		Room room = roomMapper.get(id);
		if (room != null) {
			return room;
		} else {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		// Room room = new Room(0, "Test Room");
	}

	@POST
	@Path("/verifyReservationSession")
	public ReservationSessionMessage verifyReservationSession(ReservationSessionMessage request) {
		logger.info("Request is: \n\t" + request);
		ReservationSessionMessage response = request;

		if (!reservationSessionManager.doesSessionExist(request)) {
			request.setValid(true);
		}

		return response;
	}

	@POST
	@Path("/initateReservationSession")
	public ReservationSessionMessage initateReservationSession(ReservationSessionMessage request) {
		ReservationSessionMessage response = request;
		ReservationSession newSession = reservationSessionManager.validateSessionRequest(request);

		if (newSession != null) {
			request.setValid(true);
		}

		return response;
	}

	@POST
	@Path("/destroyReservationSession")
	public Response destroyReservationSession(ReservationSessionMessage request) {	
		logger.info("Trying to destroy session from request: \n\t" + request);
		reservationSessionManager.destroySessionRequest(request);
		
		return Response.ok().build();
	}
}
