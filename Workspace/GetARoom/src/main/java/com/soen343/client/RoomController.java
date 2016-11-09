package com.soen343.client;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.Timed;
import com.soen343.core.RoomMapper;
import com.soen343.db.RoomTDG;
import com.soen343.domain.Room;
import com.soen343.mappers.RoomIdentityMap;

@Path("/room")
@Produces(MediaType.APPLICATION_JSON)
public class RoomController {
	
	private RoomMapper roomMapper;
	private RoomTDG roomTDG;

    public RoomController(RoomTDG roomTDG) {
    	this.roomTDG = roomTDG;
    	this.roomMapper = new RoomMapper(roomTDG);
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
    }
}